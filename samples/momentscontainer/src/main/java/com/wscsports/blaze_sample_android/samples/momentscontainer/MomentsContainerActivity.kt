package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.BlazeMomentsPlayerContainer
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.ActivityMomentsContainerBinding

class MomentsContainerActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMomentsContainerBinding::inflate)
    private val viewModel: MomentsContainerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        setupBottomNavigation()
        prepareMomentsContainer()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Moments Container") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.moments_player_container)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    // Optional - prepare the containers for faster loading. We have two containers in this activity.
    private fun prepareMomentsContainer() {
        BlazeMomentsPlayerContainer.prepareMoments(
            containerId = INSTANT_MOMENTS_CONTAINER_ID,
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL))
        )
        BlazeMomentsPlayerContainer.prepareMoments(
            containerId = LAZY_MOMENTS_CONTAINER_ID,
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL))
        )
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP -> {
                viewModel.setOnVolumeChangedEvent()
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    companion object {
        const val MOMENTS_LABEL = "moments"
        const val INSTANT_MOMENTS_CONTAINER_ID = "instant-moments-container-unique-id"
        const val LAZY_MOMENTS_CONTAINER_ID = "lazy-moments-container-unique-id"
    }

}