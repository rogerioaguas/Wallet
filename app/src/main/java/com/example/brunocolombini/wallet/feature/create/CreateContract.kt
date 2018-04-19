package com.example.brunocolombini.wallet.feature.create

import android.content.Context

interface CreateContract {
    interface View {
        fun errorAlert()
        fun success()
    }

    interface Presenter {
        fun onAttachView(context: Context)
        fun saveUser(name: String, password: String)
    }
}