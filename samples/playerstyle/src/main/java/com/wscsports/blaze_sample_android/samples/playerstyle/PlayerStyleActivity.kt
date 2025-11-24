package com.wscsports.blaze_sample_android.samples.playerstyle

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.shared.BlazeSDK
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.core.ui.R.string
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.playerstyle.databinding.ActivityPlayerStyleBinding

/**
 * This activity demonstrates how to use Blaze widgets to display stories and moments with
 * different player styles, default style and custom style.
 * For more information, see:
 * https://dev.wsc-sports.com/docs/android-story-player-customizations#/
 * https://dev.wsc-sports.com/docs/android-moments-player-customizations#/.
 */
class PlayerStyleActivity : AppCompatActivity(),
    BlazeWidgetDelegate by WidgetDelegateImpl(),
    BlazeFollowEntitiesDelegate {

    private val binding by viewBinding(ActivityPlayerStyleBinding::inflate)
    private val viewModel: PlayerStyleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        initFollowEntities()
        setupAppbar()
        initWidgets()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(string.player_style_title)) {
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

    private fun initFollowEntities() {
        // Set this manager as the delegate for SDK callbacks
        BlazeSDK.followEntitiesManager.delegate = this

        // 1. Load existing follow entities from local/remote storage

        // 2. Update the SDK with the followed entities:
//        BlazeSDK.followEntitiesManager.insertFollowedEntities(followEntities)
    }

    override fun onFollowEntityClicked(followEntityParams: BlazeFollowEntityClickedParams) {
        Log.d("FollowEntitiesManager", "onFollowEntityClicked - $followEntityParams")

        if (followEntityParams.newFollowingState) {
            // Update local/remote storage that this entity if followed
        } else {
            // Update local/remote storage that this entity if not followed
        }

    }


}