package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.managers

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.flow.debounce

/**
 * COLUMN SCROLL VISIBILITY MANAGEMENT
 * 
 * This file contains scroll visibility management utilities specifically for regular Column feeds.
 * Handles bounds-based player activation with precise visibility calculations for traditional scrolling.
 * 
 * ## Key Features:
 * - **Bounds-Based Detection**: Uses actual component bounds for precise visibility calculations
 * - **Threshold-Based Activation**: Configurable visibility threshold for player activation
 * - **Real-Time Deactivation**: Immediately deactivates players that fall below threshold
 * - **Center-Focused Selection**: Prioritizes most centered visible players
 * 
 * ## Usage Example:
 * ```kotlin
 * ColumnScrollVisibilityCenterActivation(
 *     scrollState = rememberScrollState(),
 *     playerBounds = playerBoundsMap,
 *     containerBounds = containerRect,
 *     onActivePlayerChanged = { playerId -> activePlayerId = playerId }
 * )
 * ```
 * 
 * ## Key Components:
 * - `ColumnScrollVisibilityCenterActivation`: Main composable for scroll management
 * - `calculateItemVisibility`: Helper for visibility percentage calculations
 * - `findMostCenteredColumnItem`: Helper for finding centered items
 */

/**
 * Composable for managing scroll-based player visibility in regular Column feeds.
 * Uses precise bounds-based visibility calculations to activate the most centered player
 * that meets the visibility threshold requirements.
 * 
 * This effect is optimized for traditional Column scrolling where all items are rendered
 * and visibility is determined by comparing component bounds. It provides immediate
 * deactivation when players scroll out of view and debounced activation after scrolling.
 * 
 * @param scrollState The ScrollState to monitor for scroll events
 * @param playerBounds Map of player IDs to their current bounds in the window
 * @param containerBounds The bounds of the scroll container in the window
 * @param onActivePlayerChanged Callback when active player changes (null = none active)
 * @param visibilityThreshold Minimum visibility percentage (0.0-1.0) to activate a player
 * @param debounceMs Debounce delay in milliseconds before activating after scroll stops
 */
@Composable
fun ColumnScrollVisibilityCenterActivation(
    scrollState: ScrollState,
    playerBounds: Map<String, Rect>,
    containerBounds: Rect?,
    onActivePlayerChanged: (String?) -> Unit,
    visibilityThreshold: Float = 0.5f,
    debounceMs: Long = 300L
) {
    var isScrolling by remember { mutableStateOf(false) }
    
    // Simple scroll detection
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .collect { scrolling ->
                isScrolling = scrolling
                if (scrolling) {
                    // Immediately deactivate during scroll
                    onActivePlayerChanged(null)
                }
            }
    }
    
    // Debounced visibility calculation when scroll stops
    LaunchedEffect(scrollState, playerBounds, containerBounds) {
        snapshotFlow { 
            Triple(scrollState.value, playerBounds.toMap(), containerBounds)
        }
        .debounce(debounceMs)
        .collect { (_, bounds, container) ->
            if (!isScrolling && container != null) {
                val activePlayerId = findMostCenteredColumnItem(
                    playerBounds = bounds,
                    containerBounds = container,
                    visibilityThreshold = visibilityThreshold
                )
                onActivePlayerChanged(activePlayerId)
            }
        }
    }
}

/**
 * Calculates the visibility percentage of an item based on its bounds and the container bounds.
 * 
 * @param itemBounds The bounds of the item to check
 * @param containerBounds The bounds of the container
 * @return Visibility percentage (0.0 to 1.0)
 */
private fun calculateItemVisibility(
    itemBounds: Rect,
    containerBounds: Rect
): Float {
    if (itemBounds.isEmpty || containerBounds.isEmpty) return 0f
    
    val visibleTop = maxOf(itemBounds.top, containerBounds.top)
    val visibleBottom = minOf(itemBounds.bottom, containerBounds.bottom)
    val visibleHeight = maxOf(0f, visibleBottom - visibleTop)
    val totalHeight = itemBounds.height
    
    return if (totalHeight > 0f) visibleHeight / totalHeight else 0f
}

/**
 * Finds the most centered item among those that meet the visibility threshold.
 * 
 * @param playerBounds Map of player IDs to their bounds
 * @param containerBounds The container bounds
 * @param visibilityThreshold Minimum visibility required
 * @return ID of the most centered visible player, or null if none qualify
 */
private fun findMostCenteredColumnItem(
    playerBounds: Map<String, Rect>,
    containerBounds: Rect,
    visibilityThreshold: Float
): String? {
    val containerCenter = containerBounds.center.y
    var closestPlayerId: String? = null
    var closestDistance = Float.MAX_VALUE
    
    playerBounds.forEach { (playerId, bounds) ->
        val visibility = calculateItemVisibility(bounds, containerBounds)
        
        if (visibility >= visibilityThreshold) {
            val itemCenter = bounds.center.y
            val distance = kotlin.math.abs(itemCenter - containerCenter)
            
            if (distance < closestDistance) {
                closestDistance = distance
                closestPlayerId = playerId
            }
        }
    }
    
    return closestPlayerId
}
