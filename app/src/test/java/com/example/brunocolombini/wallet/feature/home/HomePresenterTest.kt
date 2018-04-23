package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*
import com.nhaarman.mockito_kotlin.eq
import java.util.concurrent.TimeUnit


class HomePresenterTest {

    @InjectMocks
    lateinit var presenter: HomePresenter

    @Spy
    lateinit var publishSubject: PublishSubject<UpdateBalanceEvent>

    @Mock
    lateinit var view: HomeContract.View

    @Mock
    lateinit var appDataBase: AppDatabase

    @Mock
    lateinit var userPreference: UserPreference

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun on_balance_is_updated_success() {
        val updateBalance = UpdateBalanceEvent(BalanceEventType.FIAT, 10.0)
        `when`(publishSubject
                .observeOn(AndroidSchedulers.mainThread())).thenReturn(Observable.just(updateBalance))
        presenter.onAttachView()
        verify(view, times(1)).updateBalance(eq(BalanceEventType.FIAT), eq(10.0))

    }

}