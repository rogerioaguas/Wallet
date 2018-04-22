package com.example.brunocolombini.wallet.feature.home

import android.content.Context
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.util.delivery.BalanceEventType


interface HomeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, balance: Double)
        fun updateUserInformation(extracts: List<Extract>)
    }

    interface Presenter {
        fun onAttachView(context: Context)
        fun onDestroy()
        fun updateUserInformation()
    }
}