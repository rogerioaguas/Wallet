package com.example.brunocolombini.wallet.login

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.User
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.feature.login.LoginContract
import com.example.brunocolombini.wallet.feature.login.LoginPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class LoginPresenterTest {


    @Mock
    lateinit var view: LoginContract.View

    @InjectMocks
    lateinit var presenter: LoginPresenter

    @Mock
    lateinit var appDataBase: AppDatabase

    @Mock
    lateinit var userDao: UserDao

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter.db = appDataBase
    }

    @Test
    fun user_exist_in_local_database() {
        val user = User(id = 1, username = "ABC", email = "abc@abc.com", password = "7C4A8D09CA3762AF61E59520943DC26494F8941B")
        `when`(presenter.db.userDao()).thenReturn(userDao)
        `when`(presenter.db.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(user)
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).doLogin(ArgumentMatchers.any(User::class.java))
    }

    @Test
    fun user_not_exist_in_local_database() {
        `when`(presenter.db.userDao()).thenReturn(userDao)
        `when`(presenter.db.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null)
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).callUserNotExist()
    }

}