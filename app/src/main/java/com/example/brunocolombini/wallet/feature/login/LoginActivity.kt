package com.example.brunocolombini.wallet.feature.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.create.CreateActivity
import com.example.brunocolombini.wallet.feature.home.HomeActivity
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
        sign_in.setOnClickListener { presenter.checkUserExist(login_user.text.toString(), login_password.text.toString()) }
        sign_up.setOnClickListener { startActivity(CreateActivity.getCallingIntent(this)) }
    }

    override fun callUserNotExist() {
        val errorMessage = "Campo usuario ou senha inválido!"
        val snackbar = Snackbar.make(login_user, errorMessage, Snackbar.LENGTH_SHORT)
        snackbar.setAction("Entendi") { snackbar.dismiss() }
        snackbar.show()
    }

    override fun doLogin(user: UserWallet) {
        startActivity(HomeActivity.getCallingIntent(this, user = user))
    }


}
