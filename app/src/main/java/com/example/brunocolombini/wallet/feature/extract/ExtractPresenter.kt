package com.example.brunocolombini.wallet.feature.extract

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ExtractPresenter @Inject constructor(
        private val view: ExtractContract.View,
        private val userPreference: UserPreference,
        private val appDatabase: AppDatabase) : ExtractContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getAllExtracts() {
        compositeDisposable.add(appDatabase.extractDao()
                .getExtractById(userPreference.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t -> view.setRecycleView(t) })

    }


}