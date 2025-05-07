package com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.databinding.WidgetLayoutStyleBottomSheetLayoutBinding
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.WidgetsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ChooseLayoutStyleBottomSheetFragment: BottomSheetDialogFragment(R.layout.widget_layout_style_bottom_sheet_layout) {

    private val binding by viewBinding(WidgetLayoutStyleBottomSheetLayoutBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        setClickListeners()
    }

    private fun initializeUI() {
        val styleState = viewModel.widgetStyleState.value
        binding.apply {
            isCustomImageCheckBox.isChecked = styleState.isCustomImage
            isCustomIndicatorCheckBox.isChecked = styleState.isCustomStatusIndicator
            isCustomTitleCheckBox.isChecked = styleState.isCustomTitle
            isCustomBadgeCheckBox.isChecked = styleState.isCustomBadge
            isCustomItemStyleOverridesCheckBox.isChecked = styleState.isCustomItemStyleOverrides
        }
    }

    private fun setClickListeners() {
        binding.applyButton.setOnClickListener {
            updateStyleState()
            dismiss()
        }
    }

    private fun updateStyleState() {
        binding.apply {
            val state = WidgetLayoutStyleState(
                isCustomImage = isCustomImageCheckBox.isChecked,
                isCustomStatusIndicator = isCustomIndicatorCheckBox.isChecked,
                isCustomTitle = isCustomTitleCheckBox.isChecked,
                isCustomBadge = isCustomBadgeCheckBox.isChecked,
                isCustomItemStyleOverrides = isCustomItemStyleOverridesCheckBox.isChecked
            )
            viewModel.setWidgetLayoutStyleState(state)
        }
    }


    companion object {
        const val TAG = "WidgetLayoutStyleBottomSheetFragment"

        fun newInstance() = ChooseLayoutStyleBottomSheetFragment()
    }

}