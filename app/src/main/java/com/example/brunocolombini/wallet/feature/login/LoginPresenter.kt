package com.example.brunocolombini.wallet.feature.login

import android.arch.persistence.room.Room
import android.content.Context
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.util.HashUtils
import javax.inject.Inject


open class LoginPresenter @Inject constructor(
        private val view: LoginContract.View) : LoginContract.Presenter {


    lateinit var db: AppDatabase

    override fun onAttachView(context: Context) {
        db = Room.databaseBuilder(context,
                AppDatabase::class.java, "user").build()
    }

    override fun checkUserExist(email: String, password: String) {
        val user = db.userDao().findByUser(email, HashUtils.sha1(password))
        if (user?.id != null) {
            view.doLogin(user)
        } else {
            view.callUserNotExist()
        }
    }

}