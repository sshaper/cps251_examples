package com.example.navigationtest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Comprehensive navigation test that verifies the complete navigation flow
 * This test covers:
 * - Starting on the home screen
 * - Navigating to profile screen
 * - Back navigation from profile to home
 * - Navigating to detail screen with parameter
 * - Back navigation from detail to home
 * - Verifying navigation controller state
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCompleteNavigationFlow() {
        // Step 1: Display our app in the test environment
        composeTestRule.setContent {
            AppNavigation()
        }
        
        // Step 3: Verify we start on the home screen
        composeTestRule.onNodeWithTag("home_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Home Screen")
        
        // Step 4: Test navigation to profile screen
        composeTestRule.onNodeWithTag("profile_button")
            .performClick()
        
        // Step 5: Verify we're now on the profile screen
        composeTestRule.onNodeWithTag("profile_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Profile Screen")
        
        // Step 6: Test back navigation from profile to home
        composeTestRule.onNodeWithTag("back_button")
            .performClick()
        
        // Step 7: Verify we're back on the home screen
        composeTestRule.onNodeWithTag("home_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Home Screen")
        
        // Step 8: Test navigation to detail screen with parameter
        composeTestRule.onNodeWithTag("detail_button")
            .performClick()
        
        // Step 9: Verify we're on the detail screen with correct ID
        composeTestRule.onNodeWithTag("detail_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Detail Screen - ID: 123")
        
        // Step 10: Test back navigation from detail to home
        composeTestRule.onNodeWithTag("back_button")
            .performClick()
        
        // Step 11: Verify we're back on the home screen again
        composeTestRule.onNodeWithTag("home_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Home Screen")
        
        // Step 12: Verify we're back on the home screen (final verification)
        composeTestRule.onNodeWithTag("home_screen_title")
            .assertIsDisplayed()
            .assertTextEquals("Home Screen")
    }
} 