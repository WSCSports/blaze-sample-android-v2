package com.wscsports.blaze_sample_android.samples.search.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.stories.widgets.compose.BlazeComposeWidgetStoriesStateHandler
import com.blaze.blazesdk.features.stories.widgets.compose.row.BlazeComposeStoriesWidgetRowView
import com.blaze.blazesdk.features.videos.widgets.compose.BlazeComposeWidgetVideosStateHandler
import com.blaze.blazesdk.features.videos.widgets.compose.row.BlazeComposeVideosWidgetRowView
import com.wscsports.blaze_sample_android.samples.search.SearchUIState

/**
 * Content composable for displaying search results in three sections:
 * - Stories
 * - Quick Highlights (Moments)
 * - Full Videos
 *
 * Each section uses existing SDK Compose widgets with search data source.
 */
@Composable
fun SearchResultsContent(
    resultsState: SearchUIState.Results,
    storiesStateHandler: BlazeComposeWidgetStoriesStateHandler,
    momentsStateHandler: BlazeComposeWidgetMomentsStateHandler,
    videosStateHandler: BlazeComposeWidgetVideosStateHandler,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SearchResultSection(
            title = "Stories",
            isVisible = resultsState.showStories
        ) {
            BlazeComposeStoriesWidgetRowView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                widgetStoriesStateHandler = storiesStateHandler,
                supportsNestedHorizontalScroll = true
            )
        }

        SearchResultSection(
            title = "Quick Highlights",
            isVisible = resultsState.showMoments
        ) {
            BlazeComposeMomentsWidgetRowView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                widgetMomentsStateHandler = momentsStateHandler,
                supportsNestedHorizontalScroll = true
            )
        }

        SearchResultSection(
            title = "Full Videos",
            isVisible = resultsState.showVideos
        ) {
            BlazeComposeVideosWidgetRowView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                widgetVideosStateHandler = videosStateHandler,
                supportsNestedHorizontalScroll = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * A single search result section with a title and widget content.
 *
 * The widget composable ([content]) is always composed so it participates in
 * the composition lifecycle and can load data. The title and visual wrapping are
 * shown/hidden via [AnimatedVisibility] based on [isVisible].
 */
@Composable
private fun SearchResultSection(
    title: String,
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = title,
                color = SearchDefaults.textPrimary,
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight(600)),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            content()
        }
    }
}
