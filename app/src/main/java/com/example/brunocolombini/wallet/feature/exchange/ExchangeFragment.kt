package com.example.brunocolombini.wallet.feature.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brunocolombini.wallet.R
import dagger.android.support.DaggerFragment

class ExchangeFragment: DaggerFragment(), ExchangeContract.View{

    private lateinit var fragmentView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_exchange, container, false)
        return fragmentView

    }


}