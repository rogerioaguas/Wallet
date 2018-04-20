package com.example.brunocolombini.wallet.di.module

import br.com.projeto.pets.di.FragmentScoped
import com.example.brunocolombini.wallet.feature.exchange.ExchangeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilderModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun ExchangeFragment(): ExchangeFragment
}
