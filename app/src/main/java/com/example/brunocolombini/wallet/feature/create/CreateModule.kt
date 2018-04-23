package com.example.brunocolombini.wallet.feature.create

import com.example.brunocolombini.wallet.DAO.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class CreateModule {

    @Provides
    fun provideLoginActivity(activity: CreateActivity): CreateContract.View = activity

    @Provides
    fun providesLoginPresenter(activity: CreateActivity, appDatabase: AppDatabase):
            CreateContract.Presenter = CreatePresenter(activity, appDatabase)

}