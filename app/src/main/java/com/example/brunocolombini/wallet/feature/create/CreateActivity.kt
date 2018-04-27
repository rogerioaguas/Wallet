package com.example.brunocolombini.wallet.feature.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.example.brunocolombini.wallet.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_create.*
import javax.inject.Inject

open class CreateActivity : DaggerAppCompatActivity(), CreateContract.View {
    @Inject
    lateinit var presenter: CreateContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        sign_up.setOnClickListener {
            presenter.saveUser(register_user.text.toString(), register_password.text.toString());
        }
    }

    override fun errorAlert() {
        val errorMessage = "Campo usuario ou senha inválido!"
        val snackbar = Snackbar.make(register_user, errorMessage, Snackbar.LENGTH_SHORT)
        snackbar.setAction("Entendi") { snackbar.dismiss() }
        snackbar.show()
    }

    override fun success() {
        finish()
    }

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, CreateActivity::class.java)
    }

}
