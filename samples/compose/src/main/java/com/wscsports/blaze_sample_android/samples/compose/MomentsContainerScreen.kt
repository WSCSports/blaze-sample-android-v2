package com.wscsports.blaze_sample_android.samples.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.blaze.blazesdk.features.moments.container.compose.BlazeMomentsPlayerContainerCompose

@Composable
fun MomentsContainerScreen(viewModel: ComposeViewModel) {
    BlazeMomentsPlayerContainerCompose(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        stateHandler = viewModel.momentsContainerStateHandler,
    )

}