package com.example.accessibilityperformance

import java.util.*

// Sample data generator for the accessibility and performance demo
object SampleData {
    
    private val sampleTitles = listOf(
        "Android Development",
        "Jetpack Compose",
        "Material Design",
        "Accessibility Testing",
        "Performance Optimization",
        "UI/UX Design",
        "Mobile App Development",
        "Kotlin Programming",
        "Testing Strategies",
        "App Architecture"
    )
    
    private val sampleDescriptions = listOf(
        "Learn modern Android development with the latest tools and frameworks",
        "Build beautiful user interfaces with declarative UI toolkit",
        "Create consistent and accessible user experiences",
        "Ensure your app works for users with disabilities",
        "Make your app fast and responsive for all users",
        "Design intuitive and engaging user experiences",
        "Develop mobile applications for Android platform",
        "Write clean and efficient Kotlin code",
        "Implement comprehensive testing for your apps",
        "Structure your app with clean architecture principles"
    )
    
    private val sampleImageUrls = listOf(
        "https://picsum.photos/96/96?random=1",
        "https://picsum.photos/96/96?random=2",
        "https://picsum.photos/96/96?random=3",
        "https://picsum.photos/96/96?random=4",
        "https://picsum.photos/96/96?random=5",
        "https://picsum.photos/96/96?random=6",
        "https://picsum.photos/96/96?random=7",
        "https://picsum.photos/96/96?random=8",
        "https://picsum.photos/96/96?random=9",
        "https://picsum.photos/96/96?random=10"
    )
    
    // Generate a list of sample items
    fun generateSampleItems(count: Int = 50): List<Item> {
        val items = mutableListOf<Item>()
        val random = Random()
        
        for (i in 0 until count) {
            val titleIndex = i % sampleTitles.size
            val descriptionIndex = i % sampleDescriptions.size
            val imageIndex = i % sampleImageUrls.size
            
            items.add(
                Item(
                    id = "item_$i",
                    title = sampleTitles[titleIndex],
                    description = sampleDescriptions[descriptionIndex],
                    imageUrl = sampleImageUrls[imageIndex],
                    date = Date(System.currentTimeMillis() - random.nextLong() % (30L * 24 * 60 * 60 * 1000)), // Random date within last 30 days
                    isSelected = false
                )
            )
        }
        
        return items
    }
} 