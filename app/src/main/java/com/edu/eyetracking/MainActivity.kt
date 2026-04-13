package com.edu.eyetracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.edu.eyetracking.ui.theme.EdgeEyeTrackingTheme
import android.app.Activity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EdgeEyeTrackingTheme {
                EdgeEyeTrackingApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun EdgeEyeTrackingApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val context = LocalContext.current

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onCloseApp = { (context as? Activity)?.finish() },
                    onNavigate = { currentDestination = it },
                    currentDestination = currentDestination
                )
                AppDestinations.MODULES -> ModulesScreen(
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestinations.CALIBRATION -> CalibrationScreen(
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestinations.TRACKING -> TrackingScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Start", R.drawable.ic_home),
    MODULES("Moduły", R.drawable.ic_favorite),
    CALIBRATION("Kalibracja", R.drawable.ic_account_box),
    TRACKING("Pomiary", R.drawable.ic_home),
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCloseApp: () -> Unit,
    onNavigate: (AppDestinations) -> Unit,
    currentDestination: AppDestinations
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Instrukcja aplikacji",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Witaj w Edge Eye Tracking. Aby rozpocząć, wybierz odpowiedni moduł śledzenia wzroku z dolnego menu lub użyj przycisków poniżej. Upewnij się, że Twoje urządzenie jest stabilne.",
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Przyciski nawigacji z ramką dla aktualnie wybranego (jeśli dotyczy)
        AppDestinations.entries.filter { it != AppDestinations.HOME }.forEach { destination ->
            val isSelected = currentDestination == destination
            Button(
                onClick = { onNavigate(destination) },
                modifier = Modifier.fillMaxWidth(),
                border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                colors = if (isSelected) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(destination.label)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Opcje */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Opcje")
        }

        Button(
            onClick = onCloseApp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zamknij aplikację")
        }
    }
}

@Composable
fun ModulesScreen(modifier: Modifier = Modifier) {
    val modules = listOf(
        "Moduł Standardowy",
        "Moduł Precyzyjny",
        "Moduł Szybkiego Śledzenia",
        "Moduł Niskiego Poboru Energii"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista modułów śledzenia wzroku",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn {
            items(modules) { moduleName ->
                ListItem(
                    headlineContent = { Text(moduleName) },
                    trailingContent = {
                        Button(onClick = { /* TODO: Wybierz moduł */ }) {
                            Text("Wybierz")
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CalibrationScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Moduł Kalibracji",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Podążaj wzrokiem za kropką na ekranie, aby skalibrować system.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Button(onClick = { /* TODO: Rozpocznij kalibrację */ }) {
            Text("Rozpocznij kalibrację")
        }
    }
}

@Composable
fun TrackingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Moduł Śledzenia",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Trwa śledzenie wzroku...",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        // Tu można by dodać wizualizację punktu skupienia wzroku
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EdgeEyeTrackingTheme {
        HomeScreen(
            onCloseApp = {},
            onNavigate = {},
            currentDestination = AppDestinations.HOME
        )
    }
}