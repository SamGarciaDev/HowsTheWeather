package edu.samgarcia.howstheweather.ui.screens.home

sealed class HomeEvent {
    data class OnCityChange(val city: String): HomeEvent()
    object OnSearchClick: HomeEvent()
}
