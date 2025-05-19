package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.BlazeMomentsPlayerContainer
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.ActivityMomentsContainerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * This activity demonstrates how to use BlazeMomentsPlayerContainer.
 * It contains two BlazeMomentsPlayerContainer instances.
 * For more information, see https://dev.wsc-sports.com/docs/android-moments-player#/.
 */
class MomentsContainerActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMomentsContainerBinding::inflate)
    private val viewModel: MomentsContainerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding(shouldIncludeBottom = false)
        setupBottomNavigation()
        prepareMomentsContainer()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.onMomentsTabSelectedEvent.flowWithLifecycle(lifecycle).collect {
                binding.bottomNavigation.selectedItemId = R.id.momentsFragment
            }
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

    /**
     * Observing user increasing/decreasing volume, and updating the SDK
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

    companion object {
        const val MOMENTS_LABEL = "moments"
        const val INSTANT_MOMENTS_CONTAINER_ID = "instant-moments-container-unique-id"
        const val LAZY_MOMENTS_CONTAINER_ID = "lazy-moments-container-unique-id"
    }

}