package com.example.brunocolombini.wallet.feature.extract

import android.content.Context
import com.example.brunocolombini.wallet.DAO.user.Extract

interface ExtractContract {
    interface View {
        fun setRecycleView(extracts: List<Extract>)
    }

    interface Presenter {
        fun getAllExtracts()
    }
}