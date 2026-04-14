// Android and Compose imports
package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/** Drawer vs tabbed body: full-screen Home or Settings, or the scrollable tab strip with five topics. */
private enum class MainSection { Tabs, Home, Settings }

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

/**
 * AppWithDrawerAndTabs demonstrates a navigation drawer plus a horizontally scrollable tab row.
 * - Drawer items Home and Settings show full-screen pages; another item returns to the tab strip.
 * - Many tabs illustrate [ScrollableTabRow] when labels do not all fit on screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDrawerAndTabs() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }
    var mainSection by remember { mutableStateOf(MainSection.Tabs) }
    val tabs = listOf(
        "News", "Sports", "Weather", "Business", "Technology"
    )

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
                    label = { Text("Tab sections") },
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
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        edgePadding = 16.dp
                    ) {
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
                        3 -> BusinessContent()
                        4 -> TechnologyContent()
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

@Composable
fun BusinessContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Business Content", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun TechnologyContent() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Technology Content", style = MaterialTheme.typography.headlineMedium)
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

