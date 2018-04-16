package com.example.brunocolombini.wallet.feature.login

import javax.inject.Inject


class LoginPresenter @Inject constructor(
        private val view: LoginContract.View) : LoginContract.Presenter