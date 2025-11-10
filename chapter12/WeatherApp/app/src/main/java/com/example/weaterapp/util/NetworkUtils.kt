package com.example.weaterapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * NetworkUtils is a utility object that provides methods to check network connectivity status.
 * This is a singleton object, meaning there is only one instance throughout the application.
 * 
 * Use this utility to verify if the device has an active internet connection before making
 * network requests, such as API calls to fetch weather data.
 */
object NetworkUtils {
    /**
     * Checks if the device currently has an active internet connection.
     * 
     * This method uses Android's ConnectivityManager to check:
     * 1. If there is an active network connection
     * 2. If that network has the capability to reach the internet
     * 
     * @param context The Android context needed to access system services
     * @return true if the device has an active internet connection, false otherwise
     */
    fun isOnline(context: Context): Boolean {
        // Get the ConnectivityManager system service, which manages network connectivity
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        // Get the currently active default network
        // If no active network exists, return false immediately
        val network = connectivityManager.activeNetwork ?: return false
        
        // Get the capabilities of the active network (e.g., Wi-Fi, cellular, internet access)
        // If we can't get capabilities, return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        // Check if the network has the capability to reach the internet
        // This is the key check - having a network connection doesn't guarantee internet access
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
} 