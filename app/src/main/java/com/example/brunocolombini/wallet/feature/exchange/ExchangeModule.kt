package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Module
class ExchangeModule {

    @Provides
    fun provideUserApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    fun provideExchangeFragment(fragment: ExchangeFragment): ExchangeFragment = fragment

    @Provides
    fun provideExchangePresenter(changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>, api: Api): ExchangeContract.Presenter {
        return ExchangePresenter(changeEventDeliverySubject, api)
    }

}