// Main package for the app
package com.example.bookexamplesapp

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import androidx.core.view.WindowCompat

/**
 * MainActivity is the entry point of the app.
 * - Sets up the Compose UI and applies the MaterialTheme.
 * - Configures the status bar for better visibility.
 * - Displays a tabbed navigation for gesture/animation examples.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set status bar icons to dark for visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                GestureAnimationTabScreen()
            }
        }
    }
}

/**
 * Main screen with navigation tabs for each gesture/animation example.
 * Uses ScrollableTabRow for a better tab layout with concise tab names.
 */
@Composable
fun GestureAnimationTabScreen() {
    // List of tab titles for each example - using shorter, more concise names
    val tabTitles = listOf(
        "Tap",
        "Swipe",
        "Drag",
        "Visibility",
        "Long Press",
        "Slider",
        "Multi-Touch",
        "Rearrange",
        "Tile Swap"
    )
    var selectedTab by remember { mutableStateOf(0) }

    // The Column contains the tab row and the content for the selected tab
    Column(modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
        // Use ScrollableTabRow for better handling of multiple tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 16.dp
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { 
                        Text(
                            text = title,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium
                        ) 
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        // Display the selected example based on the selected tab
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.TopCenter) {
            when (selectedTab) {
                0 -> TapAnimationExample()
                1 -> SwipeToDeleteExample()
                2 -> DraggableCardExample()
                3 -> AnimatedVisibilityExample()
                4 -> LongPressScaleExample()
                5 -> InteractiveSliderExample()
                6 -> MultiGestureCardExample()
                7 -> RearrangeExample()
                8 -> TileSwapExample()
            }
        }
    }
}

// 1. Tap Animation Example
/**
 * Demonstrates a button-styled card that provides visual feedback when tapped.
 * When pressed, the button scales down to 95% of its size and changes color
 * from primary to primaryContainer. When released, it animates back to its
 * original size and color. Uses detectTapGestures with pointerInput to detect
 * press events, animateFloatAsState for smooth scale animation, and
 * animateColorAsState for color transitions.
 */
@Composable
fun TapAnimationExample() {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100), label = "tapScale"
    )
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) 
            MaterialTheme.colorScheme.primaryContainer 
        else 
            MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 100), label = "tapColor"
    )
    Card(
        modifier = Modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        try {
                            awaitRelease()
                        } finally {
                            isPressed = false
                        }
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = buttonColor
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tap Me!",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

// 2. Swipe to Delete Example
/**
 * Shows a list of items that can be swiped to delete using SwipeToDismissBox.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteExample() {
    var items by remember { mutableStateOf((1..3).map { "Item $it" }.toMutableList()) }
    Column {
        items.forEach { item ->
            SwipeableListItem(item = item, onDelete = { items.remove(item) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * A single swipeable list item using SwipeToDismissBox.
 * When swiped from end to start, the item is deleted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableListItem(item: String, onDelete: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            // Red background with delete icon when swiping
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                headlineContent = { Text(item) }
            )
        }
    }
}

// 3. Draggable Card Example
/**
 * Demonstrates a card that can be dragged around the screen.
 * Uses detectDragGestures and animateOffsetAsState for smooth movement.
 */
@Composable
fun DraggableCardExample() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val animatedOffset by animateOffsetAsState(
        targetValue = offset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = "dragOffset"
    )
    Card(
        modifier = Modifier
            .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += Offset(dragAmount.x, dragAmount.y)
                }
            }
            .size(200.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Drag me around!")
        }
    }
}

// 4. Animated Visibility Example
/**
 * Demonstrates showing/hiding content with animated visibility transitions.
 * Uses AnimatedVisibility with slide and fade effects.
 */
@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "This content animates in and out!",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// 5. Long Press Scale Example
/**
 * Demonstrates scaling a card up on long press and back down on release.
 * Uses detectTapGestures for long press and animateFloatAsState for scaling.
 */
@Composable
fun LongPressScaleExample() {
    var isLongPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isLongPressed) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200), label = "longPressScale"
    )
    Card(
        modifier = Modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { isLongPressed = true },
                    onPress = {
                        isLongPressed = false
                        try {
                            awaitRelease()
                        } finally {
                            isLongPressed = false
                        }
                    }
                )
            }
            .size(150.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Long press me!")
        }
    }
}

// 6. Interactive Slider Example
/**
 * Demonstrates a custom slider that animates its value and thumb position.
 * Uses detectDragGestures for horizontal dragging and animateFloatAsState for smooth animation.
 */
@Composable
fun InteractiveSliderExample() {
    var sliderValue by remember { mutableStateOf(0.5f) }
    var isDragging by remember { mutableStateOf(false) }
    val animatedValue by animateFloatAsState(
        targetValue = sliderValue,
        animationSpec = if (isDragging) {
            tween(durationMillis = 0)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        }, label = "sliderValue"
    )
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Value: \\${(animatedValue * 100).toInt()}%")
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            sliderValue = (sliderValue + dragAmount.x / 300f)
                                .coerceIn(0f, 1f)
                        }
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .offset(x = (animatedValue * 300f - 12f).dp)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}

// 7. Multi-Gesture Card Example
/**
 * Demonstrates a card that responds to pinch (zoom), rotate, and drag gestures simultaneously.
 * Uses detectTransformGestures and animates scale, rotation, and position.
 */
@Composable
fun MultiGestureCardExample() {
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "multiScale"
    )
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "multiRotation"
    )
    val animatedOffset by animateOffsetAsState(
        targetValue = offset,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "multiOffset"
    )
    Card(
        modifier = Modifier
            .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
            .scale(animatedScale)
            .rotate(animatedRotation)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotationChange ->
                    scale *= zoom
                    rotation += rotationChange
                    offset += pan
                }
            }
            .size(200.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Pinch, rotate, and drag me!")
        }
    }
}

// 8. Rearrange Example
/**
 * Demonstrates 5 boxes that can be rearranged by drag and drop.
 * Uses detectDragGestures to handle dragging and calculates new positions.
 * Provides visual feedback (elevation, opacity, scale) while dragging.
 */
@Composable
fun RearrangeExample() {
    var items by remember { mutableStateOf((1..5).map { "Box $it" }) }
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val boxHeight = 80.dp
    val density = LocalDensity.current
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEachIndexed { index, item ->
            val isDragging = draggedIndex == index
            val animatedOffset by animateOffsetAsState(
                targetValue = if (isDragging) dragOffset else Offset.Zero,
                animationSpec = if (isDragging) {
                    tween(durationMillis = 0)
                } else {
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                }, label = "rearrangeOffset"
            )
            val scale by animateFloatAsState(
                targetValue = if (isDragging) 1.05f else 1f,
                animationSpec = tween(durationMillis = 200), label = "rearrangeScale"
            )
            val alpha by animateFloatAsState(
                targetValue = if (isDragging) 0.8f else 1f,
                animationSpec = tween(durationMillis = 200), label = "rearrangeAlpha"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxHeight)
                    .padding(vertical = 4.dp)
                    .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
                    .scale(scale)
                    .alpha(alpha)
                    .shadow(
                        elevation = if (isDragging) 8.dp else 2.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .pointerInput(index) {
                        detectDragGestures(
                            onDragStart = {
                                draggedIndex = index
                                dragOffset = Offset.Zero
                            },
                            onDragEnd = {
                                // Calculate new position based on drag distance
                                val boxHeightPx = with(density) { boxHeight.toPx() }
                                val newIndex = when {
                                    dragOffset.y < -boxHeightPx / 2 && index > 0 -> index - 1
                                    dragOffset.y > boxHeightPx / 2 && index < items.size - 1 -> index + 1
                                    else -> index
                                }
                                
                                if (newIndex != index) {
                                    val newItems = items.toMutableList()
                                    val itemToMove = newItems.removeAt(index)
                                    newItems.add(newIndex, itemToMove)
                                    items = newItems
                                }
                                
                                draggedIndex = null
                                dragOffset = Offset.Zero
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += Offset(dragAmount.x, dragAmount.y)
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

// 9. Tile Swap Example
/**
 * Demonstrates a simple 2x2 tile swap game.
 * Drag tiles to swap with the empty space (only adjacent tiles can swap).
 * Uses detectDragGestures, grid layout, and adjacency checking.
 * Provides visual feedback while dragging.
 */
@Composable
fun TileSwapExample() {
    var tiles by remember { mutableStateOf(listOf(1, 2, 3, 0)) } // 0 represents empty space
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val gridSize = 2 // 2x2 grid
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Drag tiles to swap with empty space",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 2x2 Grid layout
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(gridSize) { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(gridSize) { col ->
                        val index = row * gridSize + col
                        val tileValue = tiles[index]
                        val isDragging = draggedIndex == index
                        
                        val animatedOffset by animateOffsetAsState(
                            targetValue = if (isDragging) dragOffset else Offset.Zero,
                            animationSpec = if (isDragging) {
                                tween(durationMillis = 0)
                            } else {
                                spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            }, label = "tileOffset"
                        )
                        
                        val scale by animateFloatAsState(
                            targetValue = if (isDragging) 1.1f else 1f,
                            animationSpec = tween(durationMillis = 200), label = "tileScale"
                        )
                        
                        val alpha by animateFloatAsState(
                            targetValue = if (isDragging) 0.8f else 1f,
                            animationSpec = tween(durationMillis = 200), label = "tileAlpha"
                        )
                        
                        if (tileValue == 0) {
                            // Empty space
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                        } else {
                            // Tile with number
                            Card(
                                modifier = Modifier
                                    .size(100.dp)
                                    .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
                                    .scale(scale)
                                    .alpha(alpha)
                                    .shadow(
                                        elevation = if (isDragging) 8.dp else 4.dp,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .pointerInput(index) {
                                        detectDragGestures(
                                            onDragStart = {
                                                draggedIndex = index
                                                dragOffset = Offset.Zero
                                            },
                                            onDragEnd = {
                                                // Find empty space index
                                                val emptyIndex = tiles.indexOf(0)
                                                
                                                // Check if dragged tile is adjacent to empty space
                                                if (isAdjacent(index, emptyIndex, gridSize)) {
                                                    // Swap tiles
                                                    val newTiles = tiles.toMutableList()
                                                    newTiles[emptyIndex] = tiles[index]
                                                    newTiles[index] = 0
                                                    tiles = newTiles
                                                }
                                                
                                                draggedIndex = null
                                                dragOffset = Offset.Zero
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragOffset += Offset(dragAmount.x, dragAmount.y)
                                            }
                                        )
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tileValue.toString(),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Helper function to check if two indices are adjacent in a grid.
 */
private fun isAdjacent(index1: Int, index2: Int, gridSize: Int): Boolean {
    val row1 = index1 / gridSize
    val col1 = index1 % gridSize
    val row2 = index2 / gridSize
    val col2 = index2 % gridSize
    
    // Adjacent if same row and columns differ by 1, or same column and rows differ by 1
    return (row1 == row2 && kotlin.math.abs(col1 - col2) == 1) ||
           (col1 == col2 && kotlin.math.abs(row1 - row2) == 1)
}

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    GestureAnimationTabScreen()
}

