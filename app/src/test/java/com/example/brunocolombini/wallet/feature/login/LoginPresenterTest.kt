package com.example.brunocolombini.wallet.feature.login

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import io.reactivex.schedulers.TestScheduler


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
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun user_exist_in_local_database() {
        `when`(presenter.db.userDao()).thenReturn(userDao)
        `when`(presenter.db.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Flowable.just(UserWallet(1, "ABC", "123")))
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).doLogin(ArgumentMatchers.any(UserWallet::class.java))
    }

    @Test
    fun user_not_exist_in_local_database() {
        `when`(presenter.db.userDao()).thenReturn(userDao)
        `when`(presenter.db.userDao().findByUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Flowable.just(UserWallet(null, "", "")))
        presenter.checkUserExist("ABC@ABC.com", "123456")
        verify(view, times(1)).callUserNotExist()
    }

}