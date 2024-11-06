package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerInContainerDelegate
import com.blaze.blazesdk.features.moments.container.BlazeMomentsPlayerContainer
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_LABEL
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentInstantsMomentsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class InstantMomentsFragment : Fragment(R.layout.fragment_instants_moments),
    BlazePlayerInContainerDelegate by MomentsContainerDelegateImp() {

    private val binding by viewBinding(FragmentInstantsMomentsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMomentsInContainer()
    }

    private fun startMomentsInContainer() {
        val momentsPlayerStyle = getMomentsPlayerStyle()
        val momentsPlayerContainer = BlazeMomentsPlayerContainer(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL)),
            containerId = "instant-moments-container-unique-id",
            momentsPlayerStyle = momentsPlayerStyle,
            playerInContainerDelegate = this,
            shouldOrderMomentsByReadStatus = true
        )
        momentsPlayerContainer.startPlaying(childFragmentManager, binding.momentsContainer)
    }

    private fun getMomentsPlayerStyle(): BlazeMomentsPlayerStyle {
        return BlazeMomentsPlayerStyle.base().apply {
            // buttons customization
            buttons.exit.isVisible = false // true by default
        }
    }

}