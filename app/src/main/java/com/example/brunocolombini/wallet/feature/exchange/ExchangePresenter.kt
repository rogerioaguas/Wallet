package com.example.brunocolombini.wallet.feature.exchange

import android.util.Log
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.data.BancoCentralModel
import com.example.brunocolombini.wallet.data.MercadoBitcoinModel
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.security.PrivateKey
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangePresenter @Inject constructor(
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>,
        private val api: Api
) : ExchangeContract.Presenter {

    lateinit var view: ExchangeContract.View
    private val compositeDisposable = CompositeDisposable()

    override fun onAttachView(view: ExchangeContract.View) {
        this.view = view
        compositeDisposable.add(changeEventDeliverySubject
                .sample(500, TimeUnit.MILLISECONDS)
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

}