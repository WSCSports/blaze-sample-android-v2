package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideoDataGenerator
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview.RecyclerViewPlayerAdapter
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.FragmentRecyclerviewShowcaseBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.showcases.recyclerview.RecyclerViewPlayerVisibilityManager

/**
 * Demonstrates inline video players within a RecyclerView with automatic player management.
 * 
 * This showcase implements a "one active player" mechanism where only the most centered,
 * fully visible player is embedded and active at any time. As the user scrolls, players
 * automatically switch between placeholder and active states based on position and visibility.
 * 
 * Key features demonstrated:
 * - Multiple inline players in a scrollable list
 * - Centeredness-based player selection (most centered item wins)
 * - Only fully visible items are considered for activation
 * - Memory-efficient player management with recycling
 * - Smooth transitions between placeholder and player states
 * 
 * This implementation provides a foundation that can be adapted for various use cases
 * and customized based on specific application requirements.
 */
class RecyclerViewShowcaseFragment : Fragment() {
    
    private var _binding: FragmentRecyclerviewShowcaseBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: InlineVideosViewModel by activityViewModels()
    private var playerVisibilityManager: RecyclerViewPlayerVisibilityManager? = null
    private var adapter: RecyclerViewPlayerAdapter? = null
    
    companion object {
        // For the sake of this showcase, using 8 items to demonstrate scrolling behavior
        private const val ITEM_COUNT = 8
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerviewShowcaseBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPlayerVisibilityManager()
        initializeVideoFeed()
        subscribeToVolumeChanges()
    }
    
    /**
     * Configures the RecyclerView with layout manager and adapter.
     */
    private fun setupRecyclerView() {
        adapter = RecyclerViewPlayerAdapter(this)
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@RecyclerViewShowcaseFragment.adapter
        }
    }
    
    /**
     * Initializes the player visibility manager for handling visibility and player switching.
     */
    private fun setupPlayerVisibilityManager() {
        playerVisibilityManager = RecyclerViewPlayerVisibilityManager(binding.recyclerView)
    }
    
    /**
     * Initializes the video feed with sample data and activates the first player.
     * 
     * Generates sample video items and submits them to the adapter. Once the list
     * is loaded and laid out, activates the most centered visible player.
     */
    private fun initializeVideoFeed() {
        val videoItems = InlineVideoDataGenerator.generateVideoItems(ITEM_COUNT)
        
        adapter?.submitList(videoItems) {
            // Wait for RecyclerView layout completion before checking player visibility
            // This ensures accurate visibility calculations for initial player activation
            binding.recyclerView.postDelayed({
                playerVisibilityManager?.prepareInitialPlayer()
            }, 300L)
        }
    }
    
    /**
     * Subscribes to volume change events from the shared ViewModel.
     * 
     * When the user presses volume keys in the activity, all visible inline players
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
        // Dispose all inline players and cleanup manager resources
        playerVisibilityManager?.dispose()
        
        playerVisibilityManager = null
        adapter = null
        _binding = null
    }
}
