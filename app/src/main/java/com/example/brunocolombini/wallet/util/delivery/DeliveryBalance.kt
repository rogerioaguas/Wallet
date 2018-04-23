package com.example.brunocolombini.wallet.util.delivery

import com.example.brunocolombini.wallet.R

open class UpdateBalanceEvent(val eventType: BalanceEventType, val value: Double)

enum class BalanceEventType(val type: Int) {
    FIAT(R.string.fiat),
    BTC(R.string.btc),
    BRITAS(R.string.bts)
}