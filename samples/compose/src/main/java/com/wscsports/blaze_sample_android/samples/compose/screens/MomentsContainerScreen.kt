package com.wscsports.blaze_sample_android.samples.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.blaze.blazesdk.features.moments.container.compose.BlazeMomentsPlayerContainerCompose
import com.wscsports.blaze_sample_android.samples.compose.ComposeViewModel

@Composable
fun MomentsContainerScreen(viewModel: ComposeViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        BlazeMomentsPlayerContainerCompose(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            stateHandler = viewModel.momentsContainerStateHandler,
        )
    }

}