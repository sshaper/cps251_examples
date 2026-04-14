
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    // State to track which tab is currently selected (0 = first tab, 1 = second, etc.)
    var selectedTab by remember { mutableStateOf(0) }

    // The Column contains the tab row and the content for the selected tab
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 50.dp)) {
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.TopCenter) {
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
    // Track whether the button is currently being pressed
    var isPressed by remember { mutableStateOf(false) }
    
    // Animate the scale smoothly: when pressed, shrink to 95% size
    // animateFloatAsState automatically animates between old and new values
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, // 0.95f = 95% size, 1f = 100% size
        animationSpec = tween(durationMillis = 100), // Animation takes 100ms
        label = "tapScale"
    )
    
    // Animate the color smoothly: change to red when pressed
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed)
            Color(0xFFFF0000) // Red color when pressed
        else
            MaterialTheme.colorScheme.primary, // Normal theme color
        animationSpec = tween(durationMillis = 100), // Animation takes 100ms
        label = "tapColor"
    )
    Card(
        modifier = Modifier
            .scale(scale) // Apply the animated scale to make button shrink/grow
            .pointerInput(Unit) { // pointerInput allows us to detect touch gestures
                detectTapGestures(
                    onPress = { // Called when user presses down
                        isPressed = true // Update state to trigger scale/color animation
                        try {
                            awaitRelease() // Wait until user releases finger
                        } finally {
                            isPressed = false // Always reset when done (even if error occurs)
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
    // State holding a list of items that can be deleted
    var items by remember { mutableStateOf((1..3).map { "Item $it" }.toMutableList()) }
    val scope = rememberCoroutineScope()
    Column {
        // Display each item as a swipeable list item
        items.forEach { item ->
            key(item) {
                SwipeableListItem(
                    item = item,
                    onDelete = {
                        scope.launch {
                            delay(250) // Let dismiss animation/background settle briefly before removal.
                            val updatedItems = items.toMutableList()
                            updatedItems.remove(item)
                            items = updatedItems
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
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
    // State that tracks the swipe gesture and determines if item should be dismissed
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value -> // Called when swipe completes
            // EndToStart means swiped from right to left (delete gesture)
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete() // Call the delete callback
                true // Return true to confirm the dismissal
            } else {
                false // Return false to cancel and snap back
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState, // Connect the state to track swipe gestures
        backgroundContent = {
            // This content shows behind the item when swiping (revealed as you swipe)
            // Red background with delete icon appears when swiping left
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd // Align icon to the right
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
    ) {
        // This is the main content that gets swiped (the actual list item)
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
    // Store the card's position as an offset from its original position
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    // Animate the offset smoothly with a spring animation (bouncy effect)
    // This makes the card movement feel natural and smooth
    val animatedOffset by animateOffsetAsState(
        targetValue = offset, // Target position to animate to
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, // How bouncy the animation is
            stiffness = Spring.StiffnessLow // How quickly it responds
        ),
        label = "dragOffset" // Used for debugging, not needed for app to run
    )
    Card(
        modifier = Modifier
            // Apply the animated offset to move the card
            .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // Mark the gesture as consumed so other handlers don't receive it
                    // Add the drag amount to the current offset (accumulates movement)
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
    // State to control whether content is visible or hidden
    var visible by remember { mutableStateOf(false) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide" else "Show")
        }
        // AnimatedVisibility automatically animates when visible changes
        AnimatedVisibility(
            visible = visible,
            // Enter animation: slides down from top and fades in
            enter = slideInVertically(
                initialOffsetY = { -it }, // Start above the visible area
                animationSpec = tween(durationMillis = 300) // 300ms animation
            ) + fadeIn(animationSpec = tween(durationMillis = 300)), // Also fade in
            // Exit animation: slides up and fades out
            exit = slideOutVertically(
                targetOffsetY = { -it }, // End above the visible area
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
    // Track if user is performing a long press
    var isLongPressed by remember { mutableStateOf(false) }
    
    // Animate scale: shrink to 75% when long pressed
    val scale by animateFloatAsState(
        targetValue = if (isLongPressed) .75f else 1f, // 0.75f = 75% size
        animationSpec = tween(durationMillis = 200),
        label = "longPressScale"
    )
    
    // Animate color: change to green when long pressed
    val color by animateColorAsState(
        targetValue = if (isLongPressed) {
            Color(0xFF00FF00) // Green color for long press
        } else {
            MaterialTheme.colorScheme.primary // Default theme color
        },
        animationSpec = tween(durationMillis = 200),
        label = "longPressColor" // Used for debugging, not needed for app to run
    )
    Card(
        modifier = Modifier
            .scale(scale) // Apply animated scale
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { isLongPressed = true }, // Triggered when user holds down
                    onPress = {
                        isLongPressed = false // Reset on regular press
                        try {
                            awaitRelease() // Wait for finger to lift
                        } finally {
                            isLongPressed = false // Always reset when done
                        }
                    }
                )
            }
            .size(150.dp),
            colors = CardDefaults.cardColors(
            containerColor = color)
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
    // Slider value from 0.0 to 1.0 (0% to 100%)
    var sliderValue by remember { mutableStateOf(0.5f) } // Start at 50%
    // Track if user is actively dragging (affects animation behavior)
    var isDragging by remember { mutableStateOf(false) }

    // Store the actual width of the slider track in pixels
    // We need this to calculate thumb position accurately
    var trackWidth by remember { mutableStateOf(0f) }

    // Animate the slider value smoothly
    val animatedValue by animateFloatAsState(
        targetValue = sliderValue,
        animationSpec = if (isDragging) {
            tween(durationMillis = 0) // No animation while dragging (instant response)
        } else {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy) // Bouncy animation when released
        },
        label = "sliderValue"
    )

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Value: ${(animatedValue * 100).toInt()}%")
        Spacer(modifier = Modifier.height(16.dp))

        // This is the parent Box that contains both the track and the thumb
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart // Align thumb to the start
        ) {
            // Slider Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
                    .onSizeChanged { newSize ->
                        // Capture the actual width when the track is measured
                        // This happens after the layout is calculated
                        trackWidth = newSize.width.toFloat()
                    }
                    .pointerInput(trackWidth) { // Re-trigger if width changes
                        if (trackWidth == 0f) return@pointerInput // Safety check: avoid division by zero
                        detectDragGestures(
                            onDragStart = { isDragging = true }, // User started dragging
                            onDragEnd = { isDragging = false }, // User released
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // Calculate how much the drag represents as a percentage
                                // dragAmount.x is pixels moved, trackWidth is total width
                                val dragPercentage = dragAmount.x / trackWidth
                                // Update slider value, keeping it between 0 and 1
                                sliderValue = (sliderValue + dragPercentage).coerceIn(0f, 1f)
                            }
                        )
                    }
            )

            // Slider Thumb (the draggable circle)
            Box(
                modifier = Modifier
                    .offset {
                        // Calculate thumb position: animatedValue (0-1) * trackWidth = position in pixels
                        val thumbCenterOffset = animatedValue * trackWidth
                        // Center the thumb: subtract half its width so it's centered on the position
                        val thumbHalfWidth = (24.dp.toPx() / 2)
                        IntOffset((thumbCenterOffset - thumbHalfWidth).roundToInt(), 0)
                    }
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
    // Store scale (zoom), rotation, and position separately
    var scale by remember { mutableStateOf(1f) } // 1f = 100% size
    var rotation by remember { mutableStateOf(0f) } // 0f = no rotation
    var offset by remember { mutableStateOf(Offset.Zero) } // Position offset
    
    // Animate each property separately for smooth transitions
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "multiScale"
    )
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "multiRotation"
    )
    val animatedOffset by animateOffsetAsState(
        targetValue = offset,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "multiOffset"
    )
    Card(
        modifier = Modifier
            .offset { IntOffset(animatedOffset.x.roundToInt(), animatedOffset.y.roundToInt()) }
            .scale(animatedScale)
            .rotate(animatedRotation)
            .pointerInput(Unit) {
                // detectTransformGestures handles multi-touch gestures (pinch, rotate, drag)
                detectTransformGestures { _, pan, zoom, rotationChange ->
                    // pan: how much to move (drag)
                    // zoom: how much to scale (pinch)
                    // rotationChange: how much to rotate
                    scale *= zoom // Multiply scale (zoom in/out)
                    rotation += rotationChange // Add rotation change
                    offset += pan // Add pan movement
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
    // List of items that can be rearranged
    var items by remember { mutableStateOf((1..5).map { "Box $it" }) }
    // Track which item is currently being dragged (null = nothing being dragged)
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    // Track how far the dragged item has moved from its original position
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val boxHeight = 80.dp
    val density = LocalDensity.current // Needed to convert dp to pixels

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEachIndexed { index, item ->
            // Check if this specific item is being dragged
            val isDragging = draggedIndex == index
            
            // Animate offset: if dragging, use dragOffset; otherwise return to zero
            val animatedOffset by animateOffsetAsState(
                targetValue = if (isDragging) dragOffset else Offset.Zero,
                animationSpec = if (isDragging) {
                    tween(durationMillis = 0) // No animation while dragging (follow finger)
                } else {
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy) // Bounce back when released
                },
                label = "rearrangeOffset"
            )
            // Scale up slightly when dragging (visual feedback)
            val scale by animateFloatAsState(
                targetValue = if (isDragging) 1.05f else 1f, // 5% larger when dragging
                animationSpec = tween(durationMillis = 200),
                label = "rearrangeScale"
            )
            // Make slightly transparent when dragging (shows it's being moved)
            val alpha by animateFloatAsState(
                targetValue = if (isDragging) 0.8f else 1f, // 80% opacity when dragging
                animationSpec = tween(durationMillis = 200),
                label = "rearrangeAlpha"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(boxHeight)
                    .padding(vertical = 4.dp)
                    .offset {
                        IntOffset(
                            animatedOffset.x.roundToInt(),
                            animatedOffset.y.roundToInt()
                        )
                    }
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
                                // Calculate new position based on how far item was dragged
                                val boxHeightPx = with(density) { boxHeight.toPx() } // Convert dp to pixels
                                val newIndex = when {
                                    // Dragged up more than half a box height: move up one position
                                    dragOffset.y < -boxHeightPx / 2 && index > 0 -> index - 1
                                    // Dragged down more than half a box height: move down one position
                                    dragOffset.y > boxHeightPx / 2 && index < items.size - 1 -> index + 1
                                    else -> index // Not far enough, stay in same position
                                }

                                // If position changed, rearrange the list
                                if (newIndex != index) {
                                    val newItems = items.toMutableList()
                                    val itemToMove = newItems.removeAt(index) // Remove from old position
                                    newItems.add(newIndex, itemToMove) // Insert at new position
                                    items = newItems // Update the list
                                }

                                // Reset drag state
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
    // Tiles stored as a flat list: [1, 2, 3, 0] represents a 2x2 grid
    // 0 represents the empty space that tiles can swap with
    var tiles by remember { mutableStateOf(listOf(1, 2, 3, 0)) }
    // Track which tile is being dragged
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    // Track drag offset for visual feedback
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val gridSize = 2 // 2x2 grid (2 columns, 2 rows)

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
                        // Calculate flat list index from row/col: row * columns + col
                        // Example: row 1, col 0 = 1*2+0 = index 2
                        val index = row * gridSize + col
                        val tileValue = tiles[index] // Get the value at this position
                        val isDragging = draggedIndex == index // Is this tile being dragged?

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
                            // Empty space: show as a gray box (no number, not draggable)
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                        } else {
                            // Tile with a number: can be dragged
                            // Tile with number
                            Card(
                                modifier = Modifier
                                    .size(100.dp)
                                    .offset {
                                        IntOffset(
                                            animatedOffset.x.roundToInt(),
                                            animatedOffset.y.roundToInt()
                                        )
                                    }
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
                                                // Find where the empty space (0) is located
                                                val emptyIndex = tiles.indexOf(0)

                                                // Only swap if the dragged tile is next to the empty space
                                                if (isAdjacent(index, emptyIndex, gridSize)) {
                                                    // Swap: move tile to empty space, make old position empty
                                                    val newTiles = tiles.toMutableList()
                                                    newTiles[emptyIndex] = tiles[index] // Move tile to empty space
                                                    newTiles[index] = 0 // Make old position empty
                                                    tiles = newTiles
                                                }

                                                // Reset drag state
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
 * Two tiles are adjacent if they share a row or column and are next to each other.
 */
private fun isAdjacent(index1: Int, index2: Int, gridSize: Int): Boolean {
    // Convert flat index to row/column coordinates
    val row1 = index1 / gridSize // Integer division gives row
    val col1 = index1 % gridSize  // Remainder gives column
    val row2 = index2 / gridSize
    val col2 = index2 % gridSize

    // Adjacent if: same row and columns differ by 1, OR same column and rows differ by 1
    // This means they're next to each other horizontally or vertically (not diagonally)
    return (row1 == row2 && kotlin.math.abs(col1 - col2) == 1) ||
            (col1 == col2 && kotlin.math.abs(row1 - row2) == 1)
}

