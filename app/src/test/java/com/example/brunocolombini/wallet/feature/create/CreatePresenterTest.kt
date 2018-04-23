package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
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

class CreatePresenterTest :BaseTest() {

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
    fun save_user_ok() {
        val user = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.extractDao()).thenReturn(extractDao)

        `when`(appDataBase.userDao().findByUser(any(), any())).thenReturn(Single.just(user))

        presenter.saveUser("ABC", "123456")
        verify(view, times(1)).success()
    }

    @Test
    fun save_user_error() {
        presenter.saveUser("", "")
        verify(view, Mockito.times(1)).errorAlert()

    }
}