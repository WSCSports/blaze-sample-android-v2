package com.wscsports.blaze_sample_android.samples.follow.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.follow.FollowViewModel
import com.wscsports.blaze_sample_android.samples.follow.YourPicksRefreshCoordinator
import com.wscsports.blaze_sample_android.samples.follow.makeMomentsFollowTabsConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR

private const val CONTAINER_SOURCE_ID = "follow-moments-tabs-compose"

/** The Compose variant of the Moments Follow Tabs example. */
@Composable
fun FollowTabsScreen(
    followViewModel: FollowViewModel = viewModel(),
) {
    val composeWidgetDelegate = remember { WidgetDelegateImpl() }

    Column {
        Text(
            text = "Moments Follow Tabs",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        MomentsFollowTabsWidget(
            yourPicksDataSource = followViewModel.yourPicksDataSource,
            widgetDelegate = composeWidgetDelegate,
            modifier = Modifier
                .height(340.dp)
                .fillMaxWidth()
        )
    }
}

/**
 * The tabs-backed moments widget: "Trending" and "For you" use fixed labels, while
 * "Your Picks" is personalized from the followed entities. The follow-change
 * choreography lives in [YourPicksRefreshCoordinator]; this composable rebuilds the
 * widget whenever the coordinator bumps [MomentsFollowTabsWidgetState.widgetGeneration].
 *
 * Deliberately NOT a lifecycle-gated collection: follow changes are emitted while the
 * moments player (its own activity) covers this screen, which is exactly when the
 * in-session handling must run — and a restarted collection would treat every return
 * as the first emission, recreating the widget needlessly.
 */
@Composable
private fun MomentsFollowTabsWidget(
    yourPicksDataSource: Flow<BlazeDataSourceType.Labels>,
    widgetDelegate: BlazeWidgetDelegate,
    modifier: Modifier = Modifier,
) {
    val accentColor = colorResource(CoreUiR.color.wsc_accent).toArgb()
    var tabsState by remember { mutableStateOf<MomentsFollowTabsWidgetState?>(null) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        yourPicksDataSource.collectIndexed { index, dataSource ->
            if (index == 0) {
                tabsState = MomentsFollowTabsWidgetState(CONTAINER_SOURCE_ID, dataSource)
            } else {
                tabsState?.coordinator?.onYourPicksChanged(
                    dataSource = dataSource,
                    isResumed = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
                )
            }
        }
    }

    /**
     * Recreating the widget while the moments player is open would replace the content
     * under the user mid-playback, so follow changes made inside the player are applied
     * here, once the user comes back.
     */
    LifecycleResumeEffect(tabsState) {
        tabsState?.coordinator?.onResumed()
        onPauseOrDispose { }
    }

    tabsState?.let { state ->
        key(state.widgetGeneration) {
            val momentsTabsStateHandler = remember {
                BlazeComposeWidgetMomentsStateHandler(
                    widgetId = "follow-moments-tabs-compose-id",
                    widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
                    tabsConfiguration = makeMomentsFollowTabsConfiguration(
                        containerSourceId = CONTAINER_SOURCE_ID,
                        yourPicksTab = state.coordinator.buildYourPicksTab(),
                        containerTabsDelegate = state.coordinator.containerTabsDelegate,
                        controller = state.coordinator.tabsController,
                        accentColor = accentColor
                    ),
                    widgetDelegate = widgetDelegate
                )
            }
            BlazeComposeMomentsWidgetRowView(
                modifier = modifier,
                widgetMomentsStateHandler = momentsTabsStateHandler
            )
        }
    }
}

/**
 * Holds the one piece of state the composition observes — [widgetGeneration], the
 * recomposition trigger — and the shared [YourPicksRefreshCoordinator], whose
 * `rebuildWidget` hook bumps that counter. Everything else lives in the coordinator.
 *
 * [Stable] is honored: the only public mutable property is snapshot-backed.
 */
@Stable
private class MomentsFollowTabsWidgetState(
    containerSourceId: String,
    initialDataSource: BlazeDataSourceType.Labels,
) {

    var widgetGeneration by mutableIntStateOf(0)
        private set

    val coordinator = YourPicksRefreshCoordinator(
        containerSourceId = containerSourceId,
        initialDataSource = initialDataSource,
        rebuildWidget = { widgetGeneration++ },
    )
}
