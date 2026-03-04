package com.wscsports.blaze_sample_android.samples.search

import androidx.lifecycle.ViewModel
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.shared.results.BlazeResult
import com.blaze.blazesdk.shared.results.ErrorReason
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.samples.search.SearchUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {

    private data class WidgetLoadState(
        val itemCount: Int = 0,
        val isLoaded: Boolean = false,
        val isError: Boolean = false
    ) {
        val hasContent: Boolean get() = itemCount > 0
    }

    private val suggestionsDataSource: BlazeDataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_WIDGET_DEFAULT_LABEL))

    private val _uiState = MutableStateFlow<SearchUIState>(SearchUIState.Suggestions(suggestionsDataSource))
    val uiState: StateFlow<SearchUIState> = _uiState.asStateFlow()

    private var storiesLoad = WidgetLoadState()
    private var momentsLoad = WidgetLoadState()
    private var videosLoad = WidgetLoadState()

    val widgetDelegate: BlazeWidgetDelegate = object : BlazeWidgetDelegate {
        override fun onDataLoadComplete(
            playerType: BlazePlayerType,
            sourceId: String?,
            itemsCount: Int,
            result: BlazeResult<Unit>
        ) {
            handleDataLoadComplete(sourceId, itemsCount, result)
        }
    }


    fun loadSuggestions() {
        _uiState.value = SearchUIState.Suggestions(suggestionsDataSource)
    }

    fun performSearch(query: String) {
        if (query.isBlank()) {
            clearSearch()
            return
        }
        executeSearch(query)
    }

    fun clearSearch() {
        resetLoadingStateProperties()
        loadSuggestions()
    }

    /**
     * Handles data load completion from widgets.
     * Tracks when all widgets have finished loading to determine final state.
     */
    fun handleDataLoadComplete(sourceId: String?, itemsCount: Int, result: BlazeResult<Unit>) {
        if (sourceId == null) return
        if (_uiState.value !is SearchUIState.Results) return

        val loadState = WidgetLoadState(
            itemCount = itemsCount,
            isLoaded = true,
            isError = result is BlazeResult.Error &&
                result.reason != ErrorReason.NO_AVAILABLE_CONTENT_FOR_DATA_SOURCE
        )

        when (sourceId) {
            STORIES_WIDGET_ID -> storiesLoad = loadState
            MOMENTS_WIDGET_ID -> momentsLoad = loadState
            VIDEOS_WIDGET_ID -> videosLoad = loadState
        }

        if (storiesLoad.isLoaded && momentsLoad.isLoaded && videosLoad.isLoaded) {
            updateResultsState()
        }
    }

    private fun executeSearch(query: String) {
        resetLoadingStateProperties()
        _uiState.value = SearchUIState.Results(
            searchQuery = query,
            showStories = true,
            showMoments = true,
            showVideos = true
        )
    }

    private fun resetLoadingStateProperties() {
        storiesLoad = WidgetLoadState()
        momentsLoad = WidgetLoadState()
        videosLoad = WidgetLoadState()
    }

    private fun updateResultsState() {
        val currentResults = _uiState.value as? SearchUIState.Results ?: return
        val currentQuery = currentResults.searchQuery
        val hasAnyContent = storiesLoad.hasContent || momentsLoad.hasContent || videosLoad.hasContent
        val allErrored = storiesLoad.isError && momentsLoad.isError && videosLoad.isError

        _uiState.value = when {
            hasAnyContent -> SearchUIState.Results(
                searchQuery = currentQuery,
                showStories = storiesLoad.hasContent,
                showMoments = momentsLoad.hasContent,
                showVideos = videosLoad.hasContent
            )
            allErrored -> SearchUIState.Error(
                searchQuery = currentQuery,
                message = ""
            )
            else -> SearchUIState.NoResults(searchQuery = currentQuery)
        }
    }

    companion object {
        const val STORIES_WIDGET_ID = "search_results_stories_id"
        const val MOMENTS_WIDGET_ID = "search_results_moments_id"
        const val VIDEOS_WIDGET_ID = "search_results_videos_id"
        const val SUGGESTIONS_WIDGET_ID = "suggestions_moments_grid_id"
    }
}
