package com.wscsports.blaze_sample_android.samples.widgets.widget_screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.wscsports.blaze_sample_android.samples.widgets.WidgetType
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetLayoutStyleState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseWidgetFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected val viewModel: WidgetsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.setCurrWidgetTypeAndInitLabelIfNeeded(widgetType)
        initWidgetView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            launch {
                viewModel.widgetStyleState.collectLatest { layoutStyleState ->
                    Log.d("appDebug", "StoriesRowFragment: updateLayoutStyleStateFlow: new layoutStyleState = $layoutStyleState")
                    onNewWidgetLayoutState(layoutStyleState)
                }
            }
            launch {
                viewModel.updateWidgetDataStateEvent.collectLatest { dataState ->
                    Log.d("appDebug", "StoriesRowFragment: updateDataStateFlow: new dataState = $dataState")
                    onNewDatasourceState(dataState)
                }
            }
        }
    }

    abstract val widgetType: WidgetType
    abstract fun initWidgetView()
    abstract fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState)
    abstract fun onNewDatasourceState(dataState: WidgetDataState)
}