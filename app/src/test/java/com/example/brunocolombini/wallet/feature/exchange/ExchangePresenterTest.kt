package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.BaseTest
import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.DAO.user.ExtractDao
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.data.*
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import java.util.*
import kotlin.collections.ArrayList

class ExchangePresenterTest : BaseTest() {

    @InjectMocks
    lateinit var presenter: ExchangePresenter
    @Mock
    lateinit var publishSubject: PublishSubject<UpdateBalanceEvent>
    @Mock
    lateinit var api: Api
    @Mock
    lateinit var userPreference: UserPreference
    @Mock
    lateinit var appDatabase: AppDatabase
    @Mock
    lateinit var view: ExchangeContract.View
    @Mock
    lateinit var extractDAO: ExtractDao

    val userId = 1L

    @Before
    override fun setup() {
        super.setup()

        `when`(userPreference.getUserId()).thenReturn(userId)

        `when`(appDatabase.extractDao()).thenReturn(extractDAO)


        `when`(view.getStringByResourceId(R.string.fiat)).thenReturn("FIAT")
        `when`(view.getStringByResourceId(R.string.btc)).thenReturn("BITCOIN")
        `when`(view.getStringByResourceId(R.string.bts)).thenReturn("BRITAS")
    }

    @Test
    fun on_attach_view_test() {
        val updateBalance = UpdateBalanceEvent(BalanceEventType.FIAT, 10.0)
        Mockito.`when`(publishSubject
                .observeOn(AndroidSchedulers.mainThread())).thenReturn(Observable.just(updateBalance))
        presenter.onAttachView()
        Mockito.verify(view, Mockito.times(1)).updateBalance(eq(BalanceEventType.FIAT), eq(10.0))
    }


    @Test
    fun get_britas_price_test_success() {
        val bancoCentralModel = BancoCentralModel(listBritasPrice())
        `when`(api.getBritasPrice()).thenReturn(Single.just(bancoCentralModel))
        presenter.setCoinPrice(BalanceEventType.BRITAS)
        verify(view, times(1)).setCryptoPrice(eq(BalanceEventType.BRITAS), eq(bancoCentralModel.value[0].cotacaoCompra), eq(bancoCentralModel.value[0].cotacaoVenda))
    }

    @Test
    fun get_btc_price_test_success() {
        val tickerMercadoBitcoin = MercadoBitcoinModel(Ticker("10.0", "5.0", "2.0", "1.0", "3.0", "8.0"))
        `when`(api.getBtcPrice(ArgumentMatchers.anyString())).thenReturn(Single.just(tickerMercadoBitcoin))
        presenter.setCoinPrice(BalanceEventType.BTC)
        verify(view, times(1)).setCryptoPrice(eq(BalanceEventType.BTC), eq(tickerMercadoBitcoin.ticker.buy.toDouble()), eq(tickerMercadoBitcoin.ticker.sell.toDouble()))

    }

    private fun listBritasPrice(): List<FiatPrice> {
        val listBritas: ArrayList<FiatPrice> = ArrayList()
        listBritas.add(FiatPrice(1.0, 5.0, 8.0, 12.0, Date().toString(), "Abertura"))
        listBritas.add(FiatPrice(2.0, 6.0, 9.0, 13.0, Date().toString(), "Fechado"))
        listBritas.add(FiatPrice(3.0, 7.0, 10.0, 14.0, Date().toString(), "Pre Mercado"))
        listBritas.add(FiatPrice(4.0, 8.0, 11.0, 15.0, Date().toString(), "Pos Mercado"))
        return listBritas
    }


    @Test
    fun update_balance_all_coins_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "FIAT"))
        allCoins.add(Extract(null, userId, 10.0, "BITCOIN"))
        allCoins.add(Extract(null, userId, 10.0, "BRITAS"))
        `when`(appDatabase.extractDao().getGroupExtractById(userId)).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(3)).updateBalance(any(), ArgumentMatchers.anyDouble())
    }

    @Test
    fun update_balance_only_fiat_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "FIAT"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(1)).updateBalance(eq(BalanceEventType.FIAT), eq(allCoins[0].amount))
    }

    @Test
    fun update_balance_only_bitcoin_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "BITCOIN"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(1)).updateBalance(eq(BalanceEventType.BTC), eq(allCoins[0].amount))
    }

    @Test
    fun update_balance_only_britas_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, 1, 10.0, "BRITAS"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(1)).updateBalance(eq(BalanceEventType.BRITAS), eq(allCoins[0].amount))
    }

    @Test
    fun update_balance_fiat_bitcoin_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "FIAT"))
        allCoins.add(Extract(null, userId, 10.0, "BITCOIN"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(2)).updateBalance(any(), ArgumentMatchers.anyDouble())
    }

    @Test
    fun update_balance_fiat_britas_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "FIAT"))
        allCoins.add(Extract(null, userId, 10.0, "BRITAS"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(2)).updateBalance(any(), ArgumentMatchers.anyDouble())
    }

    @Test
    fun update_balance_bitcoin_britas_success() {
        val allCoins: ArrayList<Extract> = ArrayList()
        allCoins.add(Extract(null, userId, 10.0, "BITCOIN"))
        allCoins.add(Extract(null, userId, 10.0, "BRITAS"))
        `when`(appDatabase.extractDao().getGroupExtractById(eq(userId))).thenReturn(Single.just(allCoins))
        presenter.updateBalance()
        verify(appDatabase.extractDao(), times(1)).getGroupExtractById(eq(userId))
        verify(view, times(2)).updateBalance(any(), ArgumentMatchers.anyDouble())
    }

    @Test
    fun update_balance_after_exchange_event_error_fiat_balance() {
        presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, BalanceEventType.BTC, -1.0, 10.0, 1000.0, 10.0)
        verify(view, times(1)).alertNotHaveBalance()
    }

    @Test
    fun update_balance_after_exchange_event_error_crypto_balance() {
        presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, BalanceEventType.BTC, 10.0, -1.0, 1000.0, 10.0)
        verify(view, times(1)).alertNotHaveBalance()
    }

    @Test
    fun update_balance_after_exchange_event_error_both_balance() {
        presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, BalanceEventType.BTC, -1.0, -1.0, 1000.0, 10.0)
        verify(view, times(1)).alertNotHaveBalance()
    }

    @Test
    fun update_balance_after_exchange_event_buy() {
        presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, BalanceEventType.BTC, 10.0, 10.0, 1000.0, 10.0)

        val buyExtract = Extract(null, userPreference.getUserId(), 1000.0 * -1, view.getStringByResourceId(BalanceEventType.FIAT.type))
        val sellExtract = Extract(null, userPreference.getUserId(), 10.0 * 1, view.getStringByResourceId(BalanceEventType.BTC.type))

        verify(appDatabase.extractDao(), times(1)).insertAll(eq(buyExtract),eq(sellExtract))
        verify(view, times(1)).extractUpdateWithSuccess()
    }

    @Test
    fun update_extract_sell_success() {
        presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.SELL, BalanceEventType.BTC, 10.0, 10.0, 1000.0, 10.0)

        val buyExtract = Extract(null, userPreference.getUserId(), 1000.0 * 1, view.getStringByResourceId(BalanceEventType.FIAT.type))
        val sellExtract = Extract(null, userPreference.getUserId(), 10.0 * -1, view.getStringByResourceId(BalanceEventType.BTC.type))

        verify(appDatabase.extractDao(), times(1)).insertAll(eq(buyExtract),eq(sellExtract))
        verify(view, times(1)).extractUpdateWithSuccess()
    }

}