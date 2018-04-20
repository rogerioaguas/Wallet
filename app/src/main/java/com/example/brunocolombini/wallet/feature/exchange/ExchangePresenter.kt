package com.example.brunocolombini.wallet.feature.exchange

import android.util.Log
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.data.BancoCentralModel
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

    override fun setBritasPrice() {
        api.getBritasPrice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1: BancoCentralModel ->
                    this.view.setCryptoPrice(BalanceEventType.BRITAS, t1.value[0].cotacaoCompra, t1.value[0].cotacaoVenda)
                }
    }

}