package edu.samgarcia.howstheweather.ui.screens.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.samgarcia.howstheweather.R
import edu.samgarcia.howstheweather.data.remote.WeatherApi
import edu.samgarcia.howstheweather.domain.model.WeatherItem
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class HomeViewModel(
    context: Application,
    private val weatherApi: WeatherApi
): AndroidViewModel(context) {

    var city = mutableStateOf(TextFieldValue())
        private set

    var weather by mutableStateOf<WeatherItem?>(null)

    private val cities = arrayListOf<String>()

    init {
        context.resources.openRawResource(R.raw.world_cities).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { br ->
                var line: String? = br.readLine()

                while (line != null) {
                    cities.add(line.split(";")[0])
                    line = br.readLine()
                }
            }
        }
    }

    val autoCompleteOptions = mutableStateOf(listOf<String>())
    val dropdownExpanded = mutableStateOf(false)

    val isLoading = mutableStateOf(false)

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.OnCityChange -> {
                city.value = event.city
                dropdownExpanded.value = true

                if (event.city.text.isNotBlank()) {
                    autoCompleteOptions.value = cities
                        .filter { it.lowercase().startsWith(event.city.text.lowercase()) && it != event.city.text }
                        .take(3)
                } else {
                    dropdownExpanded.value = false
                    autoCompleteOptions.value = emptyList()
                }
            }
            is HomeEvent.OnSearchClick -> {
                if (city.value.text.isBlank()) return

                viewModelScope.launch {
                    isLoading.value = true
                    weather = weatherApi.getWeatherByCity(city.value.text)
                    isLoading.value = false
                }
            }
            is HomeEvent.OnDropdownDismissRequest -> {
                dropdownExpanded.value = false
            }
        }
    }
}