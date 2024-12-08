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
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentLazyStartMomentsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch


class LazyStartMomentsFragment : Fragment(R.layout.fragment_lazy_start_moments),
    BlazePlayerInContainerDelegate by MomentsContainerDelegateImpl() {

    private val binding by viewBinding(FragmentLazyStartMomentsBinding::bind)
    private val viewModel: MomentsContainerViewModel by activityViewModels()

    private val momentsPlayerContainer: BlazeMomentsPlayerContainer by lazy {
        BlazeMomentsPlayerContainer(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL)),
            containerId = MomentsContainerActivity.LAZY_MOMENTS_CONTAINER_ID,
            momentsPlayerStyle = viewModel.lazyMomentsPlayerStyle,
            playerInContainerDelegate = this,
            shouldOrderMomentsByReadStatus = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppbar()
        setClickListeners()
        subscribeObservers()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Moments Container") {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
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

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.onVolumeChangedEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                momentsPlayerContainer.onVolumeChanged()
            }
        }
    }

}