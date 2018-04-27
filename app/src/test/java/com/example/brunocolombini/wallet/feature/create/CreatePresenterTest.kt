package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.ExtractDao
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.nhaarman.mockito_kotlin.any
import io.reactivex.Single
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import com.nhaarman.mockito_kotlin.eq

class CreatePresenterTest : BaseTest() {

    @InjectMocks
    lateinit var presenter: CreatePresenter

    @Mock
    lateinit var view: CreateContract.View

    @Mock
    lateinit var appDataBase: AppDatabase

    @Mock
    lateinit var userDao: UserDao

    @Mock
    lateinit var extractDao: ExtractDao

    @Test
    fun save_user_success() {
        val user = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.extractDao()).thenReturn(extractDao)
        `when`(appDataBase.userDao().findByUser(any(), any())).thenReturn(Single.just(user))

        presenter.saveUser("ABC", "123456")
        verify(appDataBase.extractDao()).insertAll(
                eq(Extract(null, 1, 100000.00, "FIAT")),
                eq(Extract(null, 1, 0.0, "BITCOIN")),
                eq(Extract(null, 1, 0.0, "BRITAS")))
        verify(view, times(1)).success()
    }

    @Test
    fun save_user_error() {
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.extractDao()).thenReturn(extractDao)
        `when`(appDataBase.userDao().findByUser(any(), any())).thenReturn(Single.error(Throwable("Error")))

        presenter.saveUser("ABC", "123456")
        verify(view, times(1)).errorAlert()
    }

    @Test
    fun save_user_nothing_field() {
        presenter.saveUser("", "")
        verify(view, Mockito.times(1)).errorAlert()

    }

    @Test
    fun save_user_nothing_username_field() {
        presenter.saveUser("", "123456")
        verify(view, Mockito.times(1)).errorAlert()

    }

    @Test
    fun save_user_nothing_password_field() {
        presenter.saveUser("ABC", "")
        verify(view, Mockito.times(1)).errorAlert()

    }
}