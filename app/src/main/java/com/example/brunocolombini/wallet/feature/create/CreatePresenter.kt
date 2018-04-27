package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


open class CreatePresenter @Inject constructor(
        private val view: CreateContract.View,
        private val appDatabase: AppDatabase) : CreateContract.Presenter {

    private val mDisposable = CompositeDisposable()
    /**
     * Method will create user account and create your start balance
     * @param name username
     * @param password user password
     */
    override fun saveUser(name: String, password: String) {
        if (checkUserFieldValidade(name, password)) {
            view.errorAlert()
            return
        }
        val user = UserWallet(id = null, username = name, password = HashUtils.sha1(password))
        mDisposable.add(Completable
                .fromAction { appDatabase.userDao().insertAll(user) }
                .andThen(appDatabase.userDao().findByUser(user.username, user.password))
                .flatMap { t ->
                    Single.just(
                            appDatabase.extractDao().insertAll(
                                    Extract(null, t.id!!, 100000.00, "FIAT"),
                                    Extract(null, t.id!!, 0.0, "BITCOIN"),
                                    Extract(null, t.id!!, 0.0, "BRITAS")))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.success() }, { view.errorAlert() }))
    }

    /**
     * check if the name or password is empty
     * @param name username
     * @param password user password
     */
    private fun checkUserFieldValidade(name: String, password: String): Boolean = name.isEmpty() || password.isEmpty()
}
