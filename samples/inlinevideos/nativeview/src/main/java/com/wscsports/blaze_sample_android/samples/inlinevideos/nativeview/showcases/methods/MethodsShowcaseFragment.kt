package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.methods

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.blaze.blazesdk.ads.models.ui.BlazeVideosAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlineInteractivePlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosDelegateImpl
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.R
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.FragmentMethodsShowcaseBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * MethodsShowcaseFragment demonstrates BlazeVideosInlinePlayer methods.
 * This represents an inline video player with interactive controls to showcase all available methods.
 */
class MethodsShowcaseFragment : Fragment(R.layout.fragment_methods_showcase) {

    companion object {
        // The label represents the video content collection in your CMS.
        // Replace this with your actual content labels from Blaze dashboard.
        private const val DEFAULT_LABEL = "inline-video-1"
        
        // Unique identifier for this player instance.
        // Each inline player must have a unique containerId.
        private const val CONTAINER_ID_PREFIX = "methods_showcase"
    }

    private val binding by viewBinding(FragmentMethodsShowcaseBinding::bind)
    private val viewModel: InlineVideosViewModel by activityViewModels()
    private var inlinePlayer: BlazeVideosInlinePlayer? = null
    
    // Delegate implementation to handle player events and interactions
    private val inlineDelegate = InlineVideosDelegateImpl()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayer()
        setupButtons()
        subscribeToVolumeChanges()
    }

    /**
     * Sets up the BlazeVideosInlinePlayer with required configuration.
     * 
     * Key components:
     * - lifecycleOwner: Manages player lifecycle with fragment lifecycle  
     * - storeOwner: Required for internal state management
     * - containerView: The view that will host the inline player
     * - dataSource: Content source using labels from your CMS
     * - containerId: Unique identifier for this player instance
     * - playerDelegate: Handles events and user interactions
     * - playerMode: Defines player behavior (Interactive or Preview) with styling
     */
    private fun setupPlayer() {
        // Using default label for this demonstration
        // In production, this would come from your content management system
        val label = "inline-video-1"
        
        inlinePlayer = BlazeVideosInlinePlayer(
            lifecycleOwner = this,
            storeOwner = this,
            containerView = binding.playerContainer,
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(label)),
            containerId = "${CONTAINER_ID_PREFIX}_${System.currentTimeMillis()}",
            playerDelegate = inlineDelegate,
            videosAdsConfigType = BlazeVideosAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG,
            playerMode = BlazeVideosInlinePlayer.PlayerMode.Interactive(
                interactivePlayerStyle = BlazeVideosInlineInteractivePlayerStyle.base(),
                fullScreenPlayerStyle = BlazeVideosPlayerStyle.base()
            )
        )
    }

    /**
     * Sets up method demonstration buttons.
     * Each button demonstrates a specific BlazeVideosInlinePlayer method.
     */
    private fun setupButtons() {
        
        // Prepare videos for the current data source
        // This pre-loads content for faster playback
        binding.btnPrepareVideos.setOnClickListener {
            BlazeVideosInlinePlayer.prepareVideos(
                containerId = "${CONTAINER_ID_PREFIX}_${System.currentTimeMillis()}",
                dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("inline-video-1"))
            )
        }
        
        // Embeds placeholder/thumbnail in the container
        // Can be called directly at any time
        binding.btnEmbedPlaceholder.setOnClickListener {
            inlinePlayer?.embedPlaceholder()
        }
        
        // Embeds the video player in the container
        // Can be called directly, will transition from placeholder if exists
        binding.btnEmbedPlayer.setOnClickListener {
            inlinePlayer?.embedPlayer(shouldAutoPlayOnStart = true)
        }
        
        // Resets from player back to placeholder state
        // Transitions from active player to placeholder/thumbnail
        binding.btnResetToPlaceholder.setOnClickListener {
            inlinePlayer?.resetToPlaceholder()
        }
        
        // Disposes entire container - removes all views and player
        // All player-related methods will have no effect after disposal
        binding.btnDispose.setOnClickListener {
            inlinePlayer?.disposeContainer()
        }
        
        // Enters fullscreen mode
        // Only works if player is embedded
        binding.btnEnterFullScreen.setOnClickListener {
            inlinePlayer?.enterFullScreen()
        }
        
        // Resumes video playback
        // Only works if player exists - overrides any programmatic pause
        binding.btnResumePlayer.setOnClickListener {
            inlinePlayer?.resumePlayer()
        }
        
        // Pauses video playback - programmatic pause
        // Only works if player exists - remains paused until explicitly resumed
        // Only resumePlayer() call or user interaction (if not blocked) can resume
        binding.btnPausePlayer.setOnClickListener {
            inlinePlayer?.pausePlayer()
        }
        
        // Blocks user interaction with the player
        // Only works if player exists - disables all touch events and user controls
        // Player state can only be changed programmatically when blocked
        binding.btnBlockInteraction.setOnClickListener {
            inlinePlayer?.blockInteraction()
        }
        
        // Unblocks user interaction with the player
        // Only works if player exists - re-enables touch events and user controls
        // User can now play/pause/seek through player interface
        binding.btnUnblockInteraction.setOnClickListener {
            inlinePlayer?.unblockInteraction()
        }
    }
    
    /**
     * Subscribes to volume change events from the shared ViewModel.
     * 
     * When the user presses volume keys in the activity, this inline player
     * will be notified of the volume change.
     */
    private fun subscribeToVolumeChanges() {
        lifecycleScope.launch {
            viewModel.onVolumeChangedEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                inlinePlayer?.onVolumeChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Always dispose the player when the view is destroyed to prevent memory leaks
        inlinePlayer?.disposeContainer()
        inlinePlayer = null
    }
}
