package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blaze.blazesdk.ads.models.ui.BlazeVideosAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer
import com.blaze.blazesdk.style.players.videos.BlazeVideosInlinePreviewPlayerStyle
import com.blaze.blazesdk.style.players.videos.BlazeVideosPlayerStyle
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideoDataGenerator
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosDelegateImpl
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.ItemRecyclerviewPlayerBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview.RecyclerViewPlayerVisibilityManager

/**
 * Adapter for RecyclerView with inline video players.
 * 
 * Demonstrates proper lifecycle management for video players in RecyclerView:
 * - Players are created lazily on first bind
 * - ViewHolders handle attach/detach lifecycle for optimal performance
 * - Players are properly disposed when ViewHolders are recycled
 * 
 * Each item contains a video player configured in Preview mode for optimal scrolling.
 */
class RecyclerViewPlayerAdapter(
    private val fragment: Fragment
) : ListAdapter<InlineVideoDataGenerator.InlineVideoItem, RecyclerViewPlayerAdapter.PlayerViewHolder>(DiffCallback) {
    
    companion object {
        // Prefix for generating unique container IDs - critical for proper player management
        private const val CONTAINER_ID_PREFIX = "recyclerview_player"
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        // Using ViewBinding for type-safe view access instead of findViewById
        val binding = ItemRecyclerviewPlayerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlayerViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }
    
    /**
     * Called when ViewHolder is attached to window (becomes visible).
     * The manager will handle visibility detection automatically.
     */
    override fun onViewAttachedToWindow(holder: PlayerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }
    
    /**
     * Called when ViewHolder is detached from window (goes off-screen).
     * 
     * CRITICAL: Reset to placeholder to ensure no active player exists off-screen.
     * This prevents video playback when items are not visible and optimizes memory usage.
     */
    override fun onViewDetachedFromWindow(holder: PlayerViewHolder) {
        holder.onViewDetachedFromWindow()
        super.onViewDetachedFromWindow(holder)
    }
    
    /**
     * Called when ViewHolder is about to be recycled (RecyclerView determines it's no longer needed).
     * 
     * CRITICAL: Completely dispose of player resources to prevent:
     * - Memory leaks
     * - View state pollution when ViewHolder is reused
     * - Background video playback from recycled views
     */
    override fun onViewRecycled(holder: PlayerViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }
    

    /**
     * ViewHolder for RecyclerView items containing inline video players.
     * 
     * Implements PlayerViewHolder interface to work with RecyclerViewPlayerVisibilityManager
     * for automatic player switching based on visibility and centeredness.
     */
    inner class PlayerViewHolder(
        private val binding: ItemRecyclerviewPlayerBinding
    ) : RecyclerView.ViewHolder(binding.root), RecyclerViewPlayerVisibilityManager.PlayerViewHolder {
        
        // Player instance owned by this ViewHolder - created once and reused
        private var player: BlazeVideosInlinePlayer? = null
        
        // Required by RecyclerViewPlayerVisibilityManager.PlayerViewHolder interface
        // Provides access to this ViewHolder's player for visibility management
        override fun getPlayer(): BlazeVideosInlinePlayer? = player
        
        /**
         * Binds data to this ViewHolder and creates player if needed.
         * 
         * Players are created lazily on first bind and reused for the ViewHolder's lifetime.
         * This approach works well with RecyclerView's recycling mechanism.
         */
        fun bind(item: InlineVideoDataGenerator.InlineVideoItem, position: Int) {
            // Bind data to views using ViewBinding for type safety
            binding.title.text = item.title
            binding.description.text = item.description
            
            // Create player if not exists (first time binding)
            // Using Preview mode for RecyclerView scenarios
            if (player == null) {
                player = BlazeVideosInlinePlayer(
                    lifecycleOwner = fragment,
                    storeOwner = fragment,
                    containerView = binding.playerContainer, // ViewBinding provides direct access
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(item.label)),
                    containerId = "${CONTAINER_ID_PREFIX}_${item.id}", // Unique ID using item's actual ID
                    playerDelegate = InlineVideosDelegateImpl(),
                    videosAdsConfigType = BlazeVideosAdsConfigType.FIRST_AVAILABLE_ADS_CONFIG,
                    playerMode = BlazeVideosInlinePlayer.PlayerMode.Preview(
                        previewPlayerStyle = BlazeVideosInlinePreviewPlayerStyle.base(),
                        fullScreenPlayerStyle = BlazeVideosPlayerStyle.base()
                    )
                )
                
                // Start with placeholder - manager will switch to active player based on visibility
                player?.embedPlaceholder()
            }
        }
        
        /**
         * Called when this ViewHolder becomes visible (attached to window).
         * 
         * The visibility manager will automatically detect and manage player activation,
         * so no specific action needed here. This is called for reference and can be
         * extended for custom visibility handling if needed.
         */
        fun onViewAttachedToWindow() {
            // Manager handles visibility detection automatically
            // Custom logic can be added here if needed for specific use cases
        }
        
        /**
         * Called when this ViewHolder goes off-screen (detached from window).
         * 
         * CRITICAL IMPLEMENTATION: Reset to placeholder to ensure no video plays off-screen.
         * This is essential for:
         * - Performance optimization (no background video processing)
         * - Battery life (no unnecessary video decoding)
         * - User experience (only visible videos should play)
         */
        fun onViewDetachedFromWindow() {
            // Reset to placeholder when item is no longer visible
            // This ensures no video playback happens off-screen
            player?.embedPlaceholder()
        }
        
        /**
         * Called when this ViewHolder is about to be recycled by RecyclerView.
         * 
         * CRITICAL IMPLEMENTATION: Completely dispose of player to prevent:
         * - Memory leaks from undisposed video players
         * - View state pollution when ViewHolder is reused for different content
         * - Background video playback from orphaned players
         * 
         * After disposal, the player will be recreated on next bind() if needed.
         */
        fun onViewRecycled() {
            // CRITICAL: Dispose player completely to prevent memory leaks and view pollution
            player?.disposeContainer()
            player = null
        }
    }
    
    /**
     * DiffUtil callback for efficient list updates.
     * 
     * Uses item ID for identity comparison and full object equality for content comparison.
     * This ensures RecyclerView only redraws items that actually changed.
     */
    private object DiffCallback : DiffUtil.ItemCallback<InlineVideoDataGenerator.InlineVideoItem>() {
        // Items are the same if they have the same unique ID
        override fun areItemsTheSame(
            oldItem: InlineVideoDataGenerator.InlineVideoItem,
            newItem: InlineVideoDataGenerator.InlineVideoItem
        ): Boolean = oldItem.id == newItem.id
        
        // Contents are the same if all properties match (using data class equality)
        override fun areContentsTheSame(
            oldItem: InlineVideoDataGenerator.InlineVideoItem,
            newItem: InlineVideoDataGenerator.InlineVideoItem
        ): Boolean = oldItem == newItem
    }
}
