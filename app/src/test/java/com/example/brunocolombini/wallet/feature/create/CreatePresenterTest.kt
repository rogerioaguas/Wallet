package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.user.UserDao
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.util.HashUtils
import io.reactivex.Completable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

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
        presenter.db = appDataBase
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun save_user_ok() {
        Mockito.`when`(presenter.db.userDao()).thenReturn(userDao)
        presenter.saveUser("ABC", "123456")
        verify(view, times(1)).success()
    }

    @Test
    fun save_user_error() {
        presenter.saveUser("", "")
        verify(view, Mockito.times(1)).errorAlert()

    }

}