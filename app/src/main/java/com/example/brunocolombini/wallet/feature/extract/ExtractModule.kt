package com.example.brunocolombini.wallet.feature.extract

import com.example.brunocolombini.wallet.DAO.AppDatabase
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import dagger.Module
import dagger.Provides

@Module
class ExtractModule {


    @Provides
    fun providesExtractActivity(activity: ExtractActivity): ExtractContract.View = activity


    @Provides
    fun providesExtractPresenter(activity: ExtractActivity, userPreference: UserPreference, appDatabase: AppDatabase): ExtractContract.Presenter = ExtractPresenter(activity, userPreference, appDatabase)

}