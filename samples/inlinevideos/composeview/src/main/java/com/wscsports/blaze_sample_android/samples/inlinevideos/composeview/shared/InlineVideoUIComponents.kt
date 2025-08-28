package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaze.blazesdk.features.videos.inline.compose.BlazeVideosInlinePlayerCompose
import com.blaze.blazesdk.features.videos.inline.compose.BlazeVideosInlinePlayerComposeStateHandler
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideoDataGenerator
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel

/**
 * INLINE VIDEO UI COMPONENTS
 * 
 * This file contains all UI-related components for inline video feeds.
 * These components are purely visual and handle only the presentation layer.
 * 
 * ## Key Features:
 * - **Pure UI Components**: No player logic, only visual presentation
 * - **Reusable Design**: Consistent styling across all showcases
 * - **Edge-to-edge Layout**: Players span full width with consistent spacing
 * 
 * ## Usage Examples:
 * 
 * **Smart Video Item (handles player creation automatically):**
 * ```kotlin
 * VideoItem(
 *     item = feedItem,
 *     isActive = isVisible,
 *     modifier = Modifier.fillMaxWidth()
 * )
 * ```
 * 
 * **Manual Video Card (with existing state handler):**
 * ```kotlin
 * VideoFeedItemCard(
 *     stateHandler = myStateHandler,
 *     title = "Video Title",
 *     description = "Video Description",
 *     isActive = true
 * )
 * ```
 * 
 * ## Key Components:
 * - `VideoItem`: Smart composable that auto-creates its player
 * - `VideoFeedItemCard`: Pure UI component for video display
 * - `generateFeedItems`: Data generation utility
 */

// ==========================================
// FEED GENERATION FUNCTIONS
// ==========================================

/**
 * Generates feed items with properly configured data.
 * 
 * This is the primary function for creating feed data across all showcase implementations.
 * It creates consistent data structure that can be used by any Compose feed implementation.
 * 
 * @param numItems Number of items to generate (default 8 for demo purposes)
 * @param playerConfig Player configuration to use for all items
 * @param idPrefix Prefix for container IDs (must be unique per showcase)
 * @param startingIndex Starting index for label generation (useful for pagination)
 * @return List of FeedItem ready for use in Compose UI
 */
fun generateFeedItems(
    numItems: Int = 8,
    playerConfig: PlayerConfig,
    idPrefix: String,
    startingIndex: Int = 0
): List<FeedItem> {
    return (0 until numItems).map { index ->
        val globalIndex = startingIndex + index
        val videoData = InlineVideoDataGenerator.generateSingleItem(globalIndex, idPrefix)
        
        FeedItem(
            id = videoData.id,
            playerConfig = playerConfig,
            title = videoData.title,
            description = videoData.description,
            label = videoData.label
        )
    }
}

// ==========================================
// VIDEO PLAYER COMPONENTS
// ==========================================

/**
 * Smart video item composable that automatically creates and manages its player.
 * 
 * This composable handles the complexity of player creation and state management,
 * providing a simple interface for displaying video items in feeds. It automatically
 * creates the appropriate state handler based on the item's configuration.
 * 
 * @param item The feed item to display
 * @param modifier Modifier for the root container
 * @param isActive Whether this player should be active (for visibility management)
 */
@Composable
fun VideoItem(
    item: FeedItem,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    viewModel: InlineVideosViewModel
) {
    val stateHandler = createPlayerStateHandler(item)
    
    // Apply player active state effect
    PlayerActiveStateEffect(
        stateHandler = stateHandler,
        isActive = isActive
    )
    
    // Volume synchronization effect - updates player when volume keys are pressed
    PlayerVolumeEffect(
        volumeLiveData = viewModel.volumeLiveData,
        stateHandler = stateHandler
    )
    
    VideoFeedItemCard(
        stateHandler = stateHandler,
        title = item.title,
        description = item.description,
        modifier = modifier
    )
}

// ==========================================
// UI COMPONENTS
// ==========================================

/**
 * Individual video feed item card for displaying video content.
 * 
 * This component provides the UI layout for video items with consistent styling.
 * 
 * Features:
 * - Edge-to-edge player container (no horizontal padding)
 * - 16:9 aspect ratio player area
 * - Title and description below player with consistent margins
 * 
 * @param stateHandler The state handler for the video player
 * @param title Video title to display
 * @param description Video description to display
 * @param modifier Modifier for the root container
 */
@Composable
fun VideoFeedItemCard(
    stateHandler: BlazeVideosInlinePlayerComposeStateHandler,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Player Container - Edge to edge, 16:9 aspect ratio
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            BlazeVideosInlinePlayerCompose(
                modifier = Modifier.fillMaxSize(),
                stateHandler = stateHandler
            )
        }
        
        // Content below player with margins
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Title
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                maxLines = 3
            )
        }
    }
}
