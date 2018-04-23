package com.example.brunocolombini.wallet.data

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class APIModule {
    @Provides
    fun provideUserApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

}