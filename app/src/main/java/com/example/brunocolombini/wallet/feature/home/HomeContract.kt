package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.util.delivery.BalanceEventType


interface HomeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType?, balance: Double)
    }

    interface Presenter {
        fun onAttachView()
    }
}