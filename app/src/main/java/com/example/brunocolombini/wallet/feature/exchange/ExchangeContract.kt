package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.BaseContract
import com.example.brunocolombini.wallet.feature.home.MarketType
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent

interface ExchangeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, value: Double)
        fun setCryptoPrice(market: BalanceEventType, bid: Double, ask: Double)
        fun extractUpdateWithSuccess()
        fun alertDontHaveBalance()
        fun getStringByResourceId(resourceId: Int): String
    }


    interface Presenter : BaseContract.Presenter {
        fun getBritasPrice()
        fun getBtcPrice()
        fun updateBalance()
        fun updateExtract(amount: Double, eventType: String, exchangeEvent: ExchangeEvent)
        fun updateBalanceAfterExchangeEvent(sell: ExchangeEvent, marketType: MarketType, newBalanceFiat: Double, newBalanceCrypto: Double)
    }
}