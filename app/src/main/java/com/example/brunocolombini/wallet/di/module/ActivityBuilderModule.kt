package com.example.brunocolombini.wallet.di.module

import br.com.projeto.pets.di.ActivityScoped
import com.example.brunocolombini.wallet.feature.create.CreateActivity
import com.example.brunocolombini.wallet.feature.create.CreateModule
import com.example.brunocolombini.wallet.feature.home.HomeActivity
import com.example.brunocolombini.wallet.feature.home.HomeModule
import com.example.brunocolombini.wallet.feature.login.LoginActivity
import com.example.brunocolombini.wallet.feature.login.LoginModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    internal abstract fun LoginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(CreateModule::class))
    internal abstract fun CreateActivity(): CreateActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class,FragmentBuilderModule::class))
    internal abstract fun HomeActivity(): HomeActivity

}