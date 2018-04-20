package com.example.brunocolombini.wallet.feature.login

import android.content.Context
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class LoginPresenter @Inject constructor(
        private val view: LoginContract.View) : LoginContract.Presenter {

    private val mDisposable = CompositeDisposable()

    lateinit var db: AppDatabase
    override fun onAttachView(context: Context) {
        db = AppDatabase.getInstance(context)!!
    }

    override fun checkUserExist(username: String, password: String) {
        mDisposable.add(
                db.userDao()
                        .findByUser(username, HashUtils.sha1(password))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ doActionLogin(it) }, { view.callUserNotExist() })
        )
    }

    fun doActionLogin(user: UserWallet) {
        if (user.id != null) {
            view.doLogin(user)
        } else {
            view.callUserNotExist()
        }
    }
}