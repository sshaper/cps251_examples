package com.example.mainviewmodeltesting

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Simple unit tests for SimpleViewModel.
 * These tests demonstrate basic ViewModel testing.
 */
class SimpleViewModelTest {
    
    // This is the ViewModel we're testing
    private lateinit var viewModel: SimpleViewModel
    
    // This runs before each test to set up our testing environment
    @Before
    fun setup() {
        viewModel = SimpleViewModel()
    }
    
    /**
     * Test that the ViewModel starts with no user loaded.
     */
    @Test
    fun `initial state should have no user`() {
        assertNull("User should be null initially", viewModel.user)
        assertFalse("Loading should be false initially", viewModel.isLoading)
        assertNull("Error should be null initially", viewModel.error)
    }
    
    /**
     * Test that loading a valid user works correctly.
     */
    @Test
    fun `loadUser with valid ID should load user`() {
        // Load user with ID "1"
        viewModel.loadUser("1")
        
        // Verify user is loaded correctly
        assertNotNull("User should be loaded", viewModel.user)
        assertEquals("User ID should be 1", "1", viewModel.user?.id)
        assertEquals("User name should be John Doe", "John Doe", viewModel.user?.name)
        assertEquals("User email should be john@example.com", "john@example.com", viewModel.user?.email)
        assertFalse("Loading should be false after loading", viewModel.isLoading)
        assertNull("Error should be null for valid user", viewModel.error)
    }
    
    /**
     * Test that loading an invalid user shows an error.
     */
    @Test
    fun `loadUser with invalid ID should show error`() {
        // Load user with invalid ID
        viewModel.loadUser("999")
        
        // Verify error is shown
        assertNull("User should be null for invalid ID", viewModel.user)
        assertFalse("Loading should be false after loading", viewModel.isLoading)
        assertNotNull("Error should exist for invalid user", viewModel.error)
        assertEquals("Error message should be correct", "User not found", viewModel.error)
    }
    
    /**
     * Test that updateUserName changes the user's name.
     */
    @Test
    fun `updateUserName should change user name`() {
        // First, set up a user
        viewModel.loadUser("1")
        
        // Verify user is loaded
        assertNotNull("User should be loaded", viewModel.user)
        assertEquals("Initial name should be John Doe", "John Doe", viewModel.user?.name)
        
        // Update the name
        viewModel.updateUserName("Jane Doe")
        
        // Verify the name changed
        assertEquals("Name should be updated", "Jane Doe", viewModel.user?.name)
        // Verify other properties remain the same
        assertEquals("User ID should remain the same", "1", viewModel.user?.id)
        assertEquals("User email should remain the same", "john@example.com", viewModel.user?.email)
    }
    
    /**
     * Test that clearError removes error messages.
     */
    @Test
    fun `clearError should remove error message`() {
        // Create an error by trying to load a non-existent user
        viewModel.loadUser("999")
        
        // Verify error exists
        assertNotNull("Error should exist", viewModel.error)
        assertEquals("Error message should be correct", "User not found", viewModel.error)
        
        // Clear the error
        viewModel.clearError()
        
        // Verify error is cleared
        assertNull("Error should be null after clearing", viewModel.error)
    }
    
    /**
     * Test that clearUser removes the current user and returns to initial state.
     */
    @Test
    fun `clearUser should remove current user`() {
        // Load a user first
        viewModel.loadUser("1")
        
        // Verify user is loaded
        assertNotNull("User should be loaded", viewModel.user)
        assertEquals("User name should be John Doe", "John Doe", viewModel.user?.name)
        
        // Clear the user
        viewModel.clearUser()
        
        // Verify user is cleared
        assertNull("User should be null after clearing", viewModel.user)
        assertNull("Error should be null after clearing", viewModel.error)
    }
}