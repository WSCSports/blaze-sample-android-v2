package com.wscsports.blaze_sample_android

import com.wscsports.blaze_sample_android.core.ui.R.*

enum class SampleItem(
    val title: Int,
    val subTitle: Int,
    val imageResourceId: Int,
    val classPathSuffix: String? = null
)   {
    GLOBAL_SETTINGS(string.global_settings_title, string.global_settings_subtitle, drawable.ic_settings, "globaloperations.GlobalOperationsActivity"),
    WIDGETS(string.widgets_title, string.widgets_subtitle, drawable.ic_widgets, "widgets.WidgetsActivity"),
    MOMENT_CONTAINER(string.moment_container_title, string.moment_container_subtitle, drawable.ic_moments_container, "momentscontainer.MomentsContainerActivity"),
    ENTRY_POINT(string.entry_point_title, string.entry_point_subtitle, drawable.ic_entry_point, "entrypoint.EntryPointActivity"),
    PLAYER_STYLE(string.player_style_title, string.player_style_subtitle, drawable.ic_player_style, "playerstyle.PlayerStyleActivity"),
    ADS(string.ads_title, string.ads_subtitle, drawable.ic_ads, "ads.AdsSampleActivity"),
    COMPOSE(string.compose_title, string.compose_subtitle, drawable.ic_compose, "compose.ComposeActivity");
}
