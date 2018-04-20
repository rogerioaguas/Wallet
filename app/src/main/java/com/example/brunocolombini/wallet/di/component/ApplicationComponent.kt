package com.example.brunocolombini.wallet.di.component

import android.app.Application
import com.example.brunocolombini.wallet.WalletApplication
import com.example.brunocolombini.wallet.di.module.ActivityBuilderModule
import com.example.brunocolombini.wallet.di.module.ApplicationModule
import com.example.brunocolombini.wallet.util.delivery.DeliveryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        DeliveryModule::class,
        ActivityBuilderModule::class))
interface ApplicationComponent : AndroidInjector<WalletApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}
