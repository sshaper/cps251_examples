package com.example.accessibilityperformance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

// Main screen that combines accessibility and performance features
@Composable
fun AccessibleAndPerformantListScreen(
    items: List<Item>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onItemClick: (Item) -> Unit,
    onItemSelectionChange: (Item, Boolean) -> Unit
) {
    // Step 1: Performance optimization - derived state for filtering
    val filteredItems = remember(items, searchQuery) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                items
            } else {
                items.filter { item ->
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }
    
    // Step 2: Performance optimization - remember scroll state
    val listState = rememberLazyListState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "List of items with search functionality"
            }
    ) {
        // Step 3: Accessible search bar
        AccessibleSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange
        )
        
        // Step 4: Performance optimization - show scroll indicator
        val isScrolled = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }
        
        AnimatedVisibility(
            visible = isScrolled.value,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Text(
                text = "Scrolled down - showing items ${listState.firstVisibleItemIndex + 1} to ${listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size}",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
                    .semantics {
                        contentDescription = "Scroll position indicator"
                    }
            )
        }
        
        // Step 5: The main list with accessibility and performance features
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = "List of ${filteredItems.value.size} items"
                }
        ) {
            items(
                items = filteredItems.value,
                key = { item -> item.id } // Critical for performance
            ) { item ->
                AccessibleAndPerformantListItem(
                    item = item,
                    onClick = { onItemClick(item) },
                    onSelectionChange = { isSelected -> onItemSelectionChange(item, isSelected) }
                )
            }
        }
    }
} 