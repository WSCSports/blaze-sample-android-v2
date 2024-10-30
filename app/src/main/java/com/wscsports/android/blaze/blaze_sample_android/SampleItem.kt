package com.wscsports.android.blaze.blaze_sample_android


enum class SampleItem(
    val title: String,
    val subTitle: String,
    val imageResourceId: Int,
    val className: String? = null
)   {
    GLOBAL_OPERATIONS("Global operations", "Global SDK properties setup", imageResourceId = R.drawable.ic_list_widgets, "globaloperations.GlobalOperationsActivity"),
    WIDGETS("Widgets", "Integrate all type of widgets", imageResourceId = R.drawable.ic_list_widgets, "widgets.WidgetsActivity"),
    ENTRY_POINT("Entry point", "Deeplink handling", imageResourceId = R.drawable.ic_list_widgets),
    MOMENT_CONTAINER("Moment container", "Embedding and managing moments in your app", imageResourceId = R.drawable.ic_list_moment_container),
    STORY_PLAYER("Story player", "Custom media player", imageResourceId = R.drawable.ic_list_story_player),
    MOMENT_PLAYER("Moment player", "Custom media player", imageResourceId = R.drawable.ic_list_moment_player),
    ADS("Ads", "SDK integration of banners, custom native and IMA ads", imageResourceId = R.drawable.ic_list_ads),
    COMPOSE("Compose", "Jetpack Compose widgets implementation", imageResourceId = R.drawable.ic_list_widgets),
    INTRODUCTION_SCREEN("Introduction screen", "Style configuration for the first-time slide", imageResourceId = R.drawable.ic_list_introduction_screen),
}
