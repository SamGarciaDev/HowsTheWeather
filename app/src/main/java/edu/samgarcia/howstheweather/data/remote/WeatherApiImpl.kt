package edu.samgarcia.howstheweather.data.remote

import edu.samgarcia.howstheweather.domain.model.WeatherItem
import edu.samgarcia.howstheweather.domain.model.WeatherSerializable
import edu.samgarcia.howstheweather.utils.Constants.WEATHER_API_URL
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class WeatherApiImpl: WeatherApi {
    override suspend fun getWeatherByCity(city: String): WeatherItem? {
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        val response: HttpResponse = client.request("$WEATHER_API_URL/$city?format=j1") {
            contentType(ContentType.Application.Json)
        }

        if (response.status != HttpStatusCode.OK) {
            return null
        }

        val weather: WeatherSerializable = response.receive()

        return WeatherItem.ofWeatherSerializable(weather)
    }
}