package com.wscsports.blaze_sample_android.samples.widgets


enum class WidgetScreenType(
    val title: String,
    val description: String,
    val navDestinationId: Int
) {
    STORIES_ROW(
        title = "Stories-row",
        description = "This represents a Blaze widget view for stories with a row layout on a horizontal axis",
        navDestinationId = R.id.action_navigation_widget_list_to_storiesRowFragment
    ),
    STORIES_GRID(
        title = "Stories-grid",
        description = "This represents a Blaze widget view for stories with a grid layout on a vertical axis",
        navDestinationId = R.id.action_navigation_widget_list_to_storiesGridFragment
    ),
    MOMENTS_ROW(
        title = "Moments-row",
        description = "This represents a Blaze widget view for moments with a row layout on a horizontal axis",
        navDestinationId = R.id.action_navigation_widget_list_to_momentsRowFragment
    ),
    MOMENTS_GRID(
        title = "Moments-grid",
        description = "This represents a Blaze widget view for moments with a grid layout on a vertical axis",
        navDestinationId = R.id.action_navigation_widget_list_to_momentsGridFragment
    ),
    VIDEOS_ROW(
        title = "Videos-row",
        description = "This represents a Blaze widget view for videos with a row layout on a horizontal axis",
        navDestinationId = R.id.action_navigation_widget_list_to_videosRowFragment
    ),
    VIDEOS_GRID(
        title = "Videos-grid",
        description = "This represents a Blaze widget view for videos with a grid layout on a horizontal axis",
        navDestinationId = R.id.action_navigation_widget_list_to_videosGridFragment
    ),
    MIXED_WIDGETS(
        title = "Mixed-widgets",
        description = "This represents a feed with multiple widgets on the same screen with refresh functionality",
        navDestinationId = R.id.action_navigation_widget_list_to_mixedWidgetsFragment
    );
}