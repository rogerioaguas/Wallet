package com.example.brunocolombini.wallet.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@Singleton
class NetworkModule {

    @Provides
    fun provideRetrofit() = buildRetrofit()
            .baseUrl("https://olinda.bcb.gov.br")
            .build()


    private fun buildRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    }

    companion object {
        const val GENERAL = "GENERAL"
        const val LOGIN = "LOGIN"
    }
}
