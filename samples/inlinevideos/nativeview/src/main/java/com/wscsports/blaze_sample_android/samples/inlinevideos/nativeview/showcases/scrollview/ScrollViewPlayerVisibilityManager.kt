package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.scrollview

import android.view.View
import androidx.core.widget.NestedScrollView
import com.blaze.blazesdk.features.videos.inline.BlazeVideosInlinePlayer

/**
 * ScrollView visibility manager for inline video players.
 * 
 * This is a simplified sample implementation inspired by the SDK's official
 * BlazeInlinePlayerScrollViewVisibilityManager. It demonstrates the core concepts
 * of managing multiple inline players with automatic visibility-based switching.
 * 
 * Key features demonstrated:
 * - Debounced scroll detection (300ms stabilization)
 * - Immediate placeholder conversion when visibility drops below 50%
 * - Grace period protection during initial setup
 * - Proper disposal order and resource management
 * 
 * Note: For production use, consider the official SDK manager when available.
 */
class ScrollViewPlayerVisibilityManager(
    private val scrollView: NestedScrollView
) {
    
    data class RegisteredPlayer(
        val containerView: View,
        val player: BlazeVideosInlinePlayer
    )
    
    private val registeredPlayers = mutableListOf<RegisteredPlayer>()
    private var currentActivePlayer: BlazeVideosInlinePlayer? = null
    
    // Scroll detection variables
    private var scrollIdleTime = 0L
    private val STABLE_DURATION_MS = 300L // Wait 300ms after scroll stops
    private var lastScrollPosition = 0
    
    // Initial setup protection
    private var isInitialSetupPhase = true
    private val INITIAL_SETUP_GRACE_PERIOD_MS = 500L // 500ms grace period
    private var setupStartTime = System.currentTimeMillis()
    
    /**
     * Registers a player to be managed by this visibility manager.
     * 
     * The player will be set to placeholder state initially and the manager
     * will automatically determine which player should be active based on visibility.
     */
    fun registerPlayer(position: Int, player: BlazeVideosInlinePlayer, container: View) {
        val registeredPlayer = RegisteredPlayer(container, player)
        
        // Avoid duplicate registrations
        if (registeredPlayers.any { it.player == player }) {
            return
        }
        
        registeredPlayers.add(registeredPlayer)
        
        // Set to placeholder initially
        player.embedPlaceholder()
    }
    
    /**
     * Sets up scroll listener for visibility management:
     * - Grace period protection during initial setup
     * - Immediate placeholder conversion when visibility drops
     * - Debounced activation after scroll stabilizes
     */
    private fun setupScrollListener() {
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            handleScrollChange(scrollY)
        }
    }
    
    /**
     * Handles scroll changes with optimized approach:
     * 1. Grace period protection during initial setup
     * 2. Immediate placeholder conversion for active player if needed
     * 3. Debounced activation after scroll stabilizes
     */
    private fun handleScrollChange(scrollY: Int) {
        // PROTECTION: Ignore scroll events during initial setup grace period
        if (isInitialSetupPhase) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - setupStartTime < INITIAL_SETUP_GRACE_PERIOD_MS) {
                return // Ignore scroll events during grace period
            } else {
                isInitialSetupPhase = false // Grace period ended
            }
        }
        
        // IMMEDIATE: Check if current active player should be converted to placeholder
        checkAndConvertActivePlayerToPlaceholderIfNeeded()
        
        // DEBOUNCED: Standard scroll handling for finding new active player
        lastScrollPosition = scrollY
        scrollIdleTime = System.currentTimeMillis()
        
        // Schedule evaluation after scroll stabilizes
        scrollView.postDelayed({
            val currentTime = System.currentTimeMillis()
            // Only proceed if we've been idle for the required duration
            if (currentTime - scrollIdleTime >= STABLE_DURATION_MS && 
                scrollView.scrollY == lastScrollPosition) {
                updateActivePlayer(shouldStartPlayback = true)
            }
        }, STABLE_DURATION_MS)
    }
    
    /**
     * Immediately checks if the current active player has dropped below 50% visibility
     * and converts it to placeholder without any delay.
     */
    private fun checkAndConvertActivePlayerToPlaceholderIfNeeded() {
        val activePlayer = currentActivePlayer ?: return
        
        // Find the container for the current active player
        val activePlayerContainer = registeredPlayers.find { it.player == activePlayer }?.containerView ?: return
        
        // Calculate current visibility
        val currentVisibility = calculateVisibility(activePlayerContainer)
        
        // If visibility dropped below 50%, immediately convert to placeholder
        if (currentVisibility < 0.5f) {
            activePlayer.embedPlaceholder()
            currentActivePlayer = null
        }
    }
    
    /**
     * Updates which player should be active based on current visibility.
     * 
     * Algorithm:
     * 1. Calculate visibility percentage for each registered player
     * 2. Find the player with highest visibility (must be > 50%)
     * 3. If different from current active player, switch players
     * 4. If same player and shouldStartPlayback=true, resume it
     */
    private fun updateActivePlayer(shouldStartPlayback: Boolean) {
        val mostVisiblePlayer = findMostVisiblePlayer()
        
        if (mostVisiblePlayer != currentActivePlayer) {
            // Reset current active player to placeholder
            currentActivePlayer?.embedPlaceholder()
            
            // Set new active player
            currentActivePlayer = mostVisiblePlayer
            mostVisiblePlayer?.embedPlayer(shouldAutoPlayOnStart = shouldStartPlayback)
            
        } else if (shouldStartPlayback && mostVisiblePlayer != null) {
            // Same player is active but we want to ensure it's playing
            mostVisiblePlayer.resumePlayer()
        }
    }
    
    /**
     * Finds the most visible player based on visibility calculations.
     * 
     * Visibility Criteria:
     * - Player must be at least 50% visible to be considered
     * - Among eligible players, the one with highest visibility percentage wins
     * - If no player meets the criteria, returns null (no active player)
     */
    private fun findMostVisiblePlayer(): BlazeVideosInlinePlayer? {
        var bestPlayer: BlazeVideosInlinePlayer? = null
        var bestVisibility = 0.5f // Minimum 50% visibility required
        
        registeredPlayers.forEach { registeredPlayer ->
            val visibility = calculateVisibility(registeredPlayer.containerView)
            if (visibility > bestVisibility) {
                bestVisibility = visibility
                bestPlayer = registeredPlayer.player
            }
        }
        
        return bestPlayer
    }
    
    /**
     * Public method to manually trigger active player evaluation.
     * Useful after layout changes or programmatic scrolling.
     */
    fun checkAndActivatePlayer() {
        updateActivePlayer(shouldStartPlayback = true)
    }
    
    /**
     * Calculates visibility percentage of a view within the scroll container.
     * Optimized to avoid expensive getGlobalVisibleRect calls.
     */
    private fun calculateVisibility(view: View): Float {
        val viewHeight = view.height
        if (viewHeight == 0) return 0f
        
        // Get scroll position and container height
        val scrollY = scrollView.scrollY
        val containerHeight = scrollView.height
        
        // Calculate view position relative to ScrollView
        val viewTop = view.top
        val viewBottom = view.bottom
        
        // Calculate visible area
        val visibleTop = maxOf(viewTop - scrollY, 0)
        val visibleBottom = minOf(viewBottom - scrollY, containerHeight)
        
        if (visibleBottom <= visibleTop) return 0f
        
        val visibleHeight = visibleBottom - visibleTop
        return (visibleHeight.toFloat() / viewHeight.toFloat()).coerceIn(0f, 1f)
    }
    
    /**
     * Initializes the manager and sets up scroll listener.
     * Call this after registering players.
     */
    fun initialize() {
        setupScrollListener()
    }
    
    /**
     * Disposes all players and clears resources.
     * 
     * Disposal Order:
     * 1. All players are disposed first (in registration order)
     * 2. All players are removed from management
     * 3. Active player reference is cleared
     */
    fun dispose() {
        // IMPORTANT: Dispose all players first, then clear management
        registeredPlayers.forEach { registeredPlayer ->
            registeredPlayer.player.disposeContainer()
        }
        
        // Clear management after disposal
        registeredPlayers.clear()
        currentActivePlayer = null
    }
    
    /**
     * Pauses all active players if they exist.
     * 
     * Behavior:
     * - Only affects players that are currently embedded (not placeholders)
     * - Does not change which player is active
     * - Useful for app backgrounding or temporary pause scenarios
     */
    fun pauseAllPlayers() {
        registeredPlayers.forEach { registeredPlayer ->
            registeredPlayer.player.pausePlayer()
        }
    }
    
    /**
     * Notifies all inline video players of volume changes.
     * 
     * Updates all registered inline video players with the current volume state.
     * Useful for system volume changes or app-level mute/unmute.
     */
    fun notifyVolumeChanged() {
        registeredPlayers.forEach { registeredPlayer ->
            registeredPlayer.player.onVolumeChanged()
        }
    }
}
