package edu.samgarcia.howstheweather.data.remote

import edu.samgarcia.howstheweather.domain.model.WeatherItem

interface WeatherApi {
    suspend fun getWeatherByCity(city: String): WeatherItem?
}