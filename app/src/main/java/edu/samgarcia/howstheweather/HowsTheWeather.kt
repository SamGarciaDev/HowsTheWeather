package edu.samgarcia.howstheweather

import android.app.Application
import edu.samgarcia.howstheweather.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HowsTheWeather: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HowsTheWeather)
            modules(networkModule)
        }
    }
}