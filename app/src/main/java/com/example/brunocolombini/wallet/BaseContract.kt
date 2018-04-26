package com.example.brunocolombini.wallet

interface BaseContract {
    interface View
    interface Presenter {
        fun onAttachView()
        fun onDestroy()
    }
}
