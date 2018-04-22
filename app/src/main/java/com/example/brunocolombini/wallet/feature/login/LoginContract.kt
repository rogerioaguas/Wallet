package com.example.brunocolombini.wallet.feature.login

import android.content.Context
import com.example.brunocolombini.wallet.DAO.user.UserWallet

interface LoginContract {
    interface View {
        fun callUserNotExist()
        fun doLogin(user: UserWallet)
    }

    interface Presenter {
        fun checkUserExist(username: String, password: String)
        fun onAttachView(context: Context)
        fun onDestroy()
    }
}