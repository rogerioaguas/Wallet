package com.example.brunocolombini.wallet.feature.create

import android.content.Context
import android.util.Log
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class CreatePresenter @Inject constructor(
        private val view: CreateContract.View) : CreateContract.Presenter {


    lateinit var db: AppDatabase
    private val mDisposable = CompositeDisposable()

    override fun onAttachView(context: Context) {
        db = AppDatabase.getInstance(context)!!
    }

    override fun saveUser(name: String, password: String) {
        if (checkUserFieldValidade(name, password)) {
            view.errorAlert()
            return
        }
        val user = UserWallet(id = null, username = name, password = HashUtils.sha1(password))
        mDisposable.add(Completable
                .fromAction { db.userDao().insertAll(user) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.success() }, { view.errorAlert() }))
    }

    private fun checkUserFieldValidade(name: String, password: String): Boolean = name.isEmpty() || password.isEmpty()
}