package edu.samgarcia.howstheweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.samgarcia.howstheweather.ui.screens.home.HomeScreen
import edu.samgarcia.howstheweather.ui.theme.HowsTheWeatherTheme

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