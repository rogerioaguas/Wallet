package com.example.brunocolombini.wallet.feature.login

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
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
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun user_exist_in_local_database() {
        val user_expect = UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")

        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Single.just(UserWallet(1, "ABC", "7C4A8D09CA3762AF61E59520943DC26494F8941B")))
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).doLogin(eq(user_expect))
    }

    @Test
    fun user_not_exist_in_local_database() {
        `when`(appDataBase.userDao()).thenReturn(userDao)
        `when`(appDataBase.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Single.error(Throwable("error")))
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).callUserNotExist()
    }

}