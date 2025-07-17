# NavigationTest App

This is a simple Android app that demonstrates navigation testing with Jetpack Compose Navigation. The app is designed to be used as an example for teaching navigation testing concepts to college students.

## App Structure

The app consists of three simple screens:

1. **Home Screen** - The main screen with navigation buttons
2. **Profile Screen** - Shows user profile information with a back button
3. **Detail Screen** - Shows details for a specific item (with an ID parameter) and a back button

## Navigation Flow

- Home → Profile (via "Go to Profile" button)
- Home → Detail (via "Go to Detail" button, passes ID "123")
- Profile → Home (via "Back" button)
- Detail → Home (via "Back" button)

## Key Files

- `Screen.kt` - Sealed class defining navigation routes
- `HomeScreen.kt` - Home screen composable with navigation buttons
- `ProfileScreen.kt` - Profile screen composable with back button
- `DetailScreen.kt` - Detail screen composable that displays an ID parameter
- `AppNavigation.kt` - Main navigation setup using NavHost
- `MainActivity.kt` - Entry point that uses AppNavigation
- `NavigationTest.kt` - Comprehensive test that verifies the complete navigation flow

## Testing

The app includes a comprehensive test in `NavigationTest.kt` that:

1. Verifies the app starts on the home screen
2. Tests navigation to the profile screen
3. Tests back navigation from profile to home
4. Tests navigation to the detail screen with a parameter
5. Tests back navigation from detail to home
6. Verifies all screen content is displayed correctly

## Running the Tests

To run the navigation tests:

1. Open the project in Android Studio
2. Right-click on `NavigationTest.kt` in the Project view
3. Select "Run 'NavigationTest'"

Or run from the command line:
```bash
./gradlew connectedAndroidTest
```

## Learning Objectives

This app demonstrates:
- Basic Jetpack Compose Navigation setup
- Navigation with parameters
- Back navigation using popBackStack()
- Using test tags for reliable element identification
- Comprehensive navigation testing
- Testing navigation parameters
- Testing back navigation behavior

## Dependencies

The app uses:
- Jetpack Compose Navigation
- Material3 components
- Compose UI Testing framework 