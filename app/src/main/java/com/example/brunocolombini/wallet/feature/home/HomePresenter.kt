package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class HomePresenter @Inject constructor(
        val view: HomeContract.View,
        private val userPreference: UserPreference,
        private val changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>,
        private val appDatabase: AppDatabase) : HomeContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onAttachView() {
        compositeDisposable.add(changeEventDeliverySubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: UpdateBalanceEvent ->
                    view.updateBalance(t.eventType, t.value)
                }))

    }

    override fun onDestroy() {
        userPreference.clear()
        compositeDisposable.clear()
    }

    override fun updateUserInformation() {
        compositeDisposable.add(
                appDatabase.extractDao()
                        .getGroupExtractById(userPreference.getUserId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t: List<Extract> ->
                            view.updateUserInformation(t)
                        })
        )
    }

}
