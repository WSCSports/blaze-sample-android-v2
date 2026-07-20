package com.wscsports.blaze_sample_android.samples.widgets.edit

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.blaze.blazesdk.data_source.BlazeOrderType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.databinding.WidgetDataStateBottomSheetLayoutBinding
import com.wscsports.blaze_sample_android.samples.widgets.WidgetsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ChooseDataStateBottomSheetFragment : BottomSheetDialogFragment(R.layout.widget_data_state_bottom_sheet_layout) {

    private val binding by viewBinding(WidgetDataStateBottomSheetLayoutBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataSourceRadioGroup()
        setupOrderTypeSpinner()
        initializeUI()
        setClickListeners()
    }

    private fun initializeUI() {
        val dataState = viewModel.getCurrWidgetDataState()
        binding.apply {
            radioButtonFor(dataState.selectedExample).isChecked = true
            orderTypeSpinner.setSelection(BlazeOrderType.entries.indexOf(dataState.orderType))
        }
    }

    // Each radio option maps to a different data source example - see WidgetDataState.toDataSource().
    private fun setupDataSourceRadioGroup() {
        DataSourceExample.entries.forEach { example ->
            binding.dataSourceRadioGroup.addView(
                RadioButton(requireContext()).apply {
                    tag = example
                    text = example.toTwoLineText()
                    setPadding(paddingLeft, 8.dpToPx(), paddingRight, 8.dpToPx())
                }
            )
        }
    }

    private fun radioButtonFor(example: DataSourceExample): RadioButton =
        binding.dataSourceRadioGroup.children
            .first { it.tag == example } as RadioButton

    private fun selectedDataSourceExample(): DataSourceExample {
        val checkedButton = binding.dataSourceRadioGroup.children.first { (it as RadioButton).isChecked }
        return checkedButton.tag as DataSourceExample
    }

    // Title with a smaller, gray description line below it.
    private fun DataSourceExample.toTwoLineText(): CharSequence =
        SpannableStringBuilder().apply {
            append(title)
            val descriptionStart = length + 1
            append("\n$description")
            setSpan(RelativeSizeSpan(0.8f), descriptionStart, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(Color.GRAY), descriptionStart, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun setupOrderTypeSpinner() {
        val orderTypes = BlazeOrderType.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, orderTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.orderTypeSpinner.adapter = adapter
    }

    private fun setClickListeners() {
        binding.applyButton.setOnClickListener {
            updateDataState()
            dismiss()
        }
    }

    private fun updateDataState() {
        binding.apply {
            val state = viewModel.getCurrWidgetDataState().copy(
                selectedExample = selectedDataSourceExample(),
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
