package com.example.brunocolombini.wallet.feature.login

import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginActivity(activity: LoginActivity): LoginContract.View = activity

    @Provides
    fun providesLoginPresenter(activity: LoginActivity):
            LoginContract.Presenter {
        return LoginPresenter(activity)
    }
}