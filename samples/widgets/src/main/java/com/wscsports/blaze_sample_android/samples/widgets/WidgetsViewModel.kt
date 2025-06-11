package com.wscsports.blaze_sample_android.samples.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.VIDEOS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.samples.widgets.edit.EditMenuItem
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetDataState
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetLayoutStyleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
        get() = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles.apply {
            horizontalItemsSpacing = 0.blazeDp
            widgetItemStyle.image.apply {
                width = 188.blazeDp
                height = 300.blazeDp
                margins.top = 16.blazeDp
                margins.end = 8.blazeDp
            }
        }

    val momentsGridBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.MomentsWidget.Grid.twoColumnsVerticalRectangles.apply {
            horizontalItemsSpacing = 0.blazeDp
            verticalItemsSpacing = 0.blazeDp
            widgetItemStyle.image.apply {
                width = 156.blazeDp
                height = 230.blazeDp
                margins.top = 16.blazeDp
            }
        }

    val videosRowBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.VideosWidget.Row.horizontalRectangles

    val videosGridBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.VideosWidget.Grid.twoColumnsHorizontalRectangles

    private val _currWidgetType = MutableStateFlow<WidgetScreenType?>(null)
    val currWidgetType = _currWidgetType.asStateFlow()

    private val _editWidgetMenuItemEvent = MutableSharedFlow<EditMenuItem>()
    val editWidgetMenuItemEvent = _editWidgetMenuItemEvent.asSharedFlow()

    // WidgetScreenType is null means we are in the main widgets list fragment. Should be visible only for the widget fragments.
    val showEditWidgetMenuButton: StateFlow<Boolean> = _currWidgetType
        .map { it != null }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(),
            initialValue = false
        )

    private val _widgetStyleState = MutableStateFlow(WidgetLayoutStyleState())
    val widgetStyleState = _widgetStyleState.asStateFlow()

    private val _widgetDataState = MutableStateFlow<WidgetDataState?>(null)
    val updateWidgetDataStateEvent: SharedFlow<WidgetDataState> = _widgetDataState
        .filterNotNull()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly
    )

    // Should be visible only for the widget fragments and the style state is not empty.
    val showSelectedStyleChipsContainer: StateFlow<Boolean> = _currWidgetType.combine(_widgetStyleState) { widgetType, styleState ->
        widgetType != null && styleState.isNotEmpty()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(),
        initialValue = false
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
            WidgetScreenType.STORIES_ROW -> STORIES_WIDGET_DEFAULT_LABEL
            WidgetScreenType.STORIES_GRID -> STORIES_WIDGET_DEFAULT_LABEL
            WidgetScreenType.MOMENTS_GRID -> MOMENTS_WIDGET_DEFAULT_LABEL
            WidgetScreenType.MOMENTS_ROW -> MOMENTS_WIDGET_DEFAULT_LABEL
            WidgetScreenType.VIDEOS_ROW -> VIDEOS_WIDGET_DEFAULT_LABEL
            WidgetScreenType.VIDEOS_GRID -> VIDEOS_WIDGET_DEFAULT_LABEL
            WidgetScreenType.MIXED_WIDGETS -> "" // Not needed for mixed widgets
        }
    }

    fun getWidgetLayoutBasePreset(): BlazeWidgetLayout = when (_currWidgetType.value) {
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

    fun onEditMenuOptionClicked(item: EditMenuItem) {
        viewModelScope.launch {
            _editWidgetMenuItemEvent.emit(item)
        }
    }
}