package edu.samgarcia.howstheweather.data.remote

import edu.samgarcia.howstheweather.domain.model.WeatherItem
import edu.samgarcia.howstheweather.utils.Constants.WEATHER_API_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WeatherApiImpl: WeatherApi {
    override suspend fun getWeatherByCity(city: String): WeatherItem? {
        HttpClient().use { client ->
            val response: HttpResponse = client.request(WEATHER_API_URL + city)

            if (response.status != HttpStatusCode.OK) {
                return null
            }

            return Json.decodeFromString<WeatherItem>(response.content.toString())
        }
    }
}