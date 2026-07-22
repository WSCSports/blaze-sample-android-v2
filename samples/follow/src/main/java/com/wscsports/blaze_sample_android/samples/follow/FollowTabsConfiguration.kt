package com.wscsports.blaze_sample_android.samples.follow

import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsConfiguration
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.blaze.blazesdk.follow.models.BlazeFollowEntityType
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL

internal const val YOUR_PICKS_CONTAINER_ID = "your-picks-tab"

/**
 * The SDK reports tab callbacks (onPlayerDidAppear/onTabSelected) with
 * sourceId in the "{containerSourceId}_{containerId}" format.
 */
internal fun yourPicksSourceId(containerSourceId: String) =
    "${containerSourceId}_$YOUR_PICKS_CONTAINER_ID"

internal fun makeYourPicksTab(dataSource: BlazeDataSourceType) = BlazeMomentsContainerTabItem(
    containerId = YOUR_PICKS_CONTAINER_ID,
    title = "Your Picks",
    dataSource = dataSource
)

internal fun makeMomentsFollowTabsConfiguration(
    containerSourceId: String,
    yourPicksTab: BlazeMomentsContainerTabItem,
    containerTabsDelegate: BlazePlayerContainerTabsDelegate,
    controller: BlazeMomentsWidgetTabsController,
    accentColor: Int,
) = BlazeMomentsWidgetTabsConfiguration(
    containerSourceId = containerSourceId,
    containerTabsDelegate = containerTabsDelegate,
    tabs = listOf(
        BlazeMomentsContainerTabItem(
            containerId = "trending-tab",
            title = "Trending",
            dataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL)
            ),
        ),
        BlazeMomentsContainerTabItem(
            containerId = "for-you-tab",
            title = "For you",
            dataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)
            ),

        ),
        yourPicksTab,
    ),
    playerStyle = makeFollowMomentsPlayerStyle(accentColor),
    controller = controller
)

/**
 * Shows the follow button in the moments player. The entity offered to follow
 * is resolved in a fallback order: player -> team -> property.
 * The followed state is highlighted so it clearly stands out from the unfollowed one.
 */
private fun makeFollowMomentsPlayerStyle(accentColor: Int): BlazeMomentsPlayerStyle =
    BlazeMomentsPlayerStyle.base().apply {
        followEntity.isVisible = true
        followEntity.entityType = BlazeFollowEntityType.Player(
            fallbackType = BlazeFollowEntityType.Team(
                fallbackType = BlazeFollowEntityType.Property(
                    fallbackType = null
                )
            )
        )
        followEntity.followState.avatar.borderColor = accentColor
        followEntity.followState.chip.backgroundColor = accentColor
        followEntity.followState.chip.iconColor = android.graphics.Color.BLACK
    }
