package com.example.brunocolombini.wallet.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Singleton
class InfraModule {

    @Provides
    fun provideUserPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
    }

    @Provides
    fun providesUserPreference(prefs: SharedPreferences): UserPreference = UserPreference(prefs)
}
