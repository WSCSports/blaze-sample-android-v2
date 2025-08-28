package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.tabs.BlazeMomentsPlayerContainerTabs
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.style.players.BlazePlayerButtonCustomImageStates
import com.blaze.blazesdk.style.players.tabs.BlazePlayerTabsStyle
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.MomentsContainerTabsDelegateImpl
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_CONTAINER_TABS_ID_1
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_CONTAINER_TABS_ID_2
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_CONTAINER_TABS_SOURCE_ID
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentMomentsContainerTabsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch

/**
 * This fragment demonstrates how to use BlazeMomentsPlayerContainerTabs.
 * It shows multiple moment containers with tabs for easy navigation between different content.
 * For more information, see https://dev.wsc-sports.com/docs/android-moments-player#/.
 */
class MomentsContainerTabsFragment : Fragment(R.layout.fragment_moments_container_tabs) {

    private val binding by viewBinding(FragmentMomentsContainerTabsBinding::bind)
    private val viewModel: MomentsContainerViewModel by activityViewModels()

    private val momentsPlayerContainerTabs: BlazeMomentsPlayerContainerTabs by lazy {
        BlazeMomentsPlayerContainerTabs(
            containerSourceId = MOMENTS_CONTAINER_TABS_SOURCE_ID,
            containerTabsDelegate = MomentsContainerTabsDelegateImpl(),
            tabs = listOf(
                BlazeMomentsContainerTabItem(
                    containerId = MOMENTS_CONTAINER_TABS_ID_1,
                    title = "Trending",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL)),
                    icon = BlazePlayerButtonCustomImageStates(
                        imageSelectedPathResId = com.wscsports.blaze_sample_android.core.ui.R.drawable.ic_tabs_trending,
                        imageUnselectedPathResId = com.wscsports.blaze_sample_android.core.ui.R.drawable.ic_tabs_trending,
                    )
                ),
                BlazeMomentsContainerTabItem(
                    containerId = MOMENTS_CONTAINER_TABS_ID_2,
                    title = "For You",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)),
                    icon = BlazePlayerButtonCustomImageStates(
                        imageSelectedPathResId = com.wscsports.blaze_sample_android.core.ui.R.drawable.ic_tabs_for_you,
                        imageUnselectedPathResId = com.wscsports.blaze_sample_android.core.ui.R.drawable.ic_tabs_for_you,
                    )
                ),
            ),
            containerTabsView = binding.momentsTabsContainer,
            playerStyle = viewModel.momentsPlayerStyle,
            tabsStyle = BlazePlayerTabsStyle.base(),
            storeOwner = this,
            lifecycleOwner = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMomentsTabsContainer()
        subscribeObservers()
    }

    private fun startMomentsTabsContainer() {
        momentsPlayerContainerTabs.startPlaying()
    }

    // Observing user increasing/decreasing volume from the activity, and updating the SDK
    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.onVolumeChangedEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                momentsPlayerContainerTabs.onVolumeChanged()
            }
        }
    }

}
