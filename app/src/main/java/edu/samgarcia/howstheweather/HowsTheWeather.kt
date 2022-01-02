package edu.samgarcia.howstheweather

import android.app.Application
import edu.samgarcia.howstheweather.di.networkModule
import edu.samgarcia.howstheweather.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class HowsTheWeather: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.ERROR)
            androidContext(this@HowsTheWeather)
            modules(
                listOf(
                    networkModule,
                    viewModelModule
                )
            )
        }
    }
}