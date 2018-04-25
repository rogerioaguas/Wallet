package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.data.BancoCentralModel
import com.example.brunocolombini.wallet.data.MercadoBitcoinModel
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ExchangePresenter @Inject constructor(
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>,
        private val api: Api,
        private val userPreference: UserPreference,
        private val appDatabase: AppDatabase,
        val view: ExchangeContract.View
) : ExchangeContract.Presenter {


    private val compositeDisposable = CompositeDisposable()

    override fun onAttachView() {
        compositeDisposable.add(changeEventDeliverySubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    override fun setCoinPrice(marketType: BalanceEventType) {
        when (marketType) {
            BalanceEventType.BTC -> getBtcPrice()
            else -> getBritasPrice()
        }
    }

    private fun getBritasPrice() {
        api.getBritasPrice("05-25-2018")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ticker: BancoCentralModel ->
                    this.view.setCryptoPrice(BalanceEventType.BRITAS, ticker.value[0].cotacaoCompra, ticker.value[0].cotacaoVenda)
                }, {})
    }


    /**
    The method call the api for make request to get the coin value from mercado bitcoin.
    @param null
    @return void
    @throw null
     */
    private fun getBtcPrice() {
        api.getBtcPrice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { ticker: MercadoBitcoinModel ->
                    this.view.setCryptoPrice(BalanceEventType.BTC, ticker.ticker.buy.toDouble(), ticker.ticker.sell.toDouble())
                }
    }


    override fun updateBalance() {
        compositeDisposable.add(
                appDatabase.extractDao()
                        .getGroupExtractById(userPreference.getUserId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ t: List<Extract> ->
                            updateBalanceView(t)
                        }))

    }

    private fun updateBalanceView(extracts: List<Extract>) {
        for (extract in extracts) {
            when (extract.coin) {
                view.getStringByResourceId(R.string.fiat) -> {
                    view.updateBalance(BalanceEventType.FIAT, extract.amount)
                }
                view.getStringByResourceId(R.string.btc) -> {
                    view.updateBalance(BalanceEventType.BTC, extract.amount)
                }
                view.getStringByResourceId(R.string.bts) -> {
                    view.updateBalance(BalanceEventType.BRITAS, extract.amount)
                }
            }
        }
    }

    override fun updateBalanceAfterExchangeEvent(exchangeEvent: ExchangeEvent,
                                                 marketType: BalanceEventType,
                                                 newBalanceFiat: Double,
                                                 newBalanceCrypto: Double,
                                                 total: Double,
                                                 quantity: Double) {

        if (newBalanceFiat < 0 || newBalanceCrypto < 0) {
            view.alertNotHaveBalance()
            return
        }

        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, newBalanceFiat))
        changeEventDeliverySubject.onNext(UpdateBalanceEvent(marketType, newBalanceCrypto))

        if (exchangeEvent == ExchangeEvent.BUY) {
            updateExtract(
                    Extract(null, userPreference.getUserId(), total * -1, view.getStringByResourceId(BalanceEventType.FIAT.type)),
                    Extract(null, userPreference.getUserId(), quantity * 1, view.getStringByResourceId(marketType.type)))

        } else {
            updateExtract(
                    Extract(null, userPreference.getUserId(), total * 1, view.getStringByResourceId(BalanceEventType.FIAT.type)),
                    Extract(null, userPreference.getUserId(), quantity * -1, view.getStringByResourceId(marketType.type)))

        }

    }


    private fun updateExtract(vararg extract: Extract) {
        compositeDisposable.add(
                Completable
                        .fromAction { appDatabase.extractDao().insertAll(*extract) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.extractUpdateWithSuccess() }))

    }
}

