package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.data.BancoCentralModel
import com.example.brunocolombini.wallet.data.MercadoBitcoinModel
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
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

    override fun getBritasPrice() {
        api.getBritasPrice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ ticker: BancoCentralModel ->
                    this.view.setCryptoPrice(BalanceEventType.BRITAS, ticker.value[0].cotacaoCompra, ticker.value[0].cotacaoVenda)
                }, {})
    }


    /**
    The method call the api for make request to get the coin value.
    @param null
    @return void
    @throw null
     */
    override fun getBtcPrice() {
        api.getBtcPrice("https://www.mercadobitcoin.net/api/BTC/ticker/")
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


    override fun updateExtract(amount: Double, eventType: String, exchangeEvent: ExchangeEvent) {
        val extract = if (exchangeEvent == ExchangeEvent.BUY) {
            Extract(null, userPreference.getUserId(), amount * 1, eventType)
        } else {
            Extract(null, userPreference.getUserId(), amount * -1, eventType)
        }
        compositeDisposable.add(
                Completable
                        .fromAction { appDatabase.extractDao().insertAll(extract) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.extractUpdateWithSuccess() }))

    }
}

