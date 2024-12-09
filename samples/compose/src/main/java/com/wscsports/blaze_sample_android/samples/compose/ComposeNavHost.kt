package com.wscsports.blaze_sample_android.samples.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wscsports.blaze_sample_android.samples.compose.screens.MomentsContainerScreen
import com.wscsports.blaze_sample_android.samples.compose.screens.WidgetsFeedScreen
import kotlinx.serialization.Serializable

@Composable
fun ComposeNavHost(
    navController: NavHostController,
    viewModel: ComposeViewModel,
    onTopBarBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = NavHostScreens.WidgetsFeed, modifier = modifier) {
        composable<NavHostScreens.WidgetsFeed> {
            WidgetsFeedScreen(viewModel = viewModel, onTopBarBackPressed = onTopBarBackPressed)
        }
        composable<NavHostScreens.MomentsContainer> {
            MomentsContainerScreen(viewModel)
        }
    }
}

@Serializable
sealed class NavHostScreens(val labelName: String) {

    @Serializable
    data object WidgetsFeed : NavHostScreens(labelName = "Widgets Feed")

    @Serializable
    data object MomentsContainer : NavHostScreens(labelName = "Moments Container")

    fun getBottomNavIconImage(): ImageVector {
        return when (this) {
            is WidgetsFeed -> Icons.Default.Home
            is MomentsContainer -> Icons.Default.PlayArrow
        }
    }
}