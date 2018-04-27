package com.example.brunocolombini.wallet.feature.create

interface CreateContract {
    interface View {
        fun errorAlert()
        fun success()
    }

    interface Presenter {
        fun saveUser(name: String, password: String)
    }
}