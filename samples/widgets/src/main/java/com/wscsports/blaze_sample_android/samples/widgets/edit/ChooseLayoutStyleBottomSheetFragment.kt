package com.wscsports.blaze_sample_android.samples.widgets.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wscsports.blaze_sample_android.core.ui.showView
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.databinding.WidgetLayoutStyleBottomSheetLayoutBinding
import com.wscsports.blaze_sample_android.samples.widgets.WidgetsViewModel
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
        val showsSubtitleAndDescription = viewModel.currWidgetType.value?.supportsSubtitleAndDescription == true
        binding.apply {
            isCustomImageCheckBox.isChecked = styleState.isCustomAppearance
            isCustomIndicatorCheckBox.isChecked = styleState.isCustomStatusIndicator
            isCustomTitleCheckBox.isChecked = styleState.isCustomTitle
            isCustomBadgeCheckBox.isChecked = styleState.isCustomBadge
            subtitleAndDescriptionGroup.showView(showsSubtitleAndDescription)
            isSubtitleCheckBox.isChecked = styleState.isSubtitleVisible
            isDescriptionCheckBox.isChecked = styleState.isDescriptionVisible
            isCustomItemStyleOverridesCheckBox.isChecked = styleState.isCustomItemStyleOverrides
            isStaticThumbnailsCheckBox.isChecked = styleState.isStaticThumbnails
            isStaticThumbnailsCheckBox.isVisible = viewModel.hasAnimatedThumbnailsPreset
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
                isCustomAppearance = isCustomImageCheckBox.isChecked,
                isCustomStatusIndicator = isCustomIndicatorCheckBox.isChecked,
                isCustomTitle = isCustomTitleCheckBox.isChecked,
                isCustomBadge = isCustomBadgeCheckBox.isChecked,
                isSubtitleVisible = isSubtitleCheckBox.isChecked,
                isDescriptionVisible = isDescriptionCheckBox.isChecked,
                isCustomItemStyleOverrides = isCustomItemStyleOverridesCheckBox.isChecked,
                isStaticThumbnails = isStaticThumbnailsCheckBox.isChecked
            )
            viewModel.setWidgetLayoutStyleState(state)
        }
    }


    companion object {
        const val TAG = "WidgetLayoutStyleBottomSheetFragment"

        fun newInstance() = ChooseLayoutStyleBottomSheetFragment()
    }

}