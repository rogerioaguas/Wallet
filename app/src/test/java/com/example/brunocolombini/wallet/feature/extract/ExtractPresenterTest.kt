package com.example.brunocolombini.wallet.feature.extract

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.ExtractDao
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import com.nhaarman.mockito_kotlin.eq

class ExtractPresenterTest : BaseTest() {

    @InjectMocks
    lateinit var presenter: ExtractPresenter

    @Mock
    lateinit var view: ExtractContract.View

    @Mock
    lateinit var userPreference: UserPreference

    @Mock
    lateinit var appDatabase: AppDatabase

    @Mock
    lateinit var extractDAO: ExtractDao


    @Test
    fun getAllExtracts() {
        `when`(appDatabase.extractDao()).thenReturn(extractDAO)
        `when`(appDatabase.extractDao().getExtractById(ArgumentMatchers.anyLong())).thenReturn(Single.just(extractAll()))
        presenter.getAllExtracts()
        verify(view, times(1)).setRecycleView(eq(extractAll()))
    }

    private fun extractAll(): List<Extract> {
        val extracts: ArrayList<Extract> = ArrayList()
        extracts.add(Extract(1, 1, 1000.0, "BITCOIN", "26/02/2018"))
        extracts.add(Extract(1, 1, 1000.0, "BRITAS", "26/02/2018"))
        extracts.add(Extract(1, 1, 1000.0, "FIAT", "30/03/2018"))
        extracts.add(Extract(1, 1, 1000.0, "BITCOIN", "26/02/2018"))
        return extracts
    }

}