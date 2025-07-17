package com.example.accessibilityperformance

import java.util.Date

// Data class representing an item in our list
data class Item(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val date: Date,
    val isSelected: Boolean = false
) 