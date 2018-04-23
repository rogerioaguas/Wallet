package com.example.brunocolombini.wallet.data

data class MercadoBitcoinModel(val ticker: Ticker)
data class Ticker(
        val high: String,
        val low: String,
        val vol: String,
        val last: String,
        val buy: String,
        val sell: String
)
