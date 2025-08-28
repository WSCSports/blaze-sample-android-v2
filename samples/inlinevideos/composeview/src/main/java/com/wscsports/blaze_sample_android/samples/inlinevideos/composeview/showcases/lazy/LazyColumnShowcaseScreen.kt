package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.lazy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.managers.LazyColumnScrollVisibilityCenterActivation
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.PlayerConfig
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.VideoItem
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.generateFeedItems

/**
 * LazyColumnShowcaseScreen demonstrates inline videos in a LazyColumn with visibility management.
 * 
 * This showcase shows how to implement inline videos in a LazyColumn with proper
 * visibility-based player management. Only the most visible (centered) player is active
 * at any time, suitable for large feeds.
 * 
 * Key features demonstrated:
 * - LazyColumn implementation for efficient large lists
 * - Visibility-based player activation (one active player at a time)
 * - Preview player mode designed for feeds
 * - Automatic player switching based on scroll position
 * - Centermost visible player activation strategy
 * 
 * Implementation notes:
 * - Uses Preview mode for scrollable feeds
 * - Visibility calculation based on item bounds and screen center
 * - Players are activated/deactivated based on visibility percentage
 * - Debounced activation to prevent rapid switching during scroll
 * - Each player has unique containerId for proper state management
 */
// Define constants for this showcase
private const val CONTAINER_PREFIX = "lazy_column_showcase"
private val PLAYER_CONFIG = PlayerConfig.PREVIEW

@Composable
fun LazyColumnShowcaseScreen(
    viewModel: InlineVideosViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    var activePlayerIndex by remember { mutableStateOf(-1) }

    // Generate videos list
    val feedItems = remember { 
        generateFeedItems(
            numItems = 8,
            playerConfig = PLAYER_CONFIG,
            idPrefix = CONTAINER_PREFIX
        ) 
    }
    
    // LazyColumn scroll visibility management
    LazyColumnScrollVisibilityCenterActivation(
        listState = listState,
        onActiveIndexChanged = { index ->
            activePlayerIndex = index
        }
    )

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = feedItems,
            key = { index, item -> item.id }
        ) { index, item ->
            VideoItem(
                item = item,
                modifier = Modifier.fillMaxWidth(),
                isActive = index == activePlayerIndex,
                viewModel = viewModel
            )
        }
    }
}
