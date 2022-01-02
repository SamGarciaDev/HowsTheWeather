package edu.samgarcia.howstheweather.di

import edu.samgarcia.howstheweather.data.remote.WeatherApi
import edu.samgarcia.howstheweather.data.remote.WeatherApiImpl
import org.koin.dsl.module

val networkModule = module {
    single<WeatherApi> { WeatherApiImpl() }
}