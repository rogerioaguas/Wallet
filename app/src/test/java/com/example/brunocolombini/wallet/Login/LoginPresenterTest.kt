package com.example.brunocolombini.wallet.Login

import com.example.brunocolombini.wallet.feature.login.LoginActivity
import com.example.brunocolombini.wallet.feature.login.LoginPresenter
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit


class LoginPresenterTest {

    private lateinit var presenter: LoginPresenter

    @Mock
    private lateinit var view: LoginActivity

    @JvmField
    @Rule
    var mockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        presenter = LoginPresenter(view);
    }

    @Test
    fun check_user_in_data_base() {

    }


}