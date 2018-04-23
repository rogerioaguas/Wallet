package com.example.brunocolombini.wallet.util

import org.junit.Assert
import org.junit.Test


class HashUtilTest {

    val hashUtils: HashUtils = HashUtils
    val password: String = "123456"

    @Test
    fun test_sha1() {
        val expect = "7C4A8D09CA3762AF61E59520943DC26494F8941B"
        Assert.assertEquals(expect, hashUtils.sha1(password))
    }

    @Test
    fun test_sha256() {
        val expect = "8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92"
        Assert.assertEquals(expect, hashUtils.sha256(password))
    }

    @Test
    fun test_sha512() {
        val expect = "BA3253876AED6BC22D4A6FF53D8406C6AD864195ED144AB5C87621B6C233B548BAEAE6956DF346EC8C17F5EA10F35EE3CBC514797ED7DDD3145464E2A0BAB413"
        Assert.assertEquals(expect, hashUtils.sha512(password))
    }

}