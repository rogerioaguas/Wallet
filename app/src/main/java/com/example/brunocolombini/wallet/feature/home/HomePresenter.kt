package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomePresenter @Inject constructor(
        val view: HomeContract.View,
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>) : HomeContract.Presenter {


    private val compositeDisposable = CompositeDisposable()

    override fun onAttachView() {
        compositeDisposable.add(changeEventDeliverySubject
                .sample(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))
    }

}