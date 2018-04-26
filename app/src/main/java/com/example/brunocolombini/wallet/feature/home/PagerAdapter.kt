package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.brunocolombini.wallet.DAO.user.UserWallet
import com.example.brunocolombini.wallet.feature.exchange.ExchangeFragment
import com.example.brunocolombini.wallet.util.enums.BalanceEventType

class PagerAdapter(
        private val context: Context,
        private val user: UserWallet,
        fragment: FragmentManager
) : FragmentStatePagerAdapter(fragment) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ExchangeFragment.newInstance(type = BalanceEventType.BTC, user = user)
            else -> ExchangeFragment.newInstance(type = BalanceEventType.BRITAS, user = user)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        val title = when (position) {
            0 -> BalanceEventType.BTC.type
            else -> BalanceEventType.BRITAS.type
        }
        return context.getString(title)
    }

    override fun getCount() = BalanceEventType.values().size - 1
}