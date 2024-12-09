package com.wscsports.blaze_sample_android.samples.widgets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentWidgetListBinding
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.WidgetsViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

/**
 * WidgetListFragment is a Fragment that displays a list of widget samples.
 * It uses a RecyclerView to display a list of widgets, each of which can be clicked to navigate to a different screen.
 */
class WidgetListFragment : Fragment(R.layout.fragment_widget_list) {

    private val binding by viewBinding(FragmentWidgetListBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()
    private var widgetsAdapter: WidgetListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.resetAllWidgetStates()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        widgetsAdapter = WidgetListAdapter { widget ->
            findNavController().navigate(widget.navDestinationId)
        }
        binding.recyclerView.adapter = widgetsAdapter
        widgetsAdapter?.submitList(WidgetScreenType.entries)
    }

}