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

    /**
     * The method do request to get the current coin price
     * @param marketType coin type
     */
    override fun setCoinPrice(marketType: BalanceEventType) {
        when (marketType) {
            BalanceEventType.BTC -> getBtcPrice()
            else -> getBritasPrice()
        }
    }

    /**
    The method call the api for make request to get the coin value from mercado bitcoin.
    @param null
    @return void
    @throw null
     */
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

    /**
    The method get from database the actual balance.
    @param null
    @return void
    @throw null
     */
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

    /**
    The method will update UI if the new balance
    @param extracts
    @return void
    @throw null
     */
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

    /**
    The method will register the purchase or selling in database.
    @param exchangeEvent buy or sell, use buy when you change the fiat to crypto
    @param MarketType currency type
    @param newBalanceFiat new fiat balance in ui
    @param newBalanceCrypto new crypto balance in ui
    @param total amount fiat currency expanded or recieve
    @param quantity amount currency buy or sell
    @return void
    @throw null
     */

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

    /**
     * Insert in database your trade event
     * @param extract
     */
    private fun updateExtract(vararg extract: Extract) {
        compositeDisposable.add(
                Completable
                        .fromAction { appDatabase.extractDao().insertAll(*extract) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ view.extractUpdateWithSuccess() }))

    }
}

