package com.example.tuprofe.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

// ── Shimmer ───────────────────────────────────────────────────────────────────

/**
 * Shimmer loading animation.
 * Apply AFTER .clip(shape) so the shape masks correctly.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1800f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )
    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 600f, 0f),
            end = Offset(translateAnim, 0f)
        )
    )
}

// ── Press scale ───────────────────────────────────────────────────────────────

/**
 * Subtle scale-down on press. Does NOT consume pointer events so the
 * Card's own onClick still fires normally.
 */
fun Modifier.pressScaleEffect(): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.965f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pressScale"
    )
    graphicsLayer { scaleX = scale; scaleY = scale }
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                pressed = true
                do {
                    val event = awaitPointerEvent()
                } while (event.changes.any { it.pressed })
                pressed = false
            }
        }
}

// ── Staggered list entrance ───────────────────────────────────────────────────

/**
 * Staggered fade + slide-up entrance for list items.
 * Use inside LazyColumn with itemsIndexed.
 */
@Composable
fun AnimatedListItem(
    index: Int,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 65L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(320)) +
                slideInVertically(
                    animationSpec = tween(360, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 4 }
                )
    ) {
        content()
    }
}

// ── Screen entrance ───────────────────────────────────────────────────────────

/**
 * One-shot fade + slide-up entrance for a section/screen.
 * [delayMs] staggers multiple sections on the same screen.
 */
@Composable
fun AnimatedScreen(
    delayMs: Long = 0L,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (delayMs > 0L) delay(delayMs)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(350)) +
                slideInVertically(
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 5 }
                )
    ) {
        content()
    }
}

// ── Star bounce ───────────────────────────────────────────────────────────────

/**
 * Returns a scale value that bounces once when [selected] flips to true.
 * Apply via graphicsLayer { scaleX = s; scaleY = s } on the star Icon.
 */
@Composable
fun rememberStarBounceScale(selected: Boolean): Float {
    var triggered by remember { mutableStateOf(false) }
    LaunchedEffect(selected) { if (selected) triggered = !triggered }
    val scale by animateFloatAsState(
        targetValue = if (triggered) 1.4f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        finishedListener = { triggered = false },
        label = "starBounce"
    )
    return scale
}
