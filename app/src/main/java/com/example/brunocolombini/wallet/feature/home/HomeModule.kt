package com.example.brunocolombini.wallet.feature.home

import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import com.example.brunocolombini.wallet.util.delivery.UpdateBalanceEvent
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module
class HomeModule {

    @Provides
    fun providesHomeActivity(activity: HomeActivity): HomeContract.View = activity

    @Provides
    fun providesLoginPresenter(activity: HomeActivity, userPreference: UserPreference, changeEventDeliverySubject: PublishSubject<UpdateBalanceEvent>):
            HomeContract.Presenter {
        return HomePresenter(activity, userPreference, changeEventDeliverySubject)
    }
}