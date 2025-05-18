package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * BaseWidgetFragment is a Fragment that serves as the base class for all widget screens.
 * It implements the BlazeWidgetDelegate and provides a base implementation for common widget screen operations,
 * including initializing the widget and updating the style and data source states.
 */
abstract class BaseWidgetFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

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
                    onNewWidgetLayoutState(layoutStyleState)
                }
            }
            launch {
                viewModel.updateWidgetDataStateEvent.collectLatest { dataState ->
                    onNewDatasourceState(dataState)
                }
            }
        }
    }

    abstract val widgetType: WidgetScreenType
    abstract fun initWidgetView()
    abstract fun onNewWidgetLayoutState(styleState: WidgetLayoutStyleState)
    abstract fun onNewDatasourceState(dataState: WidgetDataState)
}