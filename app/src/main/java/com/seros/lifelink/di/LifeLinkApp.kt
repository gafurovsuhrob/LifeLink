package com.seros.lifelink.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class LifeLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LifeLinkApp)
            modules(appModule)
        }
    }
}