package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class HomePresenter @Inject constructor(
        val view: HomeContract.View,
        private val userPreference: UserPreference,
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>) : HomeContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    lateinit var db: AppDatabase

    override fun onAttachView() {
        db = AppDatabase.getInstance(view.getContext())!!
        compositeDisposable.add(changeEventDeliverySubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))
    }

    override fun onDestroy() {
        userPreference.clear()
    }

    override fun updateUserInformation() {
        compositeDisposable.addAll(
                db.extractDao()
                        .getGroupExtractById(userPreference.getUserId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t: List<Extract> ->
                            view.updateUserInformation(t)
                        })
        )
    }

}
