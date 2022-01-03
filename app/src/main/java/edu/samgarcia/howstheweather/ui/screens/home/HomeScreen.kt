package edu.samgarcia.howstheweather.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import edu.samgarcia.howstheweather.domain.model.ForecastItem
import edu.samgarcia.howstheweather.domain.model.WeatherItem
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun HomeScreen(viewModel: HomeViewModel = get()) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = colors.primary
            ) {
                Row (
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🌄 How's The Weather?",
                        fontSize = typography.h6.fontSize,
                        color = colors.onPrimary
                    )
                }
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            AutoCompleteTextField(
                modifier = Modifier.fillMaxWidth(),
                viewModel = viewModel
            ) {
                OutlinedTextField(
                    value = viewModel.city,
                    onValueChange = { value ->
                        viewModel.onEvent(HomeEvent.OnCityChange(value))
                    },
                    placeholder = {
                        Text(text = "Search your city...")
                    },
                    label = {
                        Text(text = "City")
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                search(
                                    viewModel = viewModel,
                                    keyboardController = keyboardController
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            search(
                                viewModel = viewModel,
                                keyboardController = keyboardController
                            )
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                elevation = 10.dp,
                modifier = Modifier.fillMaxSize(),
            ) {
                viewModel.weather?.let { weather ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = viewModel.city,
                            fontSize = typography.h4.fontSize,
                            fontWeight = typography.h4.fontWeight,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        WeatherDisplay(weather = weather)
                    }
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun AutoCompleteTextField(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    textField: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        textField()

        DropdownMenu(
            expanded = viewModel.dropdownExpanded.value,
            onDismissRequest = { viewModel.onEvent(HomeEvent.OnDropdownDismissRequest) },
            properties = PopupProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                focusable = false,
                clippingEnabled = false
            )
        ) {
            viewModel.autoCompleteOptions.value.forEach { city ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.onEvent(HomeEvent.OnCityChange(city))
                    }
                ) {
                    Text(text = city)
                }
            }
        }
    }
}


@Composable
fun WeatherDisplay(weather: WeatherItem, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
    ) {
        val today = Calendar.getInstance()

        Text(
            text = SimpleDateFormat(
                "EEEE",
                Locale.getDefault()
            ).format(today.time).uppercase(),
            fontSize = 21.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = weather.temperature,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = weather.wind)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = weather.description,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Forecast",
            fontSize = typography.h6.fontSize
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    1.dp,
                    colors.primary,
                    shapes.medium
                )
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                today.add(Calendar.DATE, 1)

                Text(
                    text = SimpleDateFormat(
                        "EEE",
                        Locale.getDefault()
                    ).format(today.time).uppercase()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weather.forecast[0].temperature,
                    fontSize = 24.sp
                )
            }

            VerticalDivider(
                thickness = 1.dp,
                color = colors.primary,
                modifier = Modifier.height(75.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                today.add(Calendar.DATE, 1)

                Text(
                    text = SimpleDateFormat(
                        "EEE",
                        Locale.getDefault()
                    ).format(today.time).uppercase()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weather.forecast[1].temperature,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = colors.onSurface,
    thickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    val weather = WeatherItem(
        temperature = "30 °C",
        wind = "20 km/h",
        description = "Partly cloudy",
        forecast = listOf(
            ForecastItem(
                day = "1",
                temperature = "27 °C",
                wind = "12 km/h"
            ),
            ForecastItem(
                day = "2",
                temperature = "22 °C",
                wind = "8 km/h"
            )
        )
    )

    WeatherDisplay(
        weather,
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    )
}

@ExperimentalComposeUiApi
private fun search(
    viewModel: HomeViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    keyboardController?.hide()
    viewModel.weather = null
    viewModel.onEvent(HomeEvent.OnSearchClick)
}