package com.example.brunocolombini.wallet.DAO

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Singleton
class DAOModule {

    @Provides
    fun providesAppDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)!!

}