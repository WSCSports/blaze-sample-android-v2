package com.wscsports.blaze_sample_android.samples.compose

import androidx.lifecycle.ViewModel
import com.blaze.blazesdk.ads.custom_native.models.BlazeMomentsAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.compose.BlazeMomentsPlayerContainerComposeStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.stories.widgets.compose.BlazeComposeWidgetStoriesStateHandler
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.android.blaze.blaze_sample_android.core.MomentsContainerDelegateImpl
import com.wscsports.android.blaze.blaze_sample_android.core.WidgetDelegateImpl
/**
 * ViewModel for the Compose screen.
 * Note: To manage live state changes, the handler should be wrapped as a stateful object for the recomposition to happen.
 */
class ComposeViewModel: ViewModel() {

    private val composeWidgetDelegate = WidgetDelegateImpl()
    private val composeMomentsContainerDelegate = MomentsContainerDelegateImpl()

    val storiesRowStateHandler = BlazeComposeWidgetStoriesStateHandler(
        widgetId = "compose-stories-row-widget-id",
        widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles,
        playerStyle = BlazeStoryPlayerStyle.base(),
        dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("live-stories")),
        widgetDelegate = composeWidgetDelegate
    )

    val momentsRowStateHandler = BlazeComposeWidgetMomentsStateHandler(
        widgetId = "compose-moments-row-widget-id",
        widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
        playerStyle = BlazeMomentsPlayerStyle.base(),
        dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("moments")),
        widgetDelegate = composeWidgetDelegate
    )

    val storiesGridStateHandler = BlazeComposeWidgetStoriesStateHandler(
        widgetId = "compose-stories-Grid-widget-id",
        widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles,
        playerStyle = BlazeStoryPlayerStyle.base(),
        dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("top-stories")),
        widgetDelegate = composeWidgetDelegate
    )

    val momentsContainerStateHandler = BlazeMomentsPlayerContainerComposeStateHandler(
        dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel("moments")),
        playerInContainerDelegate = composeMomentsContainerDelegate,
        shouldOrderMomentsByReadStatus = true,
        cachePolicyLevel = BlazeCachingLevel.DEFAULT,
        containerId = "compose-container-moments-id",
        momentsPlayerStyle = BlazeMomentsPlayerStyle.base().apply { buttons.exit.isVisible = false },
        momentsAdsConfigType = BlazeMomentsAdsConfigType.NONE // Set No ads for container
    )

    fun onVolumeChanged() {
        momentsContainerStateHandler.onVolumeChanged()
    }

}