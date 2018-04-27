package com.example.brunocolombini.wallet.feature.login

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`


class LoginPresenterTest :BaseTest(){


    @InjectMocks
    lateinit var presenter: LoginPresenter

    @Mock
    lateinit var view: LoginContract.View

    @Mock
    lateinit var appDataBase: AppDatabase

    @Mock
    lateinit var userPreference: UserPreference

    @Mock
    lateinit var userDao: UserDao


    @Test
    fun user_exist_in_local_database() {
        val user_expect = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Single.just(UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")))
        presenter.checkUserExist("ABC@ABCgit .com", "123456")
        verify(userPreference).saveUserId(eq(user_expect.id!!))
        verify(userPreference).saveUserName(eq(user_expect.username))
        verify(view, times(1)).doLogin(eq(user_expect))
    }

    @Test
    fun user_not_exist_in_local_database() {
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Single.error(Throwable("error")))
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).callUserNotExist()
    }

    @Test
    fun get_user_by_id_success() {
        `when`(userPreference.getUserId()).thenReturn(1)
        `when`(userPreference.isLogged()).thenReturn(true)
        val user_expect = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findById(userPreference.getUserId())).thenReturn(Single.just(user_expect))
        presenter.onAttachView()
        verify(view, times(1)).doLogin(eq(user_expect))
    }

    @Test
    fun get_user_by_id_error() {
        `when`(userPreference.getUserId()).thenReturn(1)
        `when`(userPreference.isLogged()).thenReturn(true)
        val user_expect = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findById(userPreference.getUserId())).thenReturn(Single.error(Throwable("ERROR")))
        presenter.onAttachView()
        verify(view, times(1)).callUserNotExist()
    }

    @Test
    fun on_attach_is_not_logged() {
        presenter.onAttachView()
        Assert.assertFalse(userPreference.isLogged())
    }

}