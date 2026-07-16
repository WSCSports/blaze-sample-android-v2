package com.wscsports.blaze_sample_android.samples.follow

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
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
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
 * "Your Picks" is personalized from the followed entities.
 *
 * The widget appears on the first data source emission and is fully recreated (fresh
 * state handler and tab items, driven by [MomentsFollowTabsWidgetState.widgetGeneration])
 * on every follow change — the SDK removes a tab that loaded empty, so fresh tab items
 * are needed to re-add it once content exists.
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
                tabsState = MomentsFollowTabsWidgetState(dataSource)
            } else {
                tabsState?.onYourPicksDataSourceChanged(
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
        tabsState?.onResumed()
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
                        yourPicksTab = state.yourPicksTab,
                        containerTabsDelegate = state.containerTabsDelegate,
                        controller = state.momentsTabsController,
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
 * Owns the in-session follow-refresh behavior, mirroring [FollowTabsFragment]
 * so both variants stay aligned:
 * - a follow change while the widget is visible recreates it right away,
 * - a change made inside the player reloads the non-active tabs in the background
 *   (active playback stays untouched) and defers the full recreation to the return,
 * - a change made while watching "Your Picks" itself is applied the moment the user
 *   switches to another tab, so returning to it in-session already shows fresh content.
 *
 * Honors the [Stable] contract: the public mutable properties are snapshot-backed,
 * so composition is notified of every change.
 */
@Stable
private class MomentsFollowTabsWidgetState(
    initialYourPicksDataSource: BlazeDataSourceType.Labels,
) {

    /** Bumped to force a full widget recreation — the Compose analog of re-calling initWidget. */
    var widgetGeneration by mutableIntStateOf(0)
        private set

    val momentsTabsController = BlazeMomentsWidgetTabsController()

    private val yourPicksLiveDataSource: BlazeDataSourceType.Labels = initialYourPicksDataSource

    var yourPicksTab by mutableStateOf(makeYourPicksTab(initialYourPicksDataSource))
        private set

    private var hasPendingWidgetReinit = false
    private var hasPendingYourPicksReload = false
    private var isYourPicksTabActive = false

    /**
     * Tracks whether "Your Picks" is the tab the user is currently watching — the active
     * tab must never be reloaded mid-playback.
     */
    val containerTabsDelegate = object : BlazePlayerContainerTabsDelegate {

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            isYourPicksTabActive = sourceId == yourPicksSourceId(CONTAINER_SOURCE_ID)
        }

        override fun onTabSelected(playerType: BlazePlayerType, sourceId: String?, tabIndex: Int) {
            isYourPicksTabActive = sourceId == yourPicksSourceId(CONTAINER_SOURCE_ID)
            if (!isYourPicksTabActive && hasPendingYourPicksReload) {
                hasPendingYourPicksReload = false
                momentsTabsController.reloadNonActiveTabs()
            }
        }
    }

    fun onYourPicksDataSourceChanged(dataSource: BlazeDataSourceType.Labels, isResumed: Boolean) {
        yourPicksLiveDataSource.blazeWidgetLabel = dataSource.blazeWidgetLabel
        yourPicksLiveDataSource.labelsPriority = dataSource.labelsPriority
        yourPicksTab = makeYourPicksTab(yourPicksLiveDataSource)
        when {
            // Widget visible, no player on top -> rebuild it with the fresh tabs right away.
            isResumed -> recreateWidget()
            // Follow changed while watching another tab -> "Your Picks" refetches in the
            // background (active playback untouched); full recreation still runs on return.
            !isYourPicksTabActive -> {
                momentsTabsController.reloadNonActiveTabs()
                hasPendingWidgetReinit = true
            }
            // Follow changed on "Your Picks" itself -> it can't be reloaded while watched;
            // it reloads as soon as the user switches away (see onTabSelected) and the
            // recreation on return keeps the guarantee if they never switch.
            else -> {
                hasPendingYourPicksReload = true
                hasPendingWidgetReinit = true
            }
        }
    }

    fun onResumed() {
        if (hasPendingWidgetReinit) {
            hasPendingWidgetReinit = false
            recreateWidget()
        }
    }

    private fun recreateWidget() {
        // A full recreation rebuilds every tab fresh, so no in-session reload is owed anymore.
        hasPendingYourPicksReload = false
        widgetGeneration++
    }
}
