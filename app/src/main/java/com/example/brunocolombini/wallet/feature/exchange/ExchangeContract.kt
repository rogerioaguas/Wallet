package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.util.delivery.BalanceEventType

interface ExchangeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, value: Double)
    }


    interface Presenter {
        fun onAttachView(view: ExchangeContract.View)
    }
}