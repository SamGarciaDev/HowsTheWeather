package edu.samgarcia.howstheweather.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ForecastItem(
    val day: String,
    val temperature: String,
    val wind: String
)
