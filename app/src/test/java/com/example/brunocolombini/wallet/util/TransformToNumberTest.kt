package com.example.brunocolombini.wallet.util

import org.junit.Assert
import org.junit.Test

class TransformToNumberTest {

    @Test
    fun remove_string_test() {
        Assert.assertEquals(123.32, TransformToNumber.transformStringToDouble("R$ 123.32"), 0.000001)
    }
}