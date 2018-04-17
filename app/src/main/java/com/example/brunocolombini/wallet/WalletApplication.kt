package com.example.brunocolombini.wallet

import com.example.brunocolombini.wallet.di.component.ApplicationComponent
import com.example.brunocolombini.wallet.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class WalletApplication : DaggerApplication() {


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()

        applicationComponent.inject(this)

        return applicationComponent
    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}
