package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsConfiguration
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.MomentsContainerTabsDelegateImpl
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetsViewModel
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentMixedWidgetsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * MixedWidgetsFragment is a Fragment that displays a mix feed of Blaze widgets:
 * Stories-row, Moments-row, and Stories-grid.
 * It manages reload widgets data with pull-to-refresh [SwipeRefreshLayout].
 */
class MixedWidgetsFragment: Fragment(R.layout.fragment_mixed_widgets),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(FragmentMixedWidgetsBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()

    // Use this controller for any operations on the Moments Widget Tabs
    private val momentsTabsController = BlazeMomentsWidgetTabsController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initWidgets()
        setRefreshListener()
    }

    private fun initWidgets() {
        initStoriesRowWidget()
        initMomentsTabsRowWidget()
        initStoriesGridWidget()
    }

    private fun initStoriesRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )
        binding.storiesRowWidgetView.initWidget(
            widgetLayout = viewModel.storiesRowBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-stories-row-id",
            widgetDelegate = this
        )
    }

    private fun initMomentsTabsRowWidget() {
        val tabsConfig = BlazeMomentsWidgetTabsConfiguration(
            containerSourceId = "mixed-widgets-moments-tabs-id",
            tabs = listOf(
                BlazeMomentsContainerTabItem(
                    containerId = "tab-1",
                    title = "Trending",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL)),
                ),
                BlazeMomentsContainerTabItem(
                    containerId = "tab-2",
                    title = "For You",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)),
                ),
            ),
            containerTabsDelegate = MomentsContainerTabsDelegateImpl(),
            controller = momentsTabsController,
        )
        binding.momentsRowWidgetView.initWidget(
            widgetLayout = viewModel.momentsRowBaseLayout,
            tabsConfiguration = tabsConfig,
            widgetId = "mixed-widgets-moments-tabs-row-id",
            widgetDelegate = this,
        )
    }

    private fun initStoriesGridWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )
        binding.storiesGridWidgetView.initWidget(
            widgetLayout = viewModel.storiesGridBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-stories-grid-id",
            widgetDelegate = this
        )
    }

    private fun setRefreshListener() {
        binding.mixedWidgetsPullToRefresh.setOnRefreshListener {
            updateDataSource()
            binding.mixedWidgetsPullToRefresh.isRefreshing = false
        }
    }

    // For more information refer to https://dev.wsc-sports.com/docs/android-widgets#/reloaddata
    private fun updateDataSource() {
        with(binding) {
            storiesRowWidgetView.reloadData(isSilentRefresh = false)
            momentsRowWidgetView.reloadData(isSilentRefresh = false)
            storiesGridWidgetView.reloadData(isSilentRefresh = false)
        }
    }

    companion object {
        private const val TAG = "MixedWidgetsFragment"
    }

}


