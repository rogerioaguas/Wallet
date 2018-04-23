package com.example.brunocolombini.wallet.util


object TransformToNumber {
    fun removeString(string: String): Double = string.replace("R$", "").replace("BTC", "").replace("BRITAS", "").toDouble()
}