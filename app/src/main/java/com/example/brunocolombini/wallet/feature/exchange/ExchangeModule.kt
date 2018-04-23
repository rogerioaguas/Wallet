package com.example.brunocolombini.wallet.feature.exchange

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.data.APIModule
import com.example.brunocolombini.wallet.data.Api
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Module(includes = arrayOf(APIModule::class))
class ExchangeModule {

    @Provides
    fun provideExchangeFragment(fragment: ExchangeFragment): ExchangeContract.View = fragment

    @Provides
    fun provideExchangePresenter(
            changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>,
            api: Api,
            userPreference: UserPreference,
            appDatabase: AppDatabase,
            exchangeFragment: ExchangeFragment): ExchangeContract.Presenter = ExchangePresenter(changeEventDeliverySubject, api, userPreference, appDatabase, exchangeFragment)

}