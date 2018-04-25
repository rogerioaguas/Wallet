package com.example.brunocolombini.wallet.util.delivery

import com.example.brunocolombini.wallet.util.enums.BalanceEventType

open class UpdateBalanceEvent(val eventType: BalanceEventType, val value: Double)