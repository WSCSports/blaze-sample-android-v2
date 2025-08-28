package com.wscsports.blaze_sample_android.samples.inlinevideos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.inlinevideos.databinding.ActivityInlineVideosBinding

/**
 * Main activity for the Inline Videos sample module.
 * 
 * This activity serves as an entry point for demonstrating different implementations
 * of inline video functionality within the Blaze SDK. It provides two main approaches:
 * 
 * 1. **Native View Implementation**: Traditional Android Views approach using:
 *    - Interactive inline videos with various method buttons
 *    - ScrollView with manager for single active player
 *    - RecyclerView with manager for multiple videos
 * 
 * 2. **Compose View Implementation**: Modern Jetpack Compose approach featuring:
 *    - Interactive inline videos with method demonstrations
 *    - LazyColumn for efficient scrolling
 *    - Pagination examples with counters
 * 
 * Each implementation showcases best practices for integrating inline video players
 * in different UI frameworks while maintaining optimal performance and user experience.
 */
class InlineVideosActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityInlineVideosBinding::inflate)
    private var inlineVideosAdapter: InlineVideosListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        initRecyclerView()
    }

    /**
     * Sets up the app bar with title and back navigation.
     */
    private fun setupAppbar() {
        binding.appbar.setupView("Inline Videos") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Initializes the RecyclerView with adapter and data.
     * Each item represents a different implementation approach for inline videos.
     */
    private fun initRecyclerView() {
        inlineVideosAdapter = InlineVideosListAdapter { inlineVideosType ->
            navigateToImplementation(inlineVideosType)
        }
        binding.inlineVideosRecyclerView.adapter = inlineVideosAdapter
        inlineVideosAdapter?.submitList(InlineVideosType.entries)
    }

    /**
     * Navigates to the selected inline videos implementation.
     * 
     * @param inlineVideosType The type of implementation to navigate to
     */
    private fun navigateToImplementation(inlineVideosType: InlineVideosType) {
        val intent = Intent().apply {
            setClassName(
                this@InlineVideosActivity,
                "${ROOT_INLINE_VIDEOS_PKG_NAME}${inlineVideosType.classPathSuffix}"
            )
        }
        startActivity(intent)
    }

    companion object {
        private const val ROOT_INLINE_VIDEOS_PKG_NAME = "com.wscsports.blaze_sample_android.samples.inlinevideos."
    }
}
