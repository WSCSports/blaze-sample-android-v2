package com.wscsports.blaze_sample_android.samples.widgets.widget_screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.widgets.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initWidgets()
        setRefreshListener()
    }

    private fun initWidgets() {
        initStoriesRowWidget()
        initMomentsRowWidget()
        initStoriesGridWidget()
    }

    private fun initStoriesRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("live-stories")
        )
        binding.storiesRowWidgetView.initWidget(
            widgetLayout = viewModel.storiesRowBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-stories-row-id",
            widgetDelegate = this
        )
    }

    private fun initMomentsRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("moments")
        )
        binding.momentsRowWidgetView.initWidget(
            widgetLayout = viewModel.momentsRowBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-moments-row-id",
            widgetDelegate = this
        )
    }

    private fun initStoriesGridWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel("top-stories")
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

}


