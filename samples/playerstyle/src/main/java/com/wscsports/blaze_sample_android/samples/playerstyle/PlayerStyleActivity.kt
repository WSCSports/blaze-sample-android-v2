package com.wscsports.blaze_sample_android.samples.playerstyle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.wscsports.android.blaze.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.android.blaze.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.playerstyle.databinding.ActivityPlayerStyleBinding

/**
 * This activity demonstrates how to use Blaze widgets to display stories and moments with
 * different player styles, default style and custom style.
 * For more information, see:
 * https://dev.wsc-sports.com/docs/android-story-player-customizations#/
 * https://dev.wsc-sports.com/docs/android-moments-player-customizations#/.
 */
class PlayerStyleActivity : AppCompatActivity(),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(ActivityPlayerStyleBinding::inflate)
    private val viewModel: PlayerStyleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        initWidgets()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Player Style") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initWidgets() {
        initDefaultStoriesRowWidget()
        initCustomStoriesRowWidget()
        initDefaultMomentsRowWidget()
        initCustomMomentsRowWidget()
    }

    private fun initDefaultStoriesRowWidget() {
        binding.defaultStoriesRowWidgetView.initWidget(
            widgetLayout = viewModel.storiesWidgetLayout,
            playerStyle = viewModel.defaultStoryPlayerStyle,
            dataSource = viewModel.storiesDataSource,
            widgetId = "default-stories-row-id",
            widgetDelegate = this
        )
    }

    private fun initCustomStoriesRowWidget() {
        binding.customStoriesRowWidgetView.initWidget(
            widgetLayout = viewModel.storiesWidgetLayout,
            playerStyle = viewModel.customStoryPlayerStyle,
            dataSource = viewModel.storiesDataSource,
            widgetId = "custom-stories-row-id",
            widgetDelegate = this
        )
    }

    private fun initDefaultMomentsRowWidget() {
        binding.defaultMomentsRowWidgetView.initWidget(
            widgetLayout = viewModel.momentsWidgetLayout,
            playerStyle = viewModel.defaultMomentPlayerStyle,
            dataSource = viewModel.momentsDataSource,
            widgetId = "default-moments-row-id",
            widgetDelegate = this
        )
    }

    private fun initCustomMomentsRowWidget() {
        binding.customMomentsRowWidgetView.initWidget(
            widgetLayout =  viewModel.momentsWidgetLayout,
            playerStyle = viewModel.customMomentPlayerStyle,
            dataSource = viewModel.momentsDataSource,
            widgetId = "custom-moments-row-id",
            widgetDelegate = this
        )
    }


}