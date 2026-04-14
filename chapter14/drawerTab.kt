// Android and Compose imports
package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch

/**
 * MainActivity is the entry point of the app. It sets up the Compose UI and applies the MaterialTheme.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the status bar to use dark icons for better visibility
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                AppWithDrawerAndTabs()
            }
        }
    }
}

/** Which main area is shown: tabbed news sections, or a full-screen Home / Settings from the drawer. */
private enum class MainSection { Tabs, Home, Settings }

/**
 * AppWithDrawerAndTabs demonstrates a layout with a navigation drawer and a tab row.
 * - The navigation drawer slides in from the left and contains navigation items.
 * - The top app bar contains a menu icon to open the drawer.
 * - The tab row allows switching between three content screens.
 * - Drawer items Home and Settings swap the body to dedicated pages (same idea as tab content).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDrawerAndTabs() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }
    var mainSection by remember { mutableStateOf(MainSection.Tabs) }
    val tabs = listOf("News", "Sports", "Weather")

    fun closeDrawer() {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("News App", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = null) },
                    label = { Text("News, Sports & Weather") },
                    selected = mainSection == MainSection.Tabs,
                    onClick = {
                        mainSection = MainSection.Tabs
                        closeDrawer()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = mainSection == MainSection.Home,
                    onClick = {
                        mainSection = MainSection.Home
                        closeDrawer()
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = mainSection == MainSection.Settings,
                    onClick = {
                        mainSection = MainSection.Settings
                        closeDrawer()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (mainSection) {
                                MainSection.Home -> "Home"
                                MainSection.Settings -> "Settings"
                                MainSection.Tabs -> "News App"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (mainSection == MainSection.Tabs) {
                    TabRow(selectedTabIndex = selectedTab) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title) }
                            )
                        }
                    }
                    when (selectedTab) {
                        0 -> NewsContent()
                        1 -> SportsContent()
                        2 -> WeatherContent()
                    }
                } else if (mainSection == MainSection.Home) {
                    HomeContent()
                } else {
                    SettingsContent()
                }
            }
        }
    }
}

/**
 * HomeContent is shown when the user chooses Home in the drawer (same pattern as tab bodies).
 */
@Composable
fun HomeContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Welcome to the Home screen.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * SettingsContent is shown when the user chooses Settings in the drawer.
 */
@Composable
fun SettingsContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Adjust your preferences here.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * NewsContent displays the content for the "News" tab.
 */
@Composable
fun NewsContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("News Content", style = MaterialTheme.typography.headlineMedium)
    }
}

/**
 * SportsContent displays the content for the "Sports" tab.
 */
@Composable
fun SportsContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Sports Content", style = MaterialTheme.typography.headlineMedium)
    }
}

/**
 * WeatherContent displays the content for the "Weather" tab.
 */
@Composable
fun WeatherContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Weather Content", style = MaterialTheme.typography.headlineMedium)
    }
}




/**
 * Preview function for the main screen. Allows you to see the UI in Android Studio's preview.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    AppWithDrawerAndTabs()
}

