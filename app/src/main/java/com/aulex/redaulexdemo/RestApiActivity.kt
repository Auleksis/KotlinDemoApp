package com.aulex.redaulexdemo

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.aulex.redaulexdemo.ui.theme.RedAulexDemoTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class RestApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedAulexDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RestApiScreen()
                }
            }
        }
    }
}

@Composable
fun RestApiScreen() {
    val mContext = LocalContext.current

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(mContext)
    }
    val coroutineScope = rememberCoroutineScope()

    var locationText by remember { mutableStateOf("Получение данных...") }

    var result by remember {
        mutableStateOf<String>("Получение данных...")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                coroutineScope.launch {
                    val location = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).await()
                    location?.let {
                        locationText = "Широта: ${it.latitude}, Долгота: ${it.longitude}"

                        result = getWeatherData(location.latitude, location.longitude, BuildConfig.OPENWEATHER_API_KEY)
                    } ?: run {
                        locationText = "Местоположение недоступно"
                    }
                }
            } else {
                locationText = "Доступ к геоданным запрещён"
            }
        }
    )

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                coroutineScope.launch {
                    val location = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    ).await()
                    location?.let {
                        locationText = "Широта: ${it.latitude}, Долгота: ${it.longitude}"

                        result = getWeatherData(location.latitude, location.longitude, BuildConfig.OPENWEATHER_API_KEY)
                    } ?: run {
                        locationText = "Местоположение недоступно"
                    }

                }
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Текущая позиция:", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = locationText)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Погода:", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = result)
    }
}

suspend fun Task<Location>.await(): Location? = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { cont.resume(it) }
    addOnFailureListener { cont.resume(null) }
}

suspend fun getWeatherData(lat: Double, lon: Double, apiKey: String): String = withContext(
    Dispatchers.IO) {
    try {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"
        val response = URL(url).readText()
        val json = JSONObject(response)

        val mainObject = json.getJSONObject("main")
        val windObject = json.getJSONObject("wind")

        val temp = mainObject.getDouble("temp")
        val feelsLike = mainObject.getDouble("feels_like")
        val windSpeed = windObject.getInt("speed")
        val windDirection = windObject.getInt("deg")

        "Температура $temp°C\nОщущается как $feelsLike°C\nСкорость ветра $windSpeed м/с\nНаправление ветра $windDirection° относительно севера"
    } catch (e: Exception) {
        "Ошибка при получении данных о погоде"
    }
}