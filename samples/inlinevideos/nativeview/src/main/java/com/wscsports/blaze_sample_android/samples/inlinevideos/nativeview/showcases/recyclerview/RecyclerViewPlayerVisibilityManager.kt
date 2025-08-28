package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer

/**
 * Simple player visibility manager for RecyclerView demonstrations.
 * 
 * Manages multiple inline video players within a RecyclerView,
 * ensuring only one player is active at a time based on visibility and centeredness.
 * 
 * This is a simplified showcase implementation. Applications may implement
 * their own visibility logic based on specific requirements.
 */
class RecyclerViewPlayerVisibilityManager(
    private val recyclerView: RecyclerView
) {
    
    /**
     * Interface that ViewHolders must implement to work with this manager.
     * 
     * This provides a clean separation between the manager and adapter,
     * allowing the manager to access players without knowing adapter internals.
     */
    interface PlayerViewHolder {
        /**
         * Returns the inline player instance for this ViewHolder.
         * 
         * @return Player instance if created, null if not yet initialized
         */
        fun getPlayer(): BlazeVideosInlinePlayer?
    }
    private var activePosition = -1
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                checkAndActivatePlayer()
            }
        }
    }
    
    init {
        recyclerView.addOnScrollListener(scrollListener)
    }
    
    /**
     * Checks visibility and activates the most centered fully visible player.
     * Only fully visible items are considered for activation.
     */
    fun checkAndActivatePlayer() {
        var bestViewHolder: PlayerViewHolder? = null
        var bestCenteredness = 0f
        var newActivePosition = -1
        
        // Check all visible items
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val position = recyclerView.getChildAdapterPosition(child)
            val viewHolder = recyclerView.getChildViewHolder(child) as? PlayerViewHolder
            
            if (position != RecyclerView.NO_POSITION && viewHolder != null) {
                val visibility = calculateVisibility(child)
                if (visibility >= 1.0f) { // Only fully visible items
                    val centeredness = calculateCenteredness(child)
                    if (centeredness > bestCenteredness) {
                        bestCenteredness = centeredness
                        bestViewHolder = viewHolder
                        newActivePosition = position
                    }
                }
            }
        }
        
        // Switch active player if needed
        if (newActivePosition != activePosition) {
            resetCurrentActivePlayer()
            activatePlayer(bestViewHolder)
            activePosition = newActivePosition
        }
    }
    
    /**
     * Calculates visibility percentage of a view within the RecyclerView.
     */
    private fun calculateVisibility(child: View): Float {
        val recyclerBounds = android.graphics.Rect()
        recyclerView.getGlobalVisibleRect(recyclerBounds)
        
        val childBounds = android.graphics.Rect()
        child.getGlobalVisibleRect(childBounds)
        
        if (!android.graphics.Rect.intersects(recyclerBounds, childBounds)) {
            return 0f
        }
        
        val childHeight = child.height
        if (childHeight == 0) return 0f
        
        val visibleHeight = minOf(childBounds.bottom, recyclerBounds.bottom) - 
                           maxOf(childBounds.top, recyclerBounds.top)
        
        return (visibleHeight.toFloat() / childHeight.toFloat()).coerceIn(0f, 1f)
    }
    
    /**
     * Calculates how centered a view is within the RecyclerView (0-1).
     */
    private fun calculateCenteredness(child: View): Float {
        val recyclerCenter = recyclerView.height / 2f
        val childCenter = child.top + child.height / 2f
        val maxDistance = recyclerView.height / 2f
        val distance = kotlin.math.abs(recyclerCenter - childCenter)
        
        return (1f - (distance / maxDistance)).coerceIn(0f, 1f)
    }
    
    /**
     * Resets the currently active player to placeholder.
     */
    private fun resetCurrentActivePlayer() {
        // Find current active player in visible ViewHolders
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val position = recyclerView.getChildAdapterPosition(child)
            val viewHolder = recyclerView.getChildViewHolder(child) as? PlayerViewHolder
            
            if (position == activePosition && viewHolder != null) {
                viewHolder.getPlayer()?.resetToPlaceholder()
                break
            }
        }
    }
    
    /**
     * Activates the player in the specified ViewHolder.
     */
    private fun activatePlayer(viewHolder: PlayerViewHolder?) {
        viewHolder?.getPlayer()?.embedPlayer(shouldAutoPlayOnStart = true)
    }
    
    /**
     * Initializes the first player after list is loaded.
     * Call this after adapter data is set.
     */
    fun prepareInitialPlayer() {
        recyclerView.post {
            checkAndActivatePlayer()
        }
    }
    
    /**
     * Disposes the manager and removes scroll listener.
     * Call this when the fragment/activity is destroyed.
     */
    fun dispose() {
        recyclerView.removeOnScrollListener(scrollListener)
        activePosition = -1
    }
    
    /**
     * Pauses all visible players.
     * Useful for lifecycle management.
     */
    fun pauseAllPlayers() {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val viewHolder = recyclerView.getChildViewHolder(child) as? PlayerViewHolder
            viewHolder?.getPlayer()?.pausePlayer()
        }
    }
    
    /**
     * Notifies all visible inline video players of volume changes.
     * 
     * Updates all visible inline video players with the current volume state.
     * Useful for system volume changes or app-level mute/unmute.
     */
    fun notifyVolumeChanged() {
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val viewHolder = recyclerView.getChildViewHolder(child) as? PlayerViewHolder
            viewHolder?.getPlayer()?.onVolumeChanged()
        }
    }
}
