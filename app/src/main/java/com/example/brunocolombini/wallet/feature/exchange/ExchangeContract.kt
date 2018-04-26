package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.BaseContract
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent

interface ExchangeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, value: Double)
        fun setCryptoPrice(market: BalanceEventType, bid: Double, ask: Double)
        fun extractUpdateWithSuccess()
        fun alertNotHaveBalance()
        fun getStringByResourceId(resourceId: Int): String
    }


    interface Presenter : BaseContract.Presenter {
        fun updateBalance()
        fun updateBalanceAfterExchangeEvent(exchangeEvent: ExchangeEvent,
                                            marketType: BalanceEventType,
                                            newBalanceFiat: Double,
                                            newBalanceCrypto: Double,
                                            total: Double,
                                            quantity: Double)

        fun setCoinPrice(marketType: BalanceEventType)
    }
}