package com.example.uitesting

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreen_CompleteUserFlow() {
        // Step 1: Set up variables to track what happens
        var loginCalled = false
        var capturedUsername = ""
        var capturedPassword = ""

        // Step 2: Display our login screen in the test environment
        composeTestRule.setContent {
            LoginScreen { username, password ->
                // This callback will be called when the login button is pressed
                loginCalled = true
                capturedUsername = username
                capturedPassword = password
            }
        }

        // Step 3: Verify the screen displays correctly
        // Check that the welcome message is visible
        composeTestRule.onNodeWithContentDescription("welcome_text")
            .assertIsDisplayed()
            .assertTextEquals("Welcome Back")

        // Check that both input fields are present
        composeTestRule.onNodeWithContentDescription("username_field")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("password_field")
            .assertIsDisplayed()

        // Check that the login button is visible and has correct text
        composeTestRule.onNodeWithContentDescription("login_button")
            .assertIsDisplayed()
            .assertTextEquals("Login")

        // Step 4: Test user interaction - entering credentials
        // Type a username
        composeTestRule.onNodeWithContentDescription("username_field")
            .performTextInput("student123")

        // Type a password
        composeTestRule.onNodeWithContentDescription("password_field")
            .performTextInput("mypassword456")

        // Step 5: Verify the text was entered correctly by checking if the text appears in the UI
        composeTestRule.onNodeWithText("student123")
            .assertIsDisplayed()

        // Note: Password field text is hidden, so we can't verify it visually
        // We'll verify it through the callback instead

        // Step 6: Test the login button click
        composeTestRule.onNodeWithContentDescription("login_button")
            .performClick()

        // Step 7: Verify the callback was called with the right information
        assertTrue("Login callback should have been called", loginCalled)
        assertEquals("Username should match what was entered", "student123", capturedUsername)
        assertEquals("Password should match what was entered", "mypassword456", capturedPassword)
    }
}