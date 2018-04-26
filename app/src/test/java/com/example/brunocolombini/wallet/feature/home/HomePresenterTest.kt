package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.ExtractDao
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.nhaarman.mockito_kotlin.eq
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy


class HomePresenterTest : BaseTest() {

    @InjectMocks
    lateinit var presenter: HomePresenter

    @Spy
    lateinit var publishSubject: PublishSubject<UpdateBalanceEvent>

    @Mock
    lateinit var view: HomeContract.View

    @Mock
    lateinit var appDatabase: AppDatabase

    @Mock
    lateinit var userPreference: UserPreference

    @Mock
    lateinit var extraDao: ExtractDao

    @Test
    fun on_balance_is_updated_success() {
        val updateBalance = UpdateBalanceEvent(BalanceEventType.FIAT, 10.0)
        `when`(publishSubject
                .observeOn(AndroidSchedulers.mainThread())).thenReturn(Observable.just(updateBalance))
        presenter.onAttachView()
        verify(view, times(1)).updateBalance(eq(BalanceEventType.FIAT), eq(10.0))

    }

    @Test
    fun on_update_user_information_success() {
        val userId = 1L
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "FIAT"))
        allCoins.add(Extract(null, userId, 10.0, "BITCOIN"))
        allCoins.add(Extract(null, userId, 10.0, "BRITAS"))

        `when`(userPreference.getUserId()).thenReturn(1)
        `when`(appDatabase.extractDao()).thenReturn(extraDao)
        `when`(appDatabase.extractDao().getGroupExtractById(userPreference.getUserId())).thenReturn(Single.just(allCoins))
        presenter.updateUserInformation()
        verify(view).updateUserInformation(eq(allCoins))

    }


}