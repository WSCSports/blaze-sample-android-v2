package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosViewModel
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.ActivityInlineVideosNativeBinding

/**
 * Native Android Views implementation of inline videos.
 * 
 * This activity demonstrates the traditional Android View-based approach for implementing
 * inline video functionality using the Blaze SDK. It includes:
 * 
 * 1. **Interactive Inline Videos**: Shows different methods and configurations available
 *    - Various player initialization methods
 *    - Configuration options demonstration
 *    - Lifecycle management examples
 * 
 * 2. **ScrollView Implementation**: Demonstrates inline videos in a scrollable container
 *    - Single active player at a time using manager
 *    - Proper video lifecycle handling during scroll
 *    - Memory optimization patterns
 * 
 * 3. **RecyclerView Implementation**: Shows inline videos in a recycler view
 *    - Efficient view recycling with video players
 *    - Manager pattern for controlling playback
 *    - Performance optimization techniques
 * 
 * Key Features:
 * - BlazeVideosInlinePlayer integration
 * - Proper disposal and lifecycle management
 * - Manager pattern for multiple players
 * - Fragment-based navigation between different examples
 */
class InlineVideosNativeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityInlineVideosNativeBinding::inflate)
    private val viewModel: InlineVideosViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        setupNavController()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Native View Implementation") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupNavController() {
        navController = findNavController(R.id.nav_host_fragment_inline_videos)
        
        // Update header title when navigating between fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val title = destination.label?.toString() ?: "Native View Implementation"
            binding.appbar.setTitles(title)
        }
    }

    /**
     * Handles volume key events to notify all inline video players.
     * 
     * When the user presses volume up/down keys, this propagates the volume
     * change to all active inline players in the current showcase.
     */
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP -> {
                viewModel.setOnVolumeChangedEvent()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
