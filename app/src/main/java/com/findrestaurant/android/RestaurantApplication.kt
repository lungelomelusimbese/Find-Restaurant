package com.findrestaurant.android

import android.app.Application
import com.findrestaurant.android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RestaurantApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RestaurantApplication)
            modules(appModule)
        }
    }
}
