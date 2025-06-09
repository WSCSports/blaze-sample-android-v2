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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.stories.widgets.compose.grid.BlazeComposeStoriesWidgetGridView
import com.blaze.blazesdk.features.stories.widgets.compose.row.BlazeComposeStoriesWidgetRowView
import com.wscsports.blaze_sample_android.samples.compose.ComposeTopBar
import com.wscsports.blaze_sample_android.samples.compose.ComposeViewModel

@Composable
fun WidgetsFeedScreen(
    viewModel: ComposeViewModel,
    onTopBarBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
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
                widgetStoriesStateHandler = viewModel.storiesRowStateHandler
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Moments Row", modifier = Modifier.padding(16.dp))
            BlazeComposeMomentsWidgetRowView(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
                widgetMomentsStateHandler = viewModel.momentsRowStateHandler
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Stories Grid ", modifier = Modifier.padding(16.dp))
            BlazeComposeStoriesWidgetGridView(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                widgetStoriesStateHandler = viewModel.storiesGridStateHandler
            )
        }
    }
}