package com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wscsports.blaze_sample_android.samples.inlinevideos.InlineVideosListAdapter
import com.wscsports.blaze_sample_android.samples.inlinevideos.nativeview.databinding.FragmentInlineVideoListBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * Fragment that displays a list of inline video implementation examples.
 * 
 * This fragment serves as the main navigation hub for exploring different
 * approaches to implementing inline video players with the Blaze SDK.
 * 
 * **For Clients:**
 * Each option demonstrates a specific use case and implementation pattern.
 * Study the examples to understand which approach best fits your requirements:
 * 
 * - **Interactive**: For testing and configuration exploration
 * - **ScrollView**: For simple lists with single active player
 * - **RecyclerView**: For large datasets with efficient recycling
 */
class InlineVideoListFragment : Fragment(R.layout.fragment_inline_video_list) {

    private val binding by viewBinding(FragmentInlineVideoListBinding::bind)
    private var inlineVideoAdapter: InlineVideoListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    /**
     * Initializes the RecyclerView with the inline video implementation options.
     * Each item navigates to a specific implementation example.
     */
    private fun initRecyclerView() {
        inlineVideoAdapter = InlineVideoListAdapter { screenType ->
            findNavController().navigate(screenType.navDestinationId)
        }
        binding.recyclerView.adapter = inlineVideoAdapter
        inlineVideoAdapter?.submitList(InlineVideoScreenType.entries)
    }
}
