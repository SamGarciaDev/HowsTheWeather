package edu.samgarcia.howstheweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import edu.samgarcia.howstheweather.ui.screens.home.HomeScreen
import edu.samgarcia.howstheweather.ui.theme.HowsTheWeatherTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HowsTheWeatherTheme {
                HomeScreen()
            }
        }
    }
}