package com.example.brunocolombini.wallet.feature.create

import dagger.Module
import dagger.Provides

@Module
class CreateModule {

    @Provides
    fun provideLoginActivity(activity: CreateActivity): CreateContract.View = activity

    @Provides
    fun providesLoginPresenter(activity: CreateActivity):
            CreateContract.Presenter {
        return CreatePresenter(activity)
    }
}