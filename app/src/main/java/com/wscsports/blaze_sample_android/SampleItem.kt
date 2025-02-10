package com.wscsports.blaze_sample_android

enum class SampleItem(
    val title: String,
    val subTitle: String,
    val imageResourceId: Int,
    val classPathSuffix: String? = null
)   {
    GLOBAL_OPERATIONS("Global operations", "Global SDK properties setup", imageResourceId = R.drawable.ic_list_widgets, "globaloperations.GlobalOperationsActivity"),
    WIDGETS("Widgets", "Integrate all type of widgets", imageResourceId = R.drawable.ic_list_widgets, "widgets.WidgetsActivity"),
    MOMENT_CONTAINER("Moment container", "Embedding and managing moments in your app", imageResourceId = R.drawable.ic_list_moment_container, "momentscontainer.MomentsContainerActivity"),
    ENTRY_POINT("Entry point", "Deeplink handling", imageResourceId = R.drawable.ic_list_widgets, "entrypoint.EntryPointActivity"),
    PLAYER_STYLE("Player style", "Custom media player", imageResourceId = R.drawable.ic_list_story_player, "playerstyle.PlayerStyleActivity"),
    ADS("Ads", "SDK integration of banners, custom native and IMA ads", imageResourceId = R.drawable.ic_list_ads, "ads.AdsSampleActivity"),
    COMPOSE("Compose", "Jetpack Compose widgets implementation", imageResourceId = R.drawable.ic_list_widgets, "compose.ComposeActivity");
}
