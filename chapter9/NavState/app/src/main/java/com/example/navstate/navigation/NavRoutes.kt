package com.example.navstate.navigation

/**
 * NavRoutes contains all the route names used in the navigation graph
 * Using constants prevents typos and makes route names reusable
 * 
 * Benefits of this approach:
 * - Type safety: Compiler catches typos
 * - Refactoring: Easy to change route names in one place
 * - Documentation: Clear overview of all available routes
 */
object NavRoutes {
    
    /**
     * Route for the Home screen
     * This is the default starting screen of the app
     */
    const val HOME = "home"
    
    /**
     * Route for the Profile screen with a dynamic user ID parameter
     * The {userId} part is a navigation argument that gets replaced with actual values
     * Example: "profile/123" or "profile/john_doe"
     */
    const val PROFILE = "profile/{userId}"
    
    /**
     * Route for the Settings screen
     * This screen doesn't require any parameters
     */
    const val SETTINGS = "settings"
}

