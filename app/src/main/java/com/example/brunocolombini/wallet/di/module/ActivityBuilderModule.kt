package com.example.brunocolombini.wallet.di.module

import br.com.projeto.pets.di.ActivityScoped
import com.example.brunocolombini.wallet.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun MainActivity():MainActivity

}