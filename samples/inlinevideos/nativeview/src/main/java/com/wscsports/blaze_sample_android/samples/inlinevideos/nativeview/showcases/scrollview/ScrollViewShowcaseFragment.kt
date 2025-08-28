package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.scrollview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.blaze.blazesdk.ads.models.ui.BlazeVideosAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlinePreviewPlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideoDataGenerator
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosDelegateImpl
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.FragmentScrollviewShowcaseBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.ItemRecyclerviewPlayerBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.scrollview.ScrollViewPlayerVisibilityManager

/**
 * Demonstrates inline video players within a ScrollView with automatic player management.
 * 
 * This showcase implements a "one active player" mechanism where only the most visible
 * player is embedded and active at any time. As the user scrolls, players automatically
 * switch between placeholder and active states based on visibility.
 * 
 * Key features demonstrated:
 * - Multiple inline players in a scroll container
 * - Automatic visibility-based player switching
 * - Memory-efficient player management
 * - Smooth transitions between placeholder and player states
 * 
 * This implementation provides a foundation that can be adapted for various use cases
 * and customized based on specific application requirements.
 */
class ScrollViewShowcaseFragment : Fragment() {
    
    private var _binding: FragmentScrollviewShowcaseBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InlineVideosViewModel by activityViewModels()
    private var playerVisibilityManager: ScrollViewPlayerVisibilityManager? = null
    
    companion object {
        private const val CONTAINER_ID_PREFIX = "scrollview_player"
        // For the sake of this showcase, using 5 items to demonstrate scrolling behavior  
        private const val ITEM_COUNT = 5
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrollviewShowcaseBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPlayerVisibilityManager()
        createPlayerContainers()
        initializePlayerVisibilityManager()
        subscribeToVolumeChanges()
    }
    
    /**
     * Creates the player visibility manager for handling visibility and player switching.
     * 
     * The manager is needed first to register players during container creation.
     */
    private fun setupPlayerVisibilityManager() {
        playerVisibilityManager = ScrollViewPlayerVisibilityManager(binding.scrollView)
    }
    
    /**
     * Creates multiple player containers programmatically for showcase purposes.
     * Each player is configured with Preview mode for optimal scrolling experience.
     * In production, video data would typically come from your backend/API.
     */
    private fun createPlayerContainers() {
        val linearLayout = binding.scrollView.getChildAt(0) as ViewGroup
        val videoItems = InlineVideoDataGenerator.generateVideoItems(ITEM_COUNT)
        
        videoItems.forEachIndexed { index, videoItem ->
            // Create container for each player
            val containerView = createPlayerContainer(index, videoItem.title, videoItem.description)
            linearLayout.addView(containerView)
            
            // Create and setup inline player
            // Using Preview mode for scrolling scenarios
            val player = BlazeVideosInlinePlayer(
                lifecycleOwner = this,
                storeOwner = this,
                containerView = (containerView.tag as ItemRecyclerviewPlayerBinding).playerContainer,
                dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(videoItem.label)),
                containerId = "${CONTAINER_ID_PREFIX}_$index",
                playerDelegate = InlineVideosDelegateImpl(),
                videosAdsConfigType = BlazeVideosAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG,
                playerMode = BlazeVideosInlinePlayer.PlayerMode.Preview(
                    previewPlayerStyle = BlazeVideosInlinePreviewPlayerStyle.base(),
                    fullScreenPlayerStyle = BlazeVideosPlayerStyle.base()
                )
            )
            
            playerVisibilityManager?.registerPlayer(index, player, containerView)
        }
    }
    
    /**
     * Creates a container view for each player with title, description and player area.
     * 
     * Reuses the same layout as RecyclerView items for consistency and shares ViewBinding.
     */
    private fun createPlayerContainer(index: Int, title: String, description: String): View {
        // Using same layout and ViewBinding as RecyclerView for consistency
        val binding = ItemRecyclerviewPlayerBinding.inflate(
            LayoutInflater.from(requireContext()), null, false
        )
        
        // Bind data using ViewBinding for type safety
        binding.title.text = title
        binding.description.text = description
        
        // Ensure margin is applied when added to LinearLayout in ScrollView
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = (24 * resources.displayMetrics.density).toInt() // 24dp
        binding.root.layoutParams = layoutParams
        
        // Store binding as tag for later access to playerContainer
        binding.root.tag = binding
        
        return binding.root
    }
    
    /**
     * Initializes the player visibility manager after all players are registered.
     * 
     * For showcase purposes, we create players programmatically, so we initialize
     * the manager only after all players have been registered to ensure proper
     * scroll event handling and visibility management.
     */
    private fun initializePlayerVisibilityManager() {
        playerVisibilityManager?.initialize()
    }
    
    /**
     * Subscribes to volume change events from the shared ViewModel.
     * 
     * When the user presses volume keys in the activity, all inline players
     * in this showcase will be notified of the volume change.
     */
    private fun subscribeToVolumeChanges() {
        lifecycleScope.launch {
            viewModel.onVolumeChangedEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                playerVisibilityManager?.notifyVolumeChanged()
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        // Pause all players when fragment goes to background
        playerVisibilityManager?.pauseAllPlayers()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up all players and manager resources
        playerVisibilityManager?.dispose()
        playerVisibilityManager = null
        _binding = null
    }
}
