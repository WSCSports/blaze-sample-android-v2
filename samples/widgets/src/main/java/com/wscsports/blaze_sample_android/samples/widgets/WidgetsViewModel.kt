package com.wscsports.blaze_sample_android.samples.widgets

import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLiveStreamStatus
import com.blaze.blazesdk.features.videos.models.ui.BlazeVideoContentType
import com.blaze.blazesdk.features.videos.models.ui.BlazeVideosFilterParams
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.widgets.edit.EditMenuItem
import com.wscsports.blaze_sample_android.samples.widgets.edit.WidgetContentType
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

    val liveVideoRowBaseLayout: BlazeWidgetLayout
        get() = BlazeWidgetLayout.Presets.VideosWidget.Row.horizontalRectangles.apply {
            maxDisplayItemsCount = null
            widgetItemStyle.statusIndicator.apply {
                isVisible = true
                unreadState.backgroundColor = "#E5FF00".toColorInt()
                unreadState.textStyle.textColor = "#000000".toColorInt()
            }
        }

    // Requests stream content, across all stream states, so the Live Video widget's
    // demo data always has live/upcoming/ended items to show.
    val liveVideoFilterParams: BlazeVideosFilterParams
        get() = BlazeVideosFilterParams.base().apply {
            contentTypes = listOf(BlazeVideoContentType.STREAM)
            streamStates = listOf(BlazeLiveStreamStatus.LIVE, BlazeLiveStreamStatus.UPCOMING, BlazeLiveStreamStatus.ENDED)
        }

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
        // Screens without a content type build their own data sources and never open the edit
        // sheet, so there is no data state to initialize for them.
        val contentType = widgetType.contentType() ?: return
        _widgetDataState.update { prevState ->
            prevState ?: WidgetDataState(contentType = contentType)
        }
    }

    // Maps each widget screen to the content type its data source examples should use,
    // or null for screens that build their own data sources and never open the edit sheet.
    private fun WidgetScreenType.contentType(): WidgetContentType? {
        return when (this) {
            WidgetScreenType.STORIES_ROW -> WidgetContentType.STORIES
            WidgetScreenType.STORIES_GRID -> WidgetContentType.STORIES
            WidgetScreenType.MOMENTS_GRID -> WidgetContentType.MOMENTS
            WidgetScreenType.MOMENTS_ROW -> WidgetContentType.MOMENTS
            WidgetScreenType.VIDEOS_ROW -> WidgetContentType.VIDEOS
            WidgetScreenType.VIDEOS_GRID -> WidgetContentType.VIDEOS
            // Live videos are Videos content; the fragment still builds its own live-specific
            // data source (videos-live label + LiveFirst ordering + stream filter params).
            WidgetScreenType.LIVE_VIDEO_ROW -> WidgetContentType.VIDEOS
            WidgetScreenType.MIXED_WIDGETS -> null
            WidgetScreenType.METHODS_DELEGATES -> null
        }
    }

    fun getWidgetLayoutBasePreset(): BlazeWidgetLayout = when (_currWidgetType.value) {
        WidgetScreenType.STORIES_ROW -> storiesRowBaseLayout
        WidgetScreenType.STORIES_GRID -> storiesGridBaseLayout
        WidgetScreenType.MOMENTS_ROW -> momentsRowBaseLayout
        WidgetScreenType.MOMENTS_GRID -> momentsGridBaseLayout
        WidgetScreenType.VIDEOS_ROW -> videosRowBaseLayout
        WidgetScreenType.VIDEOS_GRID -> videosGridBaseLayout
        WidgetScreenType.LIVE_VIDEO_ROW -> liveVideoRowBaseLayout
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