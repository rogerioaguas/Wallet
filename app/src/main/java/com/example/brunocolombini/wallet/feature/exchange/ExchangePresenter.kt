package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.data.BancoCentralModel
import com.example.brunocolombini.wallet.data.MercadoBitcoinModel
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangePresenter @Inject constructor(
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>,
        private val api: Api,
        private val userPreference: UserPreference
) : ExchangeContract.Presenter {

    lateinit var view: ExchangeContract.View
    private val compositeDisposable = CompositeDisposable()

    lateinit var db: AppDatabase

    override fun onAttachView(view: ExchangeContract.View) {
        this.view = view
        db = AppDatabase.getInstance(view.getContext())!!
        compositeDisposable.add(changeEventDeliverySubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))
    }

    override fun getBritasPrice() {
        api.getBritasPrice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { ticker: BancoCentralModel ->
                    this.view.setCryptoPrice(BalanceEventType.BRITAS, ticker.value[0].cotacaoCompra, ticker.value[0].cotacaoVenda)
                }
    }

    override fun getBtcPrice() {
        api.getBtcPrice("https://www.mercadobitcoin.net/api/BTC/ticker/")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { ticker: MercadoBitcoinModel ->
                    this.view.setCryptoPrice(BalanceEventType.BTC, ticker.ticker.buy.toDouble(), ticker.ticker.sell.toDouble())
                }
    }

    override fun updateBalance() {
        db.extractDao()
                .getGroupExtractById(userPreference.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ t: List<Extract> ->
                    updateBalanceView(t)
                })

    }

    private fun updateBalanceView(extracts: List<Extract>) {
        for (extract in extracts) {
            when (extract.coin) {
                "Fiat" -> {
                    view.updateBalance(BalanceEventType.FIAT, extract.amount)
                }
                "Bitcoin" -> {
                    view.updateBalance(BalanceEventType.BTC, extract.amount)
                }
                "britas" -> {
                    view.updateBalance(BalanceEventType.BRITAS, extract.amount)
                }
            }
        }
    }


    override fun updateExtract(amount: Double, eventType: String, exchangeEvent: ExchangeEvent) {
        val extract = if (exchangeEvent == ExchangeEvent.BUY) {
            Extract(null, userPreference.getUserId(), amount * 1, eventType, Date().toString())
        } else {
            Extract(null, userPreference.getUserId(), amount * -1, eventType, Date().toString())
        }
        db.extractDao().insertAll(extract)

    }
}

enum class ExchangeEvent {
    BUY, SELL
}
