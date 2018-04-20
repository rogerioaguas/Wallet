package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Singleton
@Module
class ExchangeModule {

    @Provides
    fun provideExchangeFragment(fragment: ExchangeFragment): ExchangeFragment = fragment

    @Provides
    fun provideExchangePresenter(changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>): ExchangeContract.Presenter {
        return ExchangePresenter(changeEventDeliverySubject)
    }

}