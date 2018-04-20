package com.example.brunocolombini.wallet.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class TextWatcherCryptoInput(
        val input: EditText,
        val currentPrice: Double,
        val output: EditText
) : TextWatcher {


    override fun afterTextChanged(s: Editable) {
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isEmpty()) {
            output.setText("0.0")
            return
        } else if (input.text.toString().equals(".")) {
            input.setText("0.")
            output.setText("0.0")
        } else {
            val inputValue = s.toString().toDouble()
            output.setText((inputValue * currentPrice.toFloat()).toString())
        }
    }
}