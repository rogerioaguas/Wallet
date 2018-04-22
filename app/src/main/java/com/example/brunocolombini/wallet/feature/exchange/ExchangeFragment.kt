package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.home.MarketType
import com.example.brunocolombini.wallet.util.TextWatcherCryptoInput
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.android.support.DaggerFragment
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_exchange.view.*
import javax.inject.Inject

class ExchangeFragment : DaggerFragment(), ExchangeContract.View {

    @Inject
    lateinit var presenter: ExchangeContract.Presenter

    @Inject
    lateinit var changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>

    private lateinit var fragmentView: View

    lateinit var userInformation: UserWallet

    lateinit var marketType: MarketType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_exchange, container, false)
        presenter.onAttachView(this)

        fragmentView.button_buy.setOnClickListener {
            changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, 10.0))
        }

        fragmentView.button_sell.setOnClickListener {
            changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, 10.0))
        }


        userInformation = arguments.getSerializable(USER_ARGS) as UserWallet
        marketType = arguments.getSerializable(MARKET_TYPE) as MarketType

        presenter.updateBalance()
        when (marketType) {
            MarketType.BTC -> presenter.getBtcPrice()
            MarketType.BRITAS -> presenter.getBritasPrice()
        }
        return fragmentView

    }


    private fun textInputListeners() {

        val buyQuantity = fragmentView.buy_quantity.editText!!
        val buyPrice = fragmentView.buy_price.editText!!.text.toString().toDouble()
        val buyTotal = fragmentView.buy_total.editText!!

        val sellQuantity = fragmentView.sell_quantity.editText!!
        val sellPrice = fragmentView.sell_price.editText!!.text.toString().toDouble()
        val sellTotal = fragmentView.sell_total.editText!!

        buyQuantity.addTextChangedListener(TextWatcherCryptoInput(buyQuantity, buyPrice, buyTotal))
        sellQuantity.addTextChangedListener(TextWatcherCryptoInput(sellQuantity, sellPrice, sellTotal))
    }

    override fun updateBalance(balanceType: BalanceEventType, value: Double) {
        when (balanceType) {
            BalanceEventType.FIAT -> {
                fragmentView.fiat_balance.text = String.format(resources.getString(R.string.balance_fiat), value)
            }
            BalanceEventType.BTC -> {
                fragmentView.crypto_balance.text = String.format(resources.getString(R.string.balance_btc), value)
            }
            BalanceEventType.BRITAS -> {
                fragmentView.crypto_balance.text = String.format(resources.getString(R.string.balance_britas), value)
            }
        }
    }

    override fun setCryptoPrice(market: BalanceEventType, bid: Double, ask: Double) {
        if (market.type == marketType.type) {
            fragmentView.buy_price.editText?.setText(String.format("%.8f", ask))
            fragmentView.sell_price.editText?.setText(String.format("%.8f", bid))
            textInputListeners()
        }
    }


    companion object {
        private const val MARKET_TYPE = "MARKET_TYPE"
        private const val USER_ARGS = "USER_ARGS"

        fun newInstance(type: MarketType, user: UserWallet): ExchangeFragment {
            val fragment = ExchangeFragment()

            fragment.arguments = Bundle().apply {
                putSerializable(MARKET_TYPE, type)
                putSerializable(USER_ARGS, user)
            }

            return fragment
        }

    }

}