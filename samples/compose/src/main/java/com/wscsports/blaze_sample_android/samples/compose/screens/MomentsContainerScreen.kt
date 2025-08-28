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
import com.blaze.blazesdk.ads.models.ui.BlazeMomentsAdsConfigType
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.features.moments.container.compose.BlazeMomentsPlayerContainerCompose
import com.blaze.blazesdk.features.moments.container.compose.BlazeMomentsPlayerContainerComposeStateHandler
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.MomentsContainerDelegateImpl
import com.wscsports.blaze_sample_android.samples.compose.ComposeViewModel

private const val MOMENTS_CONTAINER_COMPOSE_ID = "compose-container-moments-id"

@Composable
fun MomentsContainerScreen(viewModel: ComposeViewModel) {
    val momentsContainerStateHandler = remember {
        BlazeMomentsPlayerContainerComposeStateHandler(
            dataSource = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_WIDGET_DEFAULT_LABEL)),
            playerInContainerDelegate = MomentsContainerDelegateImpl(),
            shouldOrderMomentsByReadStatus = true,
            cachePolicyLevel = BlazeCachingLevel.DEFAULT,
            containerId = MOMENTS_CONTAINER_COMPOSE_ID,
            momentsPlayerStyle = BlazeMomentsPlayerStyle.base().apply { buttons.exit.isVisible = false },
            momentsAdsConfigType = BlazeMomentsAdsConfigType.NONE // Set No ads for container
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.onVolumeChangedEvent.collect {
            momentsContainerStateHandler.onVolumeChanged()
        }
    }
    
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        BlazeMomentsPlayerContainerCompose(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            stateHandler = momentsContainerStateHandler,
        )
    }

}