package edu.samgarcia.howstheweather.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import edu.samgarcia.howstheweather.R
import edu.samgarcia.howstheweather.domain.model.WeatherItem
import edu.samgarcia.howstheweather.ui.theme.topAppBarBackground
import org.koin.androidx.compose.get
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun HomeScreen(viewModel: HomeViewModel = get()) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MyTopBar() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 8.dp)
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current

                AutoCompleteTextField(
                    modifier = Modifier.fillMaxWidth(),
                    viewModel = viewModel
                ) {
                    OutlinedTextField(
                        value = viewModel.city.value,
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

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    elevation = 8.dp,
                    modifier = Modifier.fillMaxSize(),
                    contentColor = Color.White
                ) {
                    val desc = viewModel.weather?.description?.lowercase()

                    @DrawableRes
                    val backgroundId = when {
                        desc == null -> R.drawable.background_default
                        desc.matches(Regex("^.*(sun|clear).*\$")) -> R.drawable.background_sunny
                        desc.matches(Regex("^.*(cloud|overcast).*\$")) -> R.drawable.background_cloudy
                        desc.matches(Regex("^.*(rain|shower|storm|mist|drizzle|haze).*\$")) -> R.drawable.background_rainy
                        desc.matches(Regex("^.*(snow|blizzard).*\$")) -> R.drawable.background_snowy
                        else -> R.drawable.background_default
                    }

                    Image(
                        painter = painterResource(id = backgroundId),
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop
                    )

                    viewModel.weather?.let { weather ->
                        WeatherDisplay(
                            weather = weather,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(32.dp)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                    
                    if (viewModel.noResult.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 64.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_result),
                                contentDescription = "No result",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.None
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "There is nothing to show. Please make a search.",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (viewModel.isLoading.value) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Composable
fun MyTopBar() {
    TopAppBar(
        backgroundColor = colors.topAppBarBackground,
        contentColor = Color.White
    ) {
        Row (
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🌥   How's The Weather?",
                fontSize = typography.h6.fontSize,
                color = colors.onPrimary
            )
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
                        viewModel.onEvent(
                            HomeEvent.OnCityChange(
                                TextFieldValue(
                                    text = city,
                                    selection = TextRange(city.length)
                                )
                            )
                        )
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
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = "${weather.area}, ${weather.region}",
            fontSize = typography.h4.fontSize,
            fontWeight = typography.h4.fontWeight,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            text = "${weather.temperature} °C",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${weather.wind} km/h")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = weather.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
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
                    width = 1.dp,
                    color = Color.White,
                    shape = shapes.medium
                )
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            weather.forecast.forEachIndexed { index, day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    val date = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(day.date)

                    Text(
                        text = SimpleDateFormat(
                            "EEE",
                            Locale.getDefault()
                        ).format(date!!).uppercase()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${day.temperature} °C",
                        fontSize = 24.sp
                    )
                }

                if (index < weather.forecast.size - 1) {
                    VerticalDivider(
                        thickness = 1.dp,
                        color = Color.White,
                        modifier = Modifier.height(75.dp)
                    )
                }
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

@ExperimentalComposeUiApi
private fun search(
    viewModel: HomeViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    keyboardController?.hide()
    viewModel.weather = null
    viewModel.onEvent(HomeEvent.OnSearchClick)
}