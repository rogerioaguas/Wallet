package com.example.brunocolombini.wallet.di.module

import br.com.projeto.pets.di.ActivityScoped
import com.example.brunocolombini.wallet.MainActivity
import com.example.brunocolombini.wallet.feature.login.LoginActivity
import com.example.brunocolombini.wallet.feature.login.LoginModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun MainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
    internal abstract fun LoginActivity(): LoginActivity

}