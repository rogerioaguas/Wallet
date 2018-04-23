package com.example.brunocolombini.wallet.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText


class TextWatcherCryptoInput(
        val input: EditText,
        val currentPrice: Double,
        val output: EditText,
        val button: Button
) : TextWatcher {


    override fun afterTextChanged(s: Editable) {
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isEmpty()) {
            output.setText("0.0")
            button.isEnabled = false
            return
        } else if (input.text.toString().equals(".")) {
            input.setText("0.")
            output.setText("0.0")
        } else {
            val inputValue = s.toString().toDouble()
            output.setText((inputValue * currentPrice.toFloat()).toString())
            button.isEnabled = !(outputiszero(output.text.toString().toDouble()))
        }

    }

    private fun outputiszero(output: Double): Boolean = output <= 0

}