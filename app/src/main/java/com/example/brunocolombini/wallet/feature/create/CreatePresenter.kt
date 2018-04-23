package com.example.brunocolombini.wallet.feature.create

import android.content.Context
import android.util.Log
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*
import javax.inject.Inject


open class CreatePresenter @Inject constructor(
        private val view: CreateContract.View,
        private val appDatabase: AppDatabase) : CreateContract.Presenter {

    private val mDisposable = CompositeDisposable()
    lateinit var context: Context

    override fun onAttachView(context: Context) {
        this.context = context
    }

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
                                    Extract(null, t.id!!, 100000.00, this.context.getString(R.string.fiat)),
                                    Extract(null, t.id!!, 0.0, this.context.getString(R.string.btc)),
                                    Extract(null, t.id!!, 0.0, this.context.getString(R.string.bts))))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view.success() }, { view.errorAlert() }))
    }

    private fun checkUserFieldValidade(name: String, password: String): Boolean = name.isEmpty() || password.isEmpty()
}
