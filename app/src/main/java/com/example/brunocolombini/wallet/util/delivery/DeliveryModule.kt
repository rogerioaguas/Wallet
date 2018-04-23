package com.example.brunocolombini.wallet.util.delivery

import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

@Module
@Singleton
class DeliveryModule {

    @Provides
    @Singleton
    fun providesUpdateBalanceEvent(): PublishSubject<UpdateBalanceEvent> = PublishSubject.create()


}
