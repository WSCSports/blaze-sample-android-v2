package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blaze.blazesdk.delegates.BlazePlayerInContainerDelegate
import com.wscsports.blaze_sample_android.core.MomentsContainerDelegateImpl
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.FragmentHomeBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * This fragment represents the app's home screen and includes navigation to the Moments container fragment..
 * For more information, see https://dev.wsc-sports.com/docs/android-moments-player#/..
 */
class HomeFragment : Fragment(R.layout.fragment_home),
    BlazePlayerInContainerDelegate by MomentsContainerDelegateImpl() {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: MomentsContainerViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppbar()
        setClickListeners()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Moments Container") {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun setClickListeners() {
        binding.showMomentsButton.setOnClickListener {
            viewModel.setMomentsTabSelectedEvent()
        }
    }


}