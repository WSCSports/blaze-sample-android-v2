package com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.showcases.methods

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.ads.models.ui.BlazeVideosAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer
import com.blaze.blazesdk.features.videos.inline.compose.BlazeVideosInlinePlayerCompose
import com.blaze.blazesdk.features.videos.inline.compose.BlazeVideosInlinePlayerComposeStateHandler
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlineInteractivePlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosDelegateImpl
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.composeview.shared.PlayerVolumeEffect

// Constants for the showcase
private const val CONTAINER_ID = "methods-showcase-compose"

/**
 * MethodsShowcaseScreen demonstrates BlazeVideosInlinePlayerCompose methods.
 * 
 * This showcase provides an interactive player with buttons to demonstrate all available
 * methods of the BlazeVideosInlinePlayerCompose API. It serves as a comprehensive
 * reference for developers implementing inline video functionality in Compose.
 * 
 * Key components demonstrated:
 * - BlazeVideosInlinePlayerComposeStateHandler creation and configuration
 * - All player control methods (embed, reset, play, pause, etc.)
 * - Interactive player mode with custom styling
 * - Proper lifecycle management in Compose
 * - Event handling through delegate pattern
 */
@Composable
fun MethodsShowcaseScreen(
    viewModel: InlineVideosViewModel,
    modifier: Modifier = Modifier
) {
    // Create state handler once and remember it across recompositions
    // This is the central component that manages the inline player in Compose
    val stateHandler = remember {
        BlazeVideosInlinePlayerComposeStateHandler(
            // The label represents the video content collection in your CMS
            // Replace this with your actual content labels from Blaze dashboard
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("inline-video-1")),
            playerDelegate = InlineVideosDelegateImpl(),
            // Unique identifier for this player instance - each player must have unique containerId
            containerId = CONTAINER_ID,
            playerMode = BlazeVideosInlinePlayer.PlayerMode.Interactive(
                interactivePlayerStyle = BlazeVideosInlineInteractivePlayerStyle.base(),
                fullScreenPlayerStyle = BlazeVideosPlayerStyle.base()
            ),
            cachePolicyLevel = BlazeCachingLevel.DEFAULT,
            videosAdsConfigType = BlazeVideosAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG
        )
    }
    
    // Volume synchronization effect - updates player when volume keys are pressed
    PlayerVolumeEffect(
        volumeLiveData = viewModel.volumeLiveData,
        stateHandler = stateHandler
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Player Container - Edge to edge, 200dp height
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            BlazeVideosInlinePlayerCompose(
                modifier = Modifier.fillMaxSize(),
                stateHandler = stateHandler
            )
        }

        // Buttons Section - Edge to edge layout
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Prepare Videos & Embed Placeholder
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Prepare videos for the current data source
                // This pre-loads content for faster playback
                OutlinedButton(
                    onClick = { stateHandler.prepareVideos() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Prepare Videos", color = Color.Black)
                }

                // Embeds placeholder/thumbnail in the container
                // Can be called directly at any time
                OutlinedButton(
                    onClick = { stateHandler.embedPlaceholder() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Embed Placeholder", color = Color.Black)
                }
            }

            // Row 2: Embed Player & Reset To Placeholder
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Embeds the video player in the container
                // Can be called directly, will transition from placeholder if exists
                OutlinedButton(
                    onClick = { stateHandler.embedPlayer(shouldAutoPlayOnStart = true) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Embed Player", color = Color.Black)
                }

                // Resets from player back to placeholder state
                // Transitions from active player to placeholder/thumbnail
                OutlinedButton(
                    onClick = { stateHandler.resetToPlaceholder() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Reset To Placeholder", color = Color.Black)
                }
            }

            // Row 3: Dispose & Enter Fullscreen
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Disposes entire container - removes all views and player
                // All player-related methods will have no effect after disposal
                OutlinedButton(
                    onClick = { stateHandler.disposeContainer() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Dispose", color = Color.Black)
                }

                // Enters fullscreen mode
                // Only works if player is embedded
                OutlinedButton(
                    onClick = { stateHandler.enterFullScreen() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Enter Fullscreen", color = Color.Black)
                }
            }

            // Row 4: Play & Pause
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Resumes video playback
                // Only works if player exists - overrides any programmatic pause
                OutlinedButton(
                    onClick = { stateHandler.resumePlayer() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Play", color = Color.Black)
                }

                // Pauses video playback - programmatic pause
                // Only works if player exists - remains paused until explicitly resumed
                // Only resumePlayer() call or user interaction (if not blocked) can resume
                OutlinedButton(
                    onClick = { stateHandler.pausePlayer() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Pause", color = Color.Black)
                }
            }

            // Row 5: Block Interaction & Unblock Interaction
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Blocks user interaction with the player
                // Only works if player exists - disables all touch events and user controls
                // Player state can only be changed programmatically when blocked
                OutlinedButton(
                    onClick = { stateHandler.blockInteraction() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Block Interaction", color = Color.Black)
                }

                // Unblocks user interaction with the player
                // Only works if player exists - re-enables touch events and user controls
                // User can now play/pause/seek through player interface
                OutlinedButton(
                    onClick = { stateHandler.unblockInteraction() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Unblock Interaction", color = Color.Black)
                }
            }
        }
    }
}
