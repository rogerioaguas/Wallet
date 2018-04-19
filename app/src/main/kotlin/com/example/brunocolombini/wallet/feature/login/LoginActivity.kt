package com.example.brunocolombini.wallet.feature.login

import android.os.Bundle
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.create.CreateActivity.Companion.getCallingIntent
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

open class LoginActivity : DaggerAppCompatActivity(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onAttachView(this)
        sign_up.setOnClickListener { startActivity(getCallingIntent(this)) }
    }

    override fun callUserNotExist() {
    }

    override fun doLogin(user: UserWallet?) {
    }


}
