package com.example.brunocolombini.wallet.feature.exchange

import android.content.Context
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType

interface ExchangeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, value: Double)
        fun setCryptoPrice(britas: BalanceEventType, bid: Double, ask: Double)
        fun getContext(): Context
    }


    interface Presenter {
        fun onAttachView(view: ExchangeContract.View)
        fun getBritasPrice()
        fun getBtcPrice()
        fun updateBalance()
        fun updateExtract(amount: Double, eventType: String, exchangeEvent: ExchangeEvent)
    }
}