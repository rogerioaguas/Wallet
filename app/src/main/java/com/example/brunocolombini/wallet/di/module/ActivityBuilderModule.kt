package com.example.brunocolombini.wallet.di.module

import br.com.projeto.pets.di.ActivityScoped
import com.example.brunocolombini.wallet.feature.signin.HomeActivity
import com.example.brunocolombini.wallet.feature.create.CreateActivity
import com.example.brunocolombini.wallet.feature.create.CreateModule
import com.example.brunocolombini.wallet.feature.login.LoginActivity
import com.example.brunocolombini.wallet.feature.login.LoginModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun MainActivity(): HomeActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    internal abstract fun LoginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(CreateModule::class))
    internal abstract fun CreateActivity(): CreateActivity

}