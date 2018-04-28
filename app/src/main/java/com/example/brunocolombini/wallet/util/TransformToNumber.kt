package com.example.brunocolombini.wallet.util

import java.text.NumberFormat

object TransformToNumber {
    fun transformStringToDouble(string: String): Double {
        val numberFormat = NumberFormat.getNumberInstance()
        val number = numberFormat.parse(string.replace(Regex("[a-zA-Z$ ]"),""))
        return number.toDouble()
    }

}