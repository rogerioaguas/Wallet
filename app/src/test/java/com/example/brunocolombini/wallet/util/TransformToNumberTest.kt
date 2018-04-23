package com.example.brunocolombini.wallet.util

import org.junit.Assert
import org.junit.Test

class TransformToNumberTest {

    @Test
    fun remove_string_test() {
        Assert.assertEquals(1000.0, TransformToNumber.removeString("R$ 1000.00"), 0.000001)
    }
}