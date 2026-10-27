package com.example.navstate.navigation

/**
 * NavRoutes contains all the route names used in the navigation graph
 * Using constants prevents typos and makes route names reusable
 * 
 * Benefits of this approach:
 * - Reuse: constants reduce inconsistent spelling; route strings are not fully type safe
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
    // Despite the key name userId, this sample sends a display name such as
    // Scott Shaper or John Doe. Keep the key consistent with argument extraction.
    const val PROFILE = "profile/{userId}"
    
    /**
     * Route for the Settings screen
     * This screen doesn't require any parameters
     */
    const val SETTINGS = "settings"
}

