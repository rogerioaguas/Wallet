package com.example.brunocolombini.wallet.feature.login

import android.os.Bundle
import com.example.brunocolombini.wallet.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

open class LoginActivity : DaggerAppCompatActivity(), LoginContract.View {

    @Inject
    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
    }


}