package com.example.brunocolombini.wallet.feature.login

import com.example.brunocolombini.wallet.DAO.infra.UserPreference
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun provideLoginActivity(activity: LoginActivity): LoginContract.View = activity

    @Provides
    fun providesLoginPresenter(activity: LoginActivity,userPreference: UserPreference):
            LoginContract.Presenter {
        return LoginPresenter(activity,userPreference)
    }
}