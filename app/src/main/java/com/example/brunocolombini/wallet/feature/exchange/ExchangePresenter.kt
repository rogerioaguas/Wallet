package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExchangePresenter @Inject constructor(
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>
) : ExchangeContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onAttachView(view: ExchangeContract.View) {
        compositeDisposable.add(changeEventDeliverySubject
                .sample(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))
    }
}