// Android and Compose imports
package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
 * AppWithDrawerAndTabs demonstrates a layout with a navigation drawer and a tab row.
 * - The navigation drawer slides in from the left and contains navigation items.
 * - The top app bar contains a menu icon to open the drawer.
 * - The tab row allows switching between three content screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppWithDrawerAndTabs() {
    // State for the navigation drawer (open/closed)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // Coroutine scope for opening/closing the drawer
    val scope = rememberCoroutineScope()
    // State for the currently selected tab
    var selectedTab by remember { mutableStateOf(0) }
    // Use enough tabs so horizontal scrolling is visible
    val tabs = listOf(
        "News", "Sports", "Weather", "Business", "Technology"
    )
    
    // ModalNavigationDrawer provides the side drawer UI
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Drawer content: app name and navigation items
            ModalDrawerSheet {
                Text("News App", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { /* Handle Home navigation */ }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { /* Handle Settings navigation */ }
                )
            }
        }
    ) {
        // Scaffold provides the top app bar and main content area
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("News App") },
                    navigationIcon = {
                        // Menu icon opens the navigation drawer
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            // Main content: tab row and content for the selected tab
            Column(modifier = Modifier.padding(padding)) {
                // ScrollableTabRow lets tabs scroll horizontally
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
                
                // Show content based on the selected tab
                when (selectedTab) {
                    0 -> NewsContent()
                    1 -> SportsContent()
                    2 -> WeatherContent()
                    3 -> BusinessContent()
                    4 -> TechnologyContent()
                }
            }
        }
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

