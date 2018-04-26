package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.BaseContract
import com.example.brunocolombini.wallet.DAO.user.Extract
import com.example.brunocolombini.wallet.util.enums.BalanceEventType


interface HomeContract {
    interface View {
        fun updateBalance(balanceType: BalanceEventType, balance: Double)
        fun updateUserInformation(extracts: List<Extract>)
    }

    interface Presenter : BaseContract.Presenter {
        fun updateUserInformation()
    }
}