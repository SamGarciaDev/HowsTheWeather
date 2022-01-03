package edu.samgarcia.howstheweather.ui.screens.home

import androidx.compose.ui.text.input.TextFieldValue

sealed class HomeEvent {
    data class OnCityChange(val city: TextFieldValue): HomeEvent()
    object OnSearchClick: HomeEvent()
    object OnDropdownDismissRequest: HomeEvent()
}
