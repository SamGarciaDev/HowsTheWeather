package edu.samgarcia.howstheweather.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.samgarcia.howstheweather.data.remote.WeatherApi
import edu.samgarcia.howstheweather.domain.model.WeatherItem
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherApi: WeatherApi
): ViewModel() {
    var city by mutableStateOf("")
        private set

    var weather by mutableStateOf<WeatherItem?>(null)

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OnCityChange -> {
                city = event.city
            }
            is HomeEvent.OnSearchClick -> {
                if (city.isBlank()) return

                viewModelScope.launch {
                    weather = weatherApi.getWeatherByCity(city)
                }
            }
        }
    }
}