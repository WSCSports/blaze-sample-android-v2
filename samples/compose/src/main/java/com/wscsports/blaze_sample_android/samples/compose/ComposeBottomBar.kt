package com.wscsports.blaze_sample_android.samples.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wscsports.blaze_sample_android.core.ui.R
import com.wscsports.blaze_sample_android.samples.compose.NavHostScreens.MomentsContainer
import com.wscsports.blaze_sample_android.samples.compose.NavHostScreens.WidgetsFeed
import com.wscsports.blaze_sample_android.samples.compose.NavHostScreens.MomentsContainerTabs

@Composable
fun ComposeBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.shadow(8.dp)
    ) {
        bottomNavigationItems.forEach { item ->
            val isSelected = currentRoute == item::class.qualifiedName
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.getBottomNavIconImage(),
                        contentDescription = item.labelName
                    )
                },
                label = { Text(item.labelName) },
            )
        }
    }
}

@Composable
private fun NavHostScreens.getBottomNavIconImage(): ImageVector {
    return when (this) {
        is WidgetsFeed -> ImageVector.vectorResource(R.drawable.ic_widgets)
        is MomentsContainer -> ImageVector.vectorResource(R.drawable.ic_moments_container)
        is MomentsContainerTabs -> ImageVector.vectorResource(R.drawable.ic_tabs)
    }
}

val bottomNavigationItems = listOf(WidgetsFeed, MomentsContainer, MomentsContainerTabs)
