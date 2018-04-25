package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.feature.home.MarketType
import com.example.brunocolombini.wallet.util.TextWatcherCryptoInput
import com.example.brunocolombini.wallet.util.TransformToNumber.removeString
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent
import dagger.android.support.DaggerFragment
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.fragment_exchange.view.*
import javax.inject.Inject

class ExchangeFragment : DaggerFragment(), ExchangeContract.View {


    @Inject
    lateinit var presenter: ExchangeContract.Presenter

    @Inject
    lateinit var changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>

    private lateinit var fragmentView: View

    lateinit var userInformation: UserWallet

    lateinit var marketType: BalanceEventType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_exchange, container, false)
        presenter.onAttachView()

        userInformation = arguments.getSerializable(USER_ARGS) as UserWallet
        marketType = arguments.getSerializable(MARKET_TYPE) as BalanceEventType

        fragmentView.button_buy.setOnClickListener {
            val buyTotal = removeString(buy_total.editText!!.text.toString())
            val buyQuantity = removeString(buy_quantity.editText!!.text.toString())
            val cryptoBalance = removeString(crypto_balance.text.toString())
            val fiatBalance = removeString(fiat_balance.text.toString())

            val newBalanceFiat = fiatBalance - buyTotal
            val newBalanceCrypto = cryptoBalance + buyQuantity

            presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, marketType, newBalanceFiat, newBalanceCrypto,buyTotal,buyQuantity)


        }

        fragmentView.button_sell.setOnClickListener {
            val sellTotal = removeString(sell_total.editText!!.text.toString())
            val sellQuantity = removeString(sell_quantity.editText!!.text.toString())
            val cryptoBalance = removeString(crypto_balance.text.toString())
            val fiatBalance = removeString(fiat_balance.text.toString())

            val newBalanceFiat = fiatBalance + sellTotal
            val newBalanceCrypto = cryptoBalance - sellQuantity

            if (newBalanceCrypto >= 0) {
                changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, newBalanceFiat))
                presenter.updateExtract(sellTotal, resources.getString(BalanceEventType.FIAT.type), ExchangeEvent.BUY)
                when (marketType) {
                    MarketType.BTC -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BTC, newBalanceCrypto))
                        presenter.updateExtract(sellQuantity, resources.getString(BalanceEventType.BTC.type), ExchangeEvent.SELL)
                    }
                    MarketType.BRITAS -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BRITAS, newBalanceCrypto))
                        presenter.updateExtract(sellQuantity, resources.getString(BalanceEventType.BRITAS.type), ExchangeEvent.SELL)
                    }
                }
            } else {
                alertDontHaveBalance()
            }
        }

        presenter.updateBalance()
        when (marketType) {
            MarketType.BTC -> presenter.getBtcPrice()
            MarketType.BRITAS -> presenter.getBritasPrice()
        }
        return fragmentView
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    override fun alertDontHaveBalance() {
        val snackbar = Snackbar.make(fragmentView.button_buy, "Saldo insuficiente", Snackbar.LENGTH_SHORT)
        snackbar.setAction("Entendi", { snackbar.dismiss() })
        snackbar.show()
    }

    private fun textInputListeners() {

        val buyQuantity = fragmentView.buy_quantity.editText!!
        val buyPrice = fragmentView.buy_price.editText!!.text.toString().toDouble()
        val buyTotal = fragmentView.buy_total.editText!!

        val sellQuantity = fragmentView.sell_quantity.editText!!
        val sellPrice = fragmentView.sell_price.editText!!.text.toString().toDouble()
        val sellTotal = fragmentView.sell_total.editText!!


        buyQuantity.addTextChangedListener(TextWatcherCryptoInput(buyQuantity, buyPrice, buyTotal, button_buy))
        sellQuantity.addTextChangedListener(TextWatcherCryptoInput(sellQuantity, sellPrice, sellTotal, button_sell))
    }

    override fun updateBalance(balanceType: BalanceEventType, value: Double) {
        when (balanceType) {
            BalanceEventType.FIAT -> {
                fragmentView.fiat_balance.text = String.format(resources.getString(R.string.balance_fiat), value)
            }
            BalanceEventType.BTC -> {
                if (marketType == MarketType.BTC) {
                    fragmentView.crypto_balance.text = String.format(resources.getString(R.string.balance_btc), value)
                }
            }
            BalanceEventType.BRITAS -> {
                if (marketType == MarketType.BRITAS) {
                    fragmentView.crypto_balance.text = String.format(resources.getString(R.string.balance_britas), value)
                }
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

    override fun extractUpdateWithSuccess() {
        Snackbar
                .make(fragmentView.button_buy, "Transação feita com sucesso", Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun getStringByResourceId(resourceId: Int): String = getString(resourceId)

    companion object {
        private const val MARKET_TYPE = "MARKET_TYPE"
        private const val USER_ARGS = "USER_ARGS"

        fun newInstance(type: BalanceEventType, user: UserWallet): ExchangeFragment {
            val fragment = ExchangeFragment()

            fragment.arguments = Bundle().apply {
                putSerializable(MARKET_TYPE, type)
                putSerializable(USER_ARGS, user)
            }

            return fragment
        }

    }

}