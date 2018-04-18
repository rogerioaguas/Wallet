package com.example.brunocolombini.wallet.feature.login

import android.content.Context
import com.example.brunocolombini.wallet.DAO.user.User

interface LoginContract {
    interface View {
        fun callUserNotExist()
        fun doLogin(user: User?)
    }

    interface Presenter {
        fun checkUserExist(email: String, password: String)
        fun onAttachView(context: Context)
    }
}