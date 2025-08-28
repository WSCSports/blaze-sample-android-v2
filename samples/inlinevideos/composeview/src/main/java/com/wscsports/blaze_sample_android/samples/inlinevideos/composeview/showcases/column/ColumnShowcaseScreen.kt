package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.column

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.managers.ColumnScrollVisibilityCenterActivation
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.PlayerConfig
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.VideoItem
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.generateFeedItems

/**
 * Preview inline video feed using regular Column with scroll.
 * 
 * ## Test Focus
 * - Regular Column with scroll state
 * - Compose-style visibility management
 * - Preview player mode testing
 * - Same VideoItem pattern as lazy feeds
 * 
 * ## Key Features:
 * - **Traditional Scrolling**: Uses regular Column with verticalScroll modifier
 * - **Bounds-Based Visibility**: Tracks actual component bounds for precise visibility calculations
 * - **One Active Player**: Uses visibility manager to activate only the most centered player
 * - **Preview Mode**: Designed for scrollable feeds with multiple video items
 * - **Edge-to-edge Layout**: Players span full width with consistent spacing
 * 
 * ## Implementation Notes:
 * - All items are rendered at once (suitable for small feeds)
 * - Visibility calculated using actual component bounds
 * - Player activation is debounced to prevent rapid switching during scroll
 * - Uses Preview player mode for scrollable feeds
 */

// Define constants for this showcase
private const val CONTAINER_PREFIX = "column_showcase"
private val PLAYER_CONFIG = PlayerConfig.PREVIEW

@Composable
fun ColumnShowcaseScreen(
    viewModel: InlineVideosViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var activePlayerId by remember { mutableStateOf<String?>(null) }
    
    // Track bounds of each player for visibility calculation
    val playerBounds = remember { mutableStateMapOf<String, Rect>() }
    var containerBounds by remember { mutableStateOf<Rect?>(null) }

    // Generate videos list
    val feedItems = remember { 
        generateFeedItems(
            numItems = 8,
            playerConfig = PLAYER_CONFIG,
            idPrefix = CONTAINER_PREFIX
        ) 
    }

    // Column scroll visibility management
    ColumnScrollVisibilityCenterActivation(
        scrollState = scrollState,
        playerBounds = playerBounds,
        containerBounds = containerBounds,
        onActivePlayerChanged = { playerId ->
            activePlayerId = playerId
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .onGloballyPositioned { coordinates ->
                containerBounds = coordinates.boundsInWindow()
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top spacing
        Spacer(modifier = Modifier.height(0.dp))
        
        // Video items with bounds tracking
        feedItems.forEach { item ->
            VideoItem(
                item = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        playerBounds[item.id] = coordinates.boundsInWindow()
                    },
                isActive = item.id == activePlayerId,
                viewModel = viewModel
            )
        }
        
        // Bottom spacing
        Spacer(modifier = Modifier.height(16.dp))
    }
}
