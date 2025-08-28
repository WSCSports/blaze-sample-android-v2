package com.wscsports.blaze_sample_android.samples.compose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.stories.widgets.compose.BlazeComposeWidgetStoriesStateHandler
import com.blaze.blazesdk.features.stories.widgets.compose.grid.BlazeComposeStoriesWidgetGridView
import com.blaze.blazesdk.features.stories.widgets.compose.row.BlazeComposeStoriesWidgetRowView
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.compose.ComposeTopBar
import com.wscsports.blaze_sample_android.samples.compose.ComposeViewModel

@Composable
fun WidgetsFeedScreen(
    onTopBarBackPressed: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val composeWidgetDelegate = remember { WidgetDelegateImpl() }
    
    val storiesRowStateHandler = remember {
        BlazeComposeWidgetStoriesStateHandler(
            widgetId = "compose-stories-row-widget-id",
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles,
            playerStyle = BlazeStoryPlayerStyle.base(),
            dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)),
            widgetDelegate = composeWidgetDelegate
        )
    }

    val momentsRowStateHandler = remember {
        BlazeComposeWidgetMomentsStateHandler(
            widgetId = "compose-moments-row-widget-id",
            widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
            playerStyle = BlazeMomentsPlayerStyle.base(),
            dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_WIDGET_DEFAULT_LABEL)),
            widgetDelegate = composeWidgetDelegate
        )
    }

    val storiesGridStateHandler = remember {
        BlazeComposeWidgetStoriesStateHandler(
            widgetId = "compose-stories-Grid-widget-id",
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles,
            playerStyle = BlazeStoryPlayerStyle.base(),
            dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)),
            widgetDelegate = composeWidgetDelegate
        )
    }
    
    Column {
        ComposeTopBar(
            title = "Compose",
            onBackPressed = onTopBarBackPressed,
        )
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(text = "Compose Stories Row ", modifier = Modifier.padding(16.dp))
            BlazeComposeStoriesWidgetRowView(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth(),
                widgetStoriesStateHandler = storiesRowStateHandler
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Moments Row", modifier = Modifier.padding(16.dp))
            BlazeComposeMomentsWidgetRowView(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                widgetMomentsStateHandler = momentsRowStateHandler
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Stories Grid ", modifier = Modifier.padding(16.dp))
            BlazeComposeStoriesWidgetGridView(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                widgetStoriesStateHandler = storiesGridStateHandler
            )
        }
    }
}