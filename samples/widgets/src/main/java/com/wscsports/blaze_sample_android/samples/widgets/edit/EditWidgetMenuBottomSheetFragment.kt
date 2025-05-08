package com.wscsports.blaze_sample_android.samples.widgets.edit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetListAdapter
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.databinding.EditWidgetMenuBottomSheetLayoutBinding
import com.wscsports.blaze_sample_android.samples.widgets.screens.WidgetsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class EditWidgetMenuBottomSheetFragment : BottomSheetDialogFragment(R.layout.edit_widget_menu_bottom_sheet_layout) {

    private val binding by viewBinding(EditWidgetMenuBottomSheetLayoutBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()

    private var menuAdapter: EditWidgetMenuListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        menuAdapter = EditWidgetMenuListAdapter { menuItem ->
            viewModel.onEditMenuOptionClicked(menuItem)
        }
        binding.recyclerView.adapter = menuAdapter
        menuAdapter?.submitList(EditMenuItem.entries)
    }


    companion object {
        const val TAG = "EditWidgetMenuBottomSheetFragment"

        fun newInstance() = EditWidgetMenuBottomSheetFragment()
    }

}