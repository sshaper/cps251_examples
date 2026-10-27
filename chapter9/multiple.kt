package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Navigation imports
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * MainActivity is the entry point of the application.
 * It sets up the basic window configuration and initializes the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use a light status bar appearance (dark icons on a light background)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        
        // Set up the Compose UI with navigation
        setContent {
            MaterialTheme {
                Surface {
                    NavigationWithMultipleArgs()
                }
            }
        }
    }
}

@Composable
fun NavigationWithMultipleArgs() {
    // Own navigation here; screens report selected values through callbacks.
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToProduct = { id, name -> 
                    // Match product/{id}/{name}: ID first, then name.
                    // These fixed sample names contain no route delimiters. Arbitrary
                    // text needs Uri.encode(name) before insertion into the route.
                    navController.navigate("product/$id/$name") 
                }
            )
        }
        composable(
            // Each placeholder has a matching key and parsing type below.
            "product/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Read values belonging to this particular product destination entry.
            // Safe calls allow a null Bundle; getInt returns 0 for a missing key in
            // an existing Bundle, so this is not general validation of product IDs.
            val id = backStackEntry.arguments?.getInt("id")
            val name = backStackEntry.arguments?.getString("name")
            ProductScreen(
                id = id,
                name = name,
                onNavigateBack = {
                    // Remove details and return to the existing product-list entry.
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * HomeScreen displays the main screen of the application.
 * It contains a list of products that can be navigated to, demonstrating
 * how to pass multiple arguments through navigation.
 * 
 * @param onNavigateToProduct Callback function that takes product id and name
 *                          and handles navigation to the product screen
 */
@Composable
fun HomeScreen(onNavigateToProduct: (Int, String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Available Products",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Sample product list - in a real app, this would come from a data source
        val products = listOf(
            Product(1, "Smartphone"),
            Product(2, "Laptop"),
            Product(3, "Headphones"),
            Product(4, "Tablet")
        )
        
        // Display each product as a button
        products.forEach { product ->
            Button(
                onClick = { onNavigateToProduct(product.id, product.name) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View ${product.name}")
            }
        }
    }
}

/**
 * ProductScreen displays the details of a specific product.
 * It shows the product ID and name passed through navigation
 * and provides a way to go back to the home screen.
 * 
 * @param id The ID of the product being displayed
 * @param name The name of the product being displayed
 * @param onNavigateBack Callback function to handle navigation back to the home screen
 */
@Composable
fun ProductScreen(
    id: Int?,
    name: String?,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display product details
        Text(
            text = "Product Details",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Guard nullable parameters before display. Normal navigation supplies both
        // required values; this check does not validate whether an ID exists in a data source.
        if (id != null && name != null) {
            Text("Product ID: $id")
            Text("Product Name: $name")
            
            // Additional product details could be added here
            // For example: description, price, specifications, etc.
        } else {
            Text("Error: Product information not available")
        }
        
        // Back button
        Button(
            onClick = onNavigateBack,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back to Products")
        }
    }
}

/**
 * Data class representing a product in the application.
 * This is used to structure the product data in the home screen.
 */
data class Product(
    val id: Int,
    val name: String
)

/**
 * ProfileScreen displays the profile information for a specific user.
 * It shows the user ID passed through navigation and provides a way to go back.
 * 
 * @param userId The ID of the user whose profile is being displayed
 * @param onNavigateBack Callback function to handle navigation back to the home screen
 */
@Composable
fun ProfileScreen(
    userId: String?,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Display the user ID passed through navigation
        Text("Profile for user: $userId")
        Button(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}

/**
 * Preview function to see the UI in Android Studio's preview pane.
 * This allows developers to see how the navigation and screens look without running the app.
 * Note: The preview might not show the actual navigation behavior as it's static.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsFeedExamplePreview() {
    MaterialTheme {
        NavigationWithMultipleArgs()
    }
}