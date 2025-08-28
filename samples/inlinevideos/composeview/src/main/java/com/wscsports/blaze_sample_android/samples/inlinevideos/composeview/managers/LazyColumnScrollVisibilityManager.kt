package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.managers

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * LAZY COLUMN SCROLL VISIBILITY MANAGEMENT
 * 
 * This file contains scroll visibility management utilities specifically for LazyColumn feeds.
 * Handles scroll-based player activation with performance optimization for lazy-loaded content.
 * 
 * ## Key Features:
 * - **Debounced Activation**: Players activate only after scroll stops to prevent performance issues
 * - **Center-Based Selection**: Activates the most centered visible item automatically
 * - **Scroll State Awareness**: Deactivates players during active scrolling
 * - **Lazy-Optimized**: Designed for LazyColumn's item-based architecture
 * 
 * ## Usage Example:
 * ```kotlin
 * LazyColumnScrollVisibilityCenterActivation(
 *     listState = rememberLazyListState(),
 *     onActiveIndexChanged = { index -> activePlayerIndex = index }
 * )
 * ```
 * 
 * ## Key Components:
 * - `LazyColumnScrollVisibilityCenterActivation`: Main composable for scroll management
 * - `findMostCenteredLazyItem`: Helper for finding centered items
 */

/**
 * Composable for managing scroll-based player visibility in LazyColumn feeds.
 * Automatically activates the most centered player after scrolling stops and deactivates
 * all players during active scrolling for optimal performance.
 * 
 * This effect is specifically optimized for LazyColumn implementations where items are
 * rendered on-demand. It uses debouncing to prevent excessive player switching during
 * fast scrolling and ensures only one player is active at a time.
 * 
 * @param listState The LazyListState to monitor for scroll events
 * @param onActiveIndexChanged Callback when active player index changes (-1 = none active)
 * @param debounceMs Debounce delay in milliseconds before activating after scroll stops
 */
@Composable
fun LazyColumnScrollVisibilityCenterActivation(
    listState: LazyListState,
    onActiveIndexChanged: (Int) -> Unit,
    debounceMs: Long = 300L
) {
    var isScrolling by remember { mutableStateOf(false) }
    
    // Simple scroll detection
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { scrolling ->
                isScrolling = scrolling
                if (scrolling) {
                    // Immediately deactivate during scroll
                    onActiveIndexChanged(-1)
                }
            }
    }
    
    // Debounced visibility calculation when scroll stops
    LaunchedEffect(listState) {
        snapshotFlow { 
            listState.layoutInfo.let { info ->
                Triple(
                    info.visibleItemsInfo.map { Triple(it.index, it.offset, it.size) },
                    info.viewportStartOffset,
                    info.viewportEndOffset
                )
            }
        }
        .distinctUntilChanged()
        .debounce(debounceMs)
        .collect { (visibleItems, viewportStart, viewportEnd) ->
            if (!isScrolling) {
                val activeIndex = findMostCenteredLazyItem(
                    visibleItems = visibleItems,
                    viewportStart = viewportStart,
                    viewportEnd = viewportEnd
                )
                onActiveIndexChanged(activeIndex)
            }
        }
    }
}

/**
 * Finds the most centered item among visible items in a LazyColumn.
 * 
 * Uses the LazyColumn's layout information to determine which item is closest
 * to the center of the viewport and has sufficient visibility.
 * 
 * @param visibleItems List of visible items with their index, offset, and size
 * @param viewportStart Start offset of the viewport
 * @param viewportEnd End offset of the viewport
 * @return Index of the most centered visible item, or -1 if none qualify
 */
private fun findMostCenteredLazyItem(
    visibleItems: List<Triple<Int, Int, Int>>, // index, offset, size
    viewportStart: Int,
    viewportEnd: Int
): Int {
    if (visibleItems.isEmpty()) return -1
    
    val viewportCenter = (viewportStart + viewportEnd) / 2f
    var closestIndex = -1
    var closestDistance = Float.MAX_VALUE
    
    visibleItems.forEach { (index, offset, size) ->
        val itemStart = offset
        val itemEnd = offset + size
        val itemCenter = (itemStart + itemEnd) / 2f
        
        // Check if item has sufficient visibility (at least 50%)
        val visibleStart = maxOf(itemStart, viewportStart)
        val visibleEnd = minOf(itemEnd, viewportEnd)
        val visibleSize = maxOf(0, visibleEnd - visibleStart)
        val visibilityRatio = if (size > 0) visibleSize.toFloat() / size else 0f
        
        if (visibilityRatio >= 0.5f) {
            val distance = kotlin.math.abs(itemCenter - viewportCenter)
            if (distance < closestDistance) {
                closestDistance = distance
                closestIndex = index
            }
        }
    }
    
    return closestIndex
}
