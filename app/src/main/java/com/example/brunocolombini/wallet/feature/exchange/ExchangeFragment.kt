package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.home.MarketType
import com.example.brunocolombini.wallet.util.TextWatcherCryptoInput
import com.example.brunocolombini.wallet.util.TransformToNumber.removeString
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.android.support.DaggerFragment
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.fragment_exchange.view.*
import org.intellij.lang.annotations.JdkConstants
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

        userInformation = arguments.getSerializable(USER_ARGS) as UserWallet
        marketType = arguments.getSerializable(MARKET_TYPE) as MarketType

        fragmentView.button_buy.setOnClickListener {
            val newBalanceFiat = removeString(fiat_balance.text.toString()) - removeString(buy_total.editText!!.text.toString())
            val newBalanceCrypto = removeString(crypto_balance.text.toString()) + removeString(buy_quantity.editText!!.text.toString())
            if (newBalanceFiat >= 0) {

                changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, newBalanceFiat))
                presenter.updateExtract(buy_total.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.FIAT.type), ExchangeEvent.SELL)
                when (marketType) {
                    MarketType.BTC -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BTC, newBalanceCrypto))
                        presenter.updateExtract(buy_quantity.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.BTC.type), ExchangeEvent.BUY)
                    }
                    MarketType.BRITAS -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BRITAS, newBalanceCrypto))
                        presenter.updateExtract(buy_quantity.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.BRITAS.type), ExchangeEvent.BUY)
                    }
                }
            } else {
                alertDontHaveBalance()
            }

        }

        fragmentView.button_sell.setOnClickListener {
            val newBalanceFiat = removeString(fiat_balance.text.toString()) + removeString(sell_total.editText!!.text.toString())
            val newBalanceCrypto = removeString(crypto_balance.text.toString()) - removeString(sell_quantity.editText!!.text.toString())
            if (newBalanceCrypto >= 0) {
                changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT, newBalanceFiat))
                presenter.updateExtract(sell_total.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.FIAT.type), ExchangeEvent.BUY)
                when (marketType) {
                    MarketType.BTC -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BTC, newBalanceCrypto))
                        presenter.updateExtract(sell_quantity.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.BTC.type), ExchangeEvent.SELL)
                    }
                    MarketType.BRITAS -> {
                        changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.BRITAS, newBalanceCrypto))
                        presenter.updateExtract(sell_quantity.editText!!.text.toString().toDouble(), resources.getString(BalanceEventType.BRITAS.type), ExchangeEvent.SELL)
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

    private fun alertDontHaveBalance() {
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