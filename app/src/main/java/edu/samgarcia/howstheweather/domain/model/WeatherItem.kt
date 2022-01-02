package edu.samgarcia.howstheweather.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherItem(
    val temperature: String,
    val wind: String,
    val description: String,
    val forecast: List<ForecastItem>
)