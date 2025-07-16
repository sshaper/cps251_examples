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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
        "Multi-Touch"
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
            }
        }
    }
}

// 1. Tap Animation Example
/**
 * Demonstrates a button that animates (scales down) when tapped.
 * Uses pointerInput to detect press and animateFloatAsState for scaling.
 */
@Composable
fun TapAnimationExample() {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100), label = "tapScale"
    )
    Button(
        onClick = { /* Handle click */ },
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
            }
    ) {
        Text("Tap Me!")
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

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    GestureAnimationTabScreen()
}

