package com.example.brunocolombini.wallet.feature.login

import javax.inject.Inject


open class LoginPresenter @Inject constructor(
        private val view: LoginContract.View) : LoginContract.Presenter {


    override fun sum(int: Int): Int {
        return int + int
    }

}