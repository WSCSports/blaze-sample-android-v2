package com.wscsports.blaze_sample_android.samples.widgets.widget_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.widgets.WidgetScreenType
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.widget_screens.state.WidgetLayoutStyleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * WidgetsViewModel is a ViewModel that manages the widget state.
 * It provides the base layout for each widget type and initializes the widget data and style states.
 * Note: every call to a base layout triggers a new instance of the layout in order to get a fresh copy without our previous changes.
 */
class WidgetsViewModel: ViewModel() {

    val storiesRowBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles

    val storiesGridBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles

    val momentsRowBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles

    val momentsGridBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.MomentsWidget.Grid.twoColumnsVerticalRectangles

    val videosRowBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.VideosWidget.Row.horizontalRectangles

    val videosGridBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.VideosWidget.Grid.twoColumnsHorizontalRectangles

    private val _currWidgetType = MutableStateFlow<WidgetScreenType?>(null)
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
            scope = viewModelScope,
            started = SharingStarted.Eagerly
    )

    fun getCurrWidgetDataState(): WidgetDataState = _widgetDataState.value ?: WidgetDataState()

    fun setWidgetLayoutStyleState(state: WidgetLayoutStyleState) {
        _widgetStyleState.update { state }
    }

    fun setWidgetDataState(state: WidgetDataState) {
        _widgetDataState.update { state }
    }

    fun setCurrWidgetTypeAndInitLabelIfNeeded(widgetType: WidgetScreenType) {
        _currWidgetType.update { widgetType }
        _widgetDataState.update { prevState ->
            prevState
                ?: WidgetDataState(
                    labelName = widgetType.initDataSourceLabel()
                )
        }
    }

    private fun WidgetScreenType.initDataSourceLabel(): String {
        return when (this) {
            WidgetScreenType.STORIES_ROW -> "live-stories"
            WidgetScreenType.STORIES_GRID -> "top-stories"
            WidgetScreenType.MOMENTS_GRID -> "moments"
            WidgetScreenType.MOMENTS_ROW -> "moments"
            WidgetScreenType.VIDEOS_ROW -> "videos"
            WidgetScreenType.VIDEOS_GRID -> "videos"
            WidgetScreenType.MIXED_WIDGETS -> "" // Not needed for mixed widgets
        }
    }

    fun getWidgetLayoutPreset(): BlazeWidgetLayout = when (_currWidgetType.value) {
        WidgetScreenType.STORIES_ROW -> storiesRowBaseLayout
        WidgetScreenType.STORIES_GRID -> storiesGridBaseLayout
        WidgetScreenType.MOMENTS_ROW -> momentsRowBaseLayout
        WidgetScreenType.MOMENTS_GRID -> momentsGridBaseLayout
        WidgetScreenType.VIDEOS_ROW -> videosRowBaseLayout
        WidgetScreenType.VIDEOS_GRID -> videosGridBaseLayout
        else -> throw IllegalStateException("Widget type is not set")
    }

    // when the user navigates back to the widget screen, we reset the widget state
    fun resetAllWidgetStates(delayMillis: Long = 500) {
        viewModelScope.launch {
            _currWidgetType.emit(null)
            delay(delayMillis) // only to wait during back navigation until animation is finished
            _widgetStyleState.emit(WidgetLayoutStyleState())
            _widgetDataState.emit(null)
        }
    }
}