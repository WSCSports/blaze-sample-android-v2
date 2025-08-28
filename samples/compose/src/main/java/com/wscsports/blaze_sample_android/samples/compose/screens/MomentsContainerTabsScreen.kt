package com.wscsports.blaze_sample_android.samples.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.tabs.compose.BlazeMomentsPlayerContainerTabsCompose
import com.blaze.blazesdk.features.moments.container.tabs.compose.BlazeMomentsPlayerContainerTabsComposeStateHandler
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.tabs.BlazePlayerTabsStyle
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.MomentsContainerTabsDelegateImpl
import com.wscsports.blaze_sample_android.samples.compose.ComposeViewModel


private const val MOMENTS_CONTAINER_TABS_SOURCE_ID = "compose-container-moments-tabs"
private const val MOMENTS_CONTAINER_TABS_COMPOSE_ID_1 = "tabs-compose-1"
private const val MOMENTS_CONTAINER_TABS_COMPOSE_ID_2 = "tabs-compose-2"

@Composable
fun MomentsContainerTabsScreen(viewModel: ComposeViewModel) {
    val momentsContainerTabsStateHandler = remember {
        BlazeMomentsPlayerContainerTabsComposeStateHandler(
            containerSourceId = MOMENTS_CONTAINER_TABS_SOURCE_ID,
            containerTabsDelegate = MomentsContainerTabsDelegateImpl(),
            tabs = listOf(
                BlazeMomentsContainerTabItem(
                    containerId = MOMENTS_CONTAINER_TABS_COMPOSE_ID_1,
                    title = "For You",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL)),
                ),
                BlazeMomentsContainerTabItem(
                    containerId = MOMENTS_CONTAINER_TABS_COMPOSE_ID_2,
                    title = "Trending",
                    dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)),
                ),
            ),
            playerStyle = BlazeMomentsPlayerStyle.base().apply { 
                buttons.exit.isVisible = false
            },
            tabsStyle = BlazePlayerTabsStyle.base()
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.onVolumeChangedEvent.collect {
            momentsContainerTabsStateHandler.onVolumeChanged()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF000000))
    ) {
        BlazeMomentsPlayerContainerTabsCompose(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            stateHandler = momentsContainerTabsStateHandler,
        )
    }

}
