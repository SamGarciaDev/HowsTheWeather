package edu.samgarcia.howstheweather.domain.model

class WeatherItem(
    val temperature: String = "",
    val feelsLike: String = "",
    val wind: String = "",
    val description: String = "",
    val area: String = "",
    val region: String = "",
    val forecast: List<ForecastItem>
) {
    companion object {
        @JvmStatic
        fun ofWeatherSerializable(serializable: WeatherSerializable): WeatherItem {
            return WeatherItem(
                temperature = serializable.current_condition.getOrNull(0)?.temp_C ?: "",
                feelsLike = serializable.current_condition.getOrNull(0)?.FeelsLikeC ?: "",
                wind = serializable.current_condition.getOrNull(0)?.windspeedKmph ?: "",
                description = serializable.current_condition.getOrNull(0)?.weatherDesc?.getOrNull(0)?.value ?: "",
                area = serializable.nearest_area.getOrNull(0)?.areaName?.getOrNull(0)?.value ?: "",
                region = serializable.nearest_area.getOrNull(0)?.region?.getOrNull(0)?.value ?: "",
                forecast = serializable.weather.subList(1, 3).filter {
                    it.avgtempC.isNotBlank() && it.date.isNotBlank()
                }.map {
                    ForecastItem(
                        date = it.date,
                        temperature = it.avgtempC
                    )
                }
            )
        }
    }
}
