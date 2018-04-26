package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.util.TextWatcherCryptoInput
import com.example.brunocolombini.wallet.util.TransformToNumber.removeString
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import com.example.brunocolombini.wallet.util.enums.BalanceEventType
import com.example.brunocolombini.wallet.util.enums.ExchangeEvent
import dagger.android.support.DaggerFragment
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.fragment_exchange.view.*
import javax.inject.Inject

class ExchangeFragment : DaggerFragment(), ExchangeContract.View {


    @Inject
    lateinit var presenter: ExchangeContract.Presenter

    private lateinit var fragmentView: View

    lateinit var userInformation: UserWallet

    lateinit var marketType: BalanceEventType

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_exchange, container, false)

        userInformation = arguments.getSerializable(USER_ARGS) as UserWallet
        marketType = arguments.getSerializable(MARKET_TYPE) as BalanceEventType

        presenter.onAttachView()
        presenter.updateBalance()
        presenter.setCoinPrice(marketType)
        setButtonsListeners()

        return fragmentView
    }

    private fun setButtonsListeners() {
        fragmentView.button_buy.setOnClickListener {
            val buyTotal = removeString(buy_total.editText!!.text.toString())
            val buyQuantity = removeString(buy_quantity.editText!!.text.toString())
            val cryptoBalance = removeString(crypto_balance.text.toString())
            val fiatBalance = removeString(fiat_balance.text.toString())

            val newBalanceFiat = fiatBalance - buyTotal
            val newBalanceCrypto = cryptoBalance + buyQuantity

            presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.BUY, marketType, newBalanceFiat, newBalanceCrypto, buyTotal, buyQuantity)

        }

        fragmentView.button_sell.setOnClickListener {
            val sellTotal = removeString(sell_total.editText!!.text.toString())
            val sellQuantity = removeString(sell_quantity.editText!!.text.toString())
            val cryptoBalance = removeString(crypto_balance.text.toString())
            val fiatBalance = removeString(fiat_balance.text.toString())

            val newBalanceFiat = fiatBalance + sellTotal
            val newBalanceCrypto = cryptoBalance - sellQuantity

            presenter.updateBalanceAfterExchangeEvent(ExchangeEvent.SELL, marketType, newBalanceFiat, newBalanceCrypto, sellTotal, sellQuantity)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onDestroy()
    }

    override fun alertNotHaveBalance() {
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
                if (marketType == BalanceEventType.BTC) {
                    fragmentView.crypto_balance.text = String.format(resources.getString(R.string.balance_btc), value)
                }
            }
            BalanceEventType.BRITAS -> {
                if (marketType == BalanceEventType.BRITAS) {
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