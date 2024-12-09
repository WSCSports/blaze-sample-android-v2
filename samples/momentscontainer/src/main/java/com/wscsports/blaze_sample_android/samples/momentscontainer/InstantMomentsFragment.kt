package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerInContainerDelegate
import com.blaze.blazesdk.features.moments.container.BlazeMomentsPlayerContainer
import com.wscsports.android.blaze.blaze_sample_android.core.MomentsContainerDelegateImpl
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_LABEL
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentInstantsMomentsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch

/**
 * This fragment demonstrates how to use BlazeMomentsPlayerContainer.
 * It contains a BlazeMomentsPlayerContainer instance that starts playing moments instantly.
 * For more information, see https://dev.wsc-sports.com/docs/android-moments-player#/.
 */
class InstantMomentsFragment : Fragment(R.layout.fragment_instants_moments),
    BlazePlayerInContainerDelegate by MomentsContainerDelegateImpl() {

    private val binding by viewBinding(FragmentInstantsMomentsBinding::bind)
    private val viewModel: MomentsContainerViewModel by activityViewModels()

    private val momentsPlayerContainer: BlazeMomentsPlayerContainer by lazy {
        BlazeMomentsPlayerContainer(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL)),
            containerId = MomentsContainerActivity.INSTANT_MOMENTS_CONTAINER_ID,
            momentsPlayerStyle = viewModel.instantMomentsPlayerStyle,
            playerInContainerDelegate = this,
            shouldOrderMomentsByReadStatus = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMomentsInContainer()
        subscribeObservers()
    }

    private fun startMomentsInContainer() {
        momentsPlayerContainer.startPlaying(childFragmentManager, binding.momentsContainer)
    }

    // Observing user increasing/decreasing volume from the activity, and updating the SDK
    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.onVolumeChangedEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                momentsPlayerContainer.onVolumeChanged()
            }
        }
    }

}