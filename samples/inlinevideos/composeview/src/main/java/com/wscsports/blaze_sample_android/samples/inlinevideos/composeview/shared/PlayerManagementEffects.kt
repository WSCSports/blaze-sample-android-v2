package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.blaze.blazesdk.ads.models.ui.BlazeVideosAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer
import com.blaze.blazesdk.features.videos.inline.compose.BlazeVideosInlinePlayerComposeStateHandler
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlineInteractivePlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlinePreviewPlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosDelegateImpl

/**
 * PLAYER MANAGEMENT EFFECTS
 * 
 * This file contains composable effects and utilities for managing inline video player state and behavior.
 * These effects handle the lifecycle and state synchronization between Compose UI and player instances.
 * 
 * ## Key Features:
 * - **Player State Handler Creation**: Factory for creating configured player state handlers
 * - **Active State Management**: Controls when players are embedded vs showing placeholders
 * - **Volume Synchronization**: Keeps all players in sync with global volume settings
 * - **Lifecycle Awareness**: Properly manages player state during composition/disposal
 * - **Automatic State Updates**: Reactive effects that respond to state changes
 * 
 * ## Usage Examples:
 * 
 * **Creating Player State Handler:**
 * ```kotlin
 * val stateHandler = createPlayerStateHandler(
 *     item = feedItem,
 *     config = playerConfig
 * )
 * ```
 * 
 * **Player Active State:**
 * ```kotlin
 * PlayerActiveStateEffect(
 *     stateHandler = myStateHandler,
 *     isActive = isVisible && isSelected
 * )
 * ```
 * 
 * **Volume Synchronization:**
 * ```kotlin
 * PlayerVolumeEffect(
 *     volumeLiveData = mainViewModel.volume,
 *     stateHandler = myStateHandler
 * )
 * ```
 * 
 * ## Key Components:
 * - `createPlayerStateHandler`: Factory for creating configured player state handlers
 * - `PlayerActiveStateEffect`: Manages player embed/placeholder state
 * - `PlayerVolumeEffect`: Synchronizes volume changes across players
 */

// ==========================================
// SHARED DATA STRUCTURES
// ==========================================

/**
 * Configuration for creating player.
 */
data class PlayerConfig(
    val playerMode: PlayerMode,
    val cachePolicyLevel: BlazeCachingLevel = BlazeCachingLevel.DEFAULT,
    val videosAdsConfigType: BlazeVideosAdsConfigType = BlazeVideosAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG
) {
    sealed class PlayerMode {
        data class Preview(
            val previewPlayerStyle: BlazeVideosInlinePreviewPlayerStyle = BlazeVideosInlinePreviewPlayerStyle.base(),
            val fullScreenPlayerStyle: BlazeVideosPlayerStyle = BlazeVideosPlayerStyle.base()
        ) : PlayerMode()

        data class Interactive(
            val interactivePlayerStyle: BlazeVideosInlineInteractivePlayerStyle = BlazeVideosInlineInteractivePlayerStyle.base(),
            val fullScreenPlayerStyle: BlazeVideosPlayerStyle = BlazeVideosPlayerStyle.base()
        ) : PlayerMode()
    }
    
    companion object {
        /**
         * Default Preview configuration for scrollable lists.
         * Preview mode is designed for scrollable feeds.
         */
        val PREVIEW = PlayerConfig(playerMode = PlayerMode.Preview())
        
        /**
         * Interactive configuration for standalone players.
         * Interactive mode provides full user controls.
         */
        val INTERACTIVE = PlayerConfig(playerMode = PlayerMode.Interactive())
    }
}

/**
 * Common data class for feed items used across all feed implementations.
 * 
 * @param id Unique identifier for the item (also used as player container ID)
 * @param playerConfig Configuration for the player associated with this item
 * @param title Display title for the item
 * @param description Description text for the item
 * @param label Blaze label for data source
 */
data class FeedItem(
    val id: String,
    val playerConfig: PlayerConfig,
    val title: String,
    val description: String,
    val label: String
)

// ==========================================
// PLAYER STATE MANAGEMENT
// ==========================================

/**
 * Composable for creating a configured player state handler for feed items.
 * Factory function that creates and remembers a player state handler with all necessary
 * configuration based on the feed item and player settings.
 * 
 * This function handles the complex setup of player modes, data sources, and configuration,
 * providing a simple interface for creating properly configured players. The handler is
 * remembered by item ID to ensure stable instances across recompositions.
 *
 * @param item The feed item containing label and configuration data
 * @return Configured BlazeVideosInlinePlayerComposeStateHandler ready for use
 */
@Composable
fun createPlayerStateHandler(
    item: FeedItem
): BlazeVideosInlinePlayerComposeStateHandler {
    return remember(item.id) {
        val blazePlayerMode = when (item.playerConfig.playerMode) {
            is PlayerConfig.PlayerMode.Preview -> {
                BlazeVideosInlinePlayer.PlayerMode.Preview(
                    previewPlayerStyle = item.playerConfig.playerMode.previewPlayerStyle,
                    fullScreenPlayerStyle = item.playerConfig.playerMode.fullScreenPlayerStyle
                )
            }
            is PlayerConfig.PlayerMode.Interactive -> {
                BlazeVideosInlinePlayer.PlayerMode.Interactive(
                    interactivePlayerStyle = item.playerConfig.playerMode.interactivePlayerStyle,
                    fullScreenPlayerStyle = item.playerConfig.playerMode.fullScreenPlayerStyle
                )
            }
        }

        BlazeVideosInlinePlayerComposeStateHandler(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(item.label)),
            playerDelegate = InlineVideosDelegateImpl(),
            containerId = item.id,
            playerMode = blazePlayerMode,
            cachePolicyLevel = item.playerConfig.cachePolicyLevel,
            videosAdsConfigType = item.playerConfig.videosAdsConfigType
        )
    }
}

/**
 * Composable for managing player active state transitions.
 * Automatically embeds the player when active and shows placeholder when inactive.
 * 
 * This is the core effect that controls whether a player instance is actually rendering
 * video content or just showing a placeholder. Essential for performance in feeds where
 * only one player should be active at a time.
 * 
 * @param stateHandler The player state handler to control
 * @param isActive Whether the player should be actively playing/embedded
 * @param shouldAutoPlay Whether to auto-play when player becomes active
 */
@Composable
fun PlayerActiveStateEffect(
    stateHandler: BlazeVideosInlinePlayerComposeStateHandler,
    isActive: Boolean,
    shouldAutoPlay: Boolean = true
) {
    LaunchedEffect(isActive) {
        if (isActive) {
            stateHandler.embedPlayer(shouldAutoPlayOnStart = shouldAutoPlay)
        } else {
            stateHandler.embedPlaceholder()
        }
    }
}

// ==========================================
// VOLUME MANAGEMENT
// ==========================================

/**
 * Composable for player volume management and synchronization.
 * Automatically syncs player volume state when the app's global volume setting changes.
 * 
 * This effect observes LiveData volume changes and immediately applies them to the player,
 * ensuring consistent volume behavior across all inline players in the feed. Particularly
 * important in feeds where users expect volume changes to affect all players uniformly.
 * 
 * @param volumeLiveData LiveData that emits volume state changes (true = unmuted, false = muted)
 * @param stateHandler The player state handler to receive volume updates
 */
@Composable
fun PlayerVolumeEffect(
    volumeLiveData: LiveData<Boolean>,
    stateHandler: BlazeVideosInlinePlayerComposeStateHandler
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    
    LaunchedEffect(Unit) {
        val observer = Observer<Boolean> { _ ->
            stateHandler.onVolumeChanged()
        }
        volumeLiveData.observe(lifecycleOwner, observer)
    }
}
