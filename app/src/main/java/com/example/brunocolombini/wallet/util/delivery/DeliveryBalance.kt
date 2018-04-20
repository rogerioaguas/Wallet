package com.example.brunocolombini.wallet.util.delivery

open class UpdateBalanceEvent(val eventType: BalanceEventType?, val value: Double)

enum class BalanceEventType {
    FIAT,
    BTC,
    BRITAS
}