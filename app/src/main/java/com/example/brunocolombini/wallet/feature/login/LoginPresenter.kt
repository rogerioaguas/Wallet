package com.example.brunocolombini.wallet.feature.login

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class LoginPresenter @Inject constructor(
        private val view: LoginContract.View,
        private val userPreference: UserPreference,
        private val appDatabase: AppDatabase) : LoginContract.Presenter {

    private val mDisposable = CompositeDisposable()

    override fun onAttachView() {
        if (userPreference.isLogged()) {
            getUserAndLogin(userPreference.getUserId())
        }
    }


    private fun getUserAndLogin(id: Long) {
        mDisposable.add(appDatabase
                .userDao()
                .findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ doActionLogin(it) }, { view.callUserNotExist() }))
    }

    override fun checkUserExist(username: String, password: String) {
        mDisposable.add(
                appDatabase.userDao()
                        .findByUser(username, HashUtils.sha1(password))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            userPreference.saveUserId(it.id!!)
                            userPreference.saveUserName(it.username)
                            doActionLogin(it)
                        }, { view.callUserNotExist() })
        )
    }

    private fun doActionLogin(user: UserWallet) {
        if (user.id != null) {
            view.doLogin(user)
        } else {
            view.callUserNotExist()
        }
    }

    override fun onDestroy() {
        mDisposable.clear()
    }

}
