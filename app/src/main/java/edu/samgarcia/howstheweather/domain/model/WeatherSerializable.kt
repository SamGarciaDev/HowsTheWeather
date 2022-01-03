package edu.samgarcia.howstheweather.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherSerializable(
    val current_condition: List<current_condition>,
    val nearest_area: List<nearest_area>,
    val weather: List<weather>
)

@Serializable
data class current_condition(
    val FeelsLikeC: String,
    val temp_C: String,
    val weatherDesc: List<weatherDesc>,
    val windspeedKmph: String,
)

@Serializable
data class weatherDesc(
    val value: String
)

@Serializable
data class nearest_area(
    val areaName: List<areaName>,
    val region: List<region>,
)

@Serializable
data class areaName(
    val value: String
)

@Serializable
data class region(
    val value: String
)

@Serializable
data class weather(
    val avgtempC: String,
    val date: String
)