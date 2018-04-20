package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.R
import com.example.brunocolombini.wallet.feature.home.ExchangeTabType
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_exchange, container, false)
        presenter.onAttachView(this)
        fragmentView.button_buy.setOnClickListener {
            changeEventDeliverySubject.onNext(UpdateBalanceEvent(BalanceEventType.FIAT,10.0))
        }

        return fragmentView

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


    companion object {
        private const val TYPE_ARGS = "TYPE"
        private const val USER_ARGS = "USER_ARGS"

        fun newInstance(type: ExchangeTabType, user: UserWallet? = null): ExchangeFragment {
            val fragment = ExchangeFragment()

            fragment.arguments = Bundle().apply {
                putSerializable(TYPE_ARGS, type)
                putSerializable(USER_ARGS, user)
            }

            return fragment
        }

    }

}