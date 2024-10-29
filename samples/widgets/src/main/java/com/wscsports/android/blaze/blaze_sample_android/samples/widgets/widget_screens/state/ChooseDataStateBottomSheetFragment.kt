package com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.blaze.blazesdk.data_source.BlazeOrderType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.R
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.databinding.WidgetDataStateBottomSheetLayoutBinding
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.WidgetsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ChooseDataStateBottomSheetFragment : BottomSheetDialogFragment(R.layout.widget_data_state_bottom_sheet_layout) {

    private val binding by viewBinding(WidgetDataStateBottomSheetLayoutBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderTypeSpinner()
        initializeUI()
        setClickListeners()
    }

    private fun initializeUI() {
        val dataState = viewModel.getCurrWidgetDataState()
        with(binding) {
            labelTextInputLayout.editText?.setText(dataState.labelName)
            orderTypeSpinner.setSelection(BlazeOrderType.entries.indexOf(dataState.orderType))
        }
    }

    private fun setupOrderTypeSpinner() {
        val orderTypes = BlazeOrderType.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, orderTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.orderTypeSpinner.adapter = adapter
    }

    private fun setClickListeners() {
        binding.applyButton.setOnClickListener {
            if(!isLabelExpressionBlank()) {
                updateDataState()
                dismiss()
            }
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun isLabelExpressionBlank(): Boolean {
        return binding.labelTextInputLayout.editText?.text.toString().isBlank().also {
            if (it) Toast.makeText(context, "Label can't be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDataState() {
        with(binding) {
            val state = WidgetDataState(
                labelName = labelTextInputLayout.editText?.text.toString(),
                orderType = BlazeOrderType.entries[orderTypeSpinner.selectedItemPosition],
            )
            viewModel.setWidgetDataState(state)
        }
    }

    companion object {
        const val TAG = "DataStateBottomSheetFragment"

        fun newInstance() = ChooseDataStateBottomSheetFragment()
    }

}