package com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.WidgetType
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state.WidgetDataState
import com.wscsports.android.blaze.blaze_sample_android.samples.widgets.widget_screens.state.WidgetLayoutStyleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WidgetsViewModel: ViewModel() {

    private val _currWidgetType = MutableStateFlow<WidgetType?>(null)
    val currWidgetType = _currWidgetType.asStateFlow()

    val showEditWidgetFab: StateFlow<Boolean> = _currWidgetType
        .map { it != null }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    private val _widgetStyleState = MutableStateFlow(WidgetLayoutStyleState())
    val widgetStyleState = _widgetStyleState.asStateFlow()

    private val _widgetDataState = MutableStateFlow<WidgetDataState?>(null)
    val updateWidgetDataStateEvent: SharedFlow<WidgetDataState> = _widgetDataState
        .filterNotNull()
        .shareIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 0
    )

    fun getCurrWidgetDataState(): WidgetDataState = _widgetDataState.value ?: WidgetDataState()

    fun setWidgetLayoutStyleState(state: WidgetLayoutStyleState) {
        _widgetStyleState.update { state }
    }

    fun setWidgetDataState(state: WidgetDataState) {
        _widgetDataState.update { state }
    }

    fun setCurrWidgetTypeAndInitLabelIfNeeded(widgetType: WidgetType) {
        _currWidgetType.update { widgetType }
        _widgetDataState.update { prevState ->
            prevState
                ?: WidgetDataState(
                    labelName = widgetType.initDataSourceLabel()
                )
        }
    }

    private fun WidgetType.initDataSourceLabel(): String {
        return when (_currWidgetType.value) {
            WidgetType.STORIES_ROW -> "live-stories"
            WidgetType.STORIES_GRID -> "live-stories"
            WidgetType.MOMENTS_GRID -> "moments"
            WidgetType.MOMENTS_ROW -> "moments"
            else -> ""
        }
    }

    fun getWidgetLayoutPreset(): BlazeWidgetLayout = when (_currWidgetType.value) {
        WidgetType.STORIES_ROW -> BlazeWidgetLayout.Presets.StoriesWidget.Row.circles
        WidgetType.STORIES_GRID -> BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles
        WidgetType.MOMENTS_ROW -> BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles
        WidgetType.MOMENTS_GRID -> BlazeWidgetLayout.Presets.MomentsWidget.Grid.twoColumnsVerticalRectangles
        else -> BlazeWidgetLayout.Presets.StoriesWidget.Row.circles
    }

    // when the user navigates back to the widget screen, we reset the widget state
    fun resetAllWidgetStates(delayMillis: Long = 500) {
        viewModelScope.launch {
            _currWidgetType.emit(null)
            delay(delayMillis) // Optional - to remain unchanged during back navigation with animation
            _widgetStyleState.emit(WidgetLayoutStyleState())
            _widgetDataState.emit(null)
        }
    }
}