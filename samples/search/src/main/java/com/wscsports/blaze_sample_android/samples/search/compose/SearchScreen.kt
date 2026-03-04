package com.wscsports.blaze_sample_android.samples.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.extentions.blazeDeepCopy
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.stories.widgets.compose.BlazeComposeWidgetStoriesStateHandler
import com.blaze.blazesdk.features.videos.widgets.compose.BlazeComposeWidgetVideosStateHandler
import com.blaze.blazesdk.style.shared.models.blazeDp
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.samples.search.SearchViewModel
import com.wscsports.blaze_sample_android.samples.search.SearchUIState

private val searchResultCornerRadius = 4.blazeDp
private val suggestionsItemSpacing = 2.blazeDp

/**
 * Main search screen composable.
 * Handles different UI states: Suggestions, Results, NoResults, Error.
 *
 * Result widget handlers are created once per Results session via [remember] (no key)
 * and reused across searches by calling [updateDataSource][SearchResultHandlers.updateAll].
 * They are disposed when the Results branch leaves composition, preventing
 * detached LayoutNode access from async callbacks.
 */
@Composable
fun SearchScreen(
    uiState: SearchUIState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onBackPressed: () -> Unit,
    widgetDelegate: BlazeWidgetDelegate,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SearchDefaults.backgroundColor)
            .padding(contentPadding)
    ) {
        SearchHeader(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            onSearch = onSearch,
            onClear = onClear,
            onBackPressed = onBackPressed
        )

        when (uiState) {
            is SearchUIState.Suggestions -> {
                val handler = remember(uiState.suggestionsDataSource) {
                    createSuggestionsHandler(uiState.suggestionsDataSource, widgetDelegate)
                }
                SuggestionsGridWidget(
                    stateHandler = handler,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is SearchUIState.Results -> {
                val resultHandlers = remember {
                    createResultHandlers(uiState.searchQuery, widgetDelegate)
                }
                val lastAppliedQuery = rememberSaveable { mutableStateOf("") }
                SideEffect {
                    if (lastAppliedQuery.value != uiState.searchQuery) {
                        lastAppliedQuery.value = uiState.searchQuery
                        resultHandlers.updateAll(uiState.searchQuery)
                    }
                }
                SearchResultsContent(
                    resultsState = uiState,
                    storiesStateHandler = resultHandlers.storiesHandler,
                    momentsStateHandler = resultHandlers.momentsHandler,
                    videosStateHandler = resultHandlers.videosHandler,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is SearchUIState.NoResults -> {
                NoResultsContent(searchQuery = uiState.searchQuery)
            }

            is SearchUIState.Error -> {
                ErrorContent()
            }
        }
    }
}

private class SearchResultHandlers(
    val storiesHandler: BlazeComposeWidgetStoriesStateHandler,
    val momentsHandler: BlazeComposeWidgetMomentsStateHandler,
    val videosHandler: BlazeComposeWidgetVideosStateHandler
) {
    fun updateAll(searchQuery: String) {
        val dataSource = BlazeDataSourceType.Search(searchText = searchQuery)
        storiesHandler.updateDataSource(dataSource)
        momentsHandler.updateDataSource(dataSource)
        videosHandler.updateDataSource(dataSource)
    }
}

private fun createResultHandlers(
    searchQuery: String,
    widgetDelegate: BlazeWidgetDelegate
): SearchResultHandlers {
    val initialDataSource = BlazeDataSourceType.Search(searchText = searchQuery)
    return SearchResultHandlers(
        storiesHandler = BlazeComposeWidgetStoriesStateHandler(
            widgetId = SearchViewModel.STORIES_WIDGET_ID,
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles,
            dataSourceType = initialDataSource,
            widgetDelegate = widgetDelegate,
            shouldOrderWidgetByReadStatus = false
        ),
        momentsHandler = BlazeComposeWidgetMomentsStateHandler(
            widgetId = SearchViewModel.MOMENTS_WIDGET_ID,
            widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles
                .blazeDeepCopy().apply { widgetItemStyle.image.cornerRadius = searchResultCornerRadius },
            dataSourceType = initialDataSource,
            widgetDelegate = widgetDelegate,
            shouldOrderWidgetByReadStatus = false
        ),
        videosHandler = BlazeComposeWidgetVideosStateHandler(
            widgetId = SearchViewModel.VIDEOS_WIDGET_ID,
            widgetLayout = BlazeWidgetLayout.Presets.VideosWidget.Row.horizontalRectangles
                .blazeDeepCopy().apply { widgetItemStyle.image.cornerRadius = searchResultCornerRadius },
            dataSourceType = initialDataSource,
            widgetDelegate = widgetDelegate,
            shouldOrderWidgetByReadStatus = false
        )
    )
}

private fun createSuggestionsHandler(
    dataSource: BlazeDataSourceType,
    widgetDelegate: BlazeWidgetDelegate
): BlazeComposeWidgetMomentsStateHandler {
    val widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Grid
        .threeColumnsVerticalRectangles.blazeDeepCopy().apply {
            widgetItemStyle.apply {
                image.apply {
                    margins.apply {
                        top = 0.blazeDp; bottom = 0.blazeDp
                        start = 0.blazeDp; end = 0.blazeDp
                    }
                    cornerRadius = 0.blazeDp
                    gradientOverlay.isVisible = false
                }
                title.isVisible = false
                statusIndicator.isVisible = false
                badge.isVisible = false
                durationElement.isVisible = false
                cornerRadius = 0.blazeDp
            }
            horizontalItemsSpacing = suggestionsItemSpacing
            verticalItemsSpacing = suggestionsItemSpacing
            margins.apply {
                start = 0.blazeDp; top = 0.blazeDp
                end = 0.blazeDp; bottom = 0.blazeDp
            }
        }

    return BlazeComposeWidgetMomentsStateHandler(
        widgetId = SearchViewModel.SUGGESTIONS_WIDGET_ID,
        widgetLayout = widgetLayout,
        dataSourceType = dataSource,
        widgetDelegate = widgetDelegate,
        shouldOrderWidgetByReadStatus = false
    )
}

@Composable
private fun NoResultsContent(searchQuery: String) {
    Text(
        text = "No results found for \"$searchQuery\"",
        color = SearchDefaults.textSecondary,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun ErrorContent() {
    Text(
        text = "Something went wrong. Please try again.",
        color = SearchDefaults.textSecondary,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    )
}
