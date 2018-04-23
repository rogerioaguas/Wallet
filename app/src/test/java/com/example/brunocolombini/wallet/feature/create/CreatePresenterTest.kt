package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserDao
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class CreatePresenterTest {

    @InjectMocks
    lateinit var presenter: CreatePresenter

    @Mock
    lateinit var view: CreateContract.View

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
    fun save_user_ok() {
        Mockito.`when`(appDataBase.userDao()).thenReturn(userDao)
        presenter.saveUser("ABC", "123456")
        verify(view, times(1)).success()
    }

    @Test
    fun save_user_error() {
        presenter.saveUser("", "")
        verify(view, Mockito.times(1)).errorAlert()

    }
}