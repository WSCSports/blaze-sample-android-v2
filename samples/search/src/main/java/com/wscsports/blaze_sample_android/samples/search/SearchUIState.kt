package com.wscsports.blaze_sample_android.samples.search

import com.blaze.blazesdk.data_source.BlazeDataSourceType

/**
 * Represents the different UI states for the Search Screen.
 */
sealed class SearchUIState {

    /**
     * Suggestions state displayed when the search field is empty.
     * Shows a grid of moments from the provided suggestions data source.
     *
     * @param suggestionsDataSource Data source for the suggestions grid.
     */
    data class Suggestions(
        val suggestionsDataSource: BlazeDataSourceType
    ) : SearchUIState()

    /**
     * Results state showing search results from all content types.
     * Each content type (stories, moments, videos) is fetched by its
     * own widget using BlazeDataSourceType.Search, and displayed in its own section.
     * Widgets handle their own loading states (skeleton animations).
     *
     * Per-section visibility starts as `true` so widgets compose and load data,
     * then is updated to reflect actual content availability after all widgets report back.
     *
     * @param searchQuery The search query that was executed
     * @param showStories Whether the stories section should be visible
     * @param showMoments Whether the moments section should be visible
     * @param showVideos Whether the videos section should be visible
     */
    data class Results(
        val searchQuery: String,
        val showStories: Boolean = true,
        val showMoments: Boolean = true,
        val showVideos: Boolean = true
    ) : SearchUIState()

    /**
     * Empty results state when no content matches the search query.
     *
     * @param searchQuery The search query that returned no results
     */
    data class NoResults(val searchQuery: String) : SearchUIState()

    /**
     * Error state when search fails (e.g., network error, timeout).
     *
     * @param searchQuery The search query that failed
     * @param message A user-facing error message
     */
    data class Error(val searchQuery: String, val message: String) : SearchUIState()
}