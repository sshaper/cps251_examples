package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Chapter 9 collection arguments: a List<Int> and an IntArray carry the same IDs.
 * Use this file as a standalone alternative to the other Chapter 9 examples.
 * It defines its own MainActivity and requires Navigation Compose 2.8.0 or later
 * for IntListType. Do not add all example MainActivity classes to one package.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    CollectionAppNav()
                }
            }
        }
    }
}

@Composable
fun CollectionAppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "collectionHome") {
        composable("collectionHome") {
            CollectionHomeScreen(
                onSendList = { selectedIds ->
                    // Repeat the query key once per element: ids=1&ids=3&ids=4.
                    // "$selectedIds" would produce display text such as [1, 3, 4],
                    // which is not the representation expected by IntListType.
                    val query = selectedIds.joinToString("&") { "ids=$it" }
                    navController.navigate("listSummary?$query")
                },
                onSendArray = { selectedIds ->
                    // Arrays use the same repeated-key route representation here.
                    val query = selectedIds.joinToString("&") { "ids=$it" }
                    navController.navigate("arraySummary?$query")
                }
            )
        }
        composable(
            route = "listSummary?ids={ids}",
            arguments = listOf(navArgument("ids") { type = NavType.IntListType })
        ) { entry ->
            // Use the same NavType to read the List<Int> it parsed and stored.
            // The safe call handles a missing arguments Bundle; orEmpty gives
            // the UI an empty list if no collection is available.
            val ids = entry.arguments?.let { NavType.IntListType[it, "ids"] }.orEmpty()
            CollectionSummaryScreen(
                heading = "Received a List<Int>",
                ids = ids,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "arraySummary?ids={ids}",
            arguments = listOf(navArgument("ids") { type = NavType.IntArrayType })
        ) { entry ->
            // IntArrayType stores an IntArray, so read it with getIntArray.
            val ids = entry.arguments?.getIntArray("ids")
            CollectionSummaryScreen(
                heading = "Received an IntArray",
                // Convert only for this shared display function's List<Int> parameter.
                // The navigation argument itself was parsed as an IntArray.
                ids = ids?.toList().orEmpty(),
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun CollectionHomeScreen(
    onSendList: (List<Int>) -> Unit,
    onSendArray: (IntArray) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Selected product IDs: 1, 3, 4")
        // Fixed nonempty selections keep this example focused on argument transport.
        // These are IDs, not Product objects or a complete product catalog.
        Button(onClick = { onSendList(listOf(1, 3, 4)) }) {
            Text("Send a list")
        }
        Button(onClick = { onSendArray(intArrayOf(1, 3, 4)) }) {
            Text("Send an array")
        }
    }
}

@Composable
fun CollectionSummaryScreen(heading: String, ids: List<Int>, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(heading)
        // joinToString also formats a readable message; this time the separator
        // is a comma and space because this text is for display, not navigation.
        Text("Product IDs: ${ids.joinToString(", ")}")
        Button(onClick = onBack) {
            Text("Back to selections")
        }
    }
}
