package com.example.brunocolombini.wallet.feature.extract

import dagger.Module
import dagger.Provides

@Module
class ExtractModule {


    @Provides
    fun providesExtractActivity(): ExtractContract.View = ExtractActivity()


    @Provides
    fun providesExtractPresenter(activity: ExtractContract.View): ExtractContract.Presenter = ExtractPresenter(activity)

}