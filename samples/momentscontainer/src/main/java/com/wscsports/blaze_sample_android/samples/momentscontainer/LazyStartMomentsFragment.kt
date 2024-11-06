package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerInContainerDelegate
import com.blaze.blazesdk.features.moments.container.BlazeMomentsPlayerContainer
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerCtaStyle
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.wscsports.blaze_sample_android.samples.momentscontainer.MomentsContainerActivity.Companion.MOMENTS_LABEL
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentLazyStartMomentsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class LazyStartMomentsFragment : Fragment(R.layout.fragment_lazy_start_moments),
    BlazePlayerInContainerDelegate by MomentsContainerDelegateImp() {

    private val binding by viewBinding(FragmentLazyStartMomentsBinding::bind)
    private lateinit var momentsPlayerContainer: BlazeMomentsPlayerContainer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        initMomentsInContainer()
    }

    private fun setClickListeners() {
        binding.showMomentsButton.setOnClickListener {
            startPlayingMoments()
        }
    }

    private fun startPlayingMoments() {
        momentsPlayerContainer.startPlaying(
            childFragmentManager,
            binding.momentsContainer,
        )
    }

    private fun initMomentsInContainer() {
        val momentsPlayerStyle = getMomentsPlayerStyle()
        momentsPlayerContainer = BlazeMomentsPlayerContainer(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL)),
            containerId = "lazy-moments-container-unique-id",
            momentsPlayerStyle = momentsPlayerStyle,
            playerInContainerDelegate = this,
            shouldOrderMomentsByReadStatus = true
        )
    }

    private fun getMomentsPlayerStyle(): BlazeMomentsPlayerStyle {
        return BlazeMomentsPlayerStyle.base().apply {
            // buttons customization
            buttons.mute.isVisible = true // true by default
            buttons.exit.isVisible = true // true by default
            // Seek bar customization
            seekBar.playingState.cornerRadius = 0.blazeDp
            seekBar.pausedState.cornerRadius = 0.blazeDp
            seekBar.pausedState.isThumbVisible = false
            seekBar.bottomMargin = 0.blazeDp
            seekBar.horizontalMargin = 0.blazeDp
            // cta customization
            cta.layoutPositioning = BlazeMomentsPlayerCtaStyle.BlazeCTAPositioning.CTA_NEXT_TO_BOTTOM_BUTTONS_BOX
        }
    }

}