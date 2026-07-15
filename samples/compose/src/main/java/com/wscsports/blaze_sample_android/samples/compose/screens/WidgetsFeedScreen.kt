package com.wscsports.blaze_sample_android.samples.compose.screens

import android.graphics.Color
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.compose.BlazeComposeWidgetMomentsStateHandler
import com.blaze.blazesdk.features.moments.widgets.compose.row.BlazeComposeMomentsWidgetRowView
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsConfiguration
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.blaze.blazesdk.features.stories.widgets.compose.BlazeComposeWidgetStoriesStateHandler
import com.blaze.blazesdk.features.stories.widgets.compose.grid.BlazeComposeStoriesWidgetGridView
import com.blaze.blazesdk.features.stories.widgets.compose.row.BlazeComposeStoriesWidgetRowView
import com.blaze.blazesdk.follow.models.BlazeFollowEntityType
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.compose.ComposeTopBar
import com.wscsports.blaze_sample_android.samples.compose.WidgetsFeedViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR

private const val YOUR_PICKS_CONTAINER_ID = "your-picks-tab"

@Composable
fun WidgetsFeedScreen(
    onTopBarBackPressed: () -> Unit,
    widgetsFeedViewModel: WidgetsFeedViewModel = viewModel(factory = WidgetsFeedViewModel.Factory),
) {
    val scrollState = rememberScrollState()

    val composeWidgetDelegate = remember { WidgetDelegateImpl() }

    val storiesRowStateHandler = remember {
        BlazeComposeWidgetStoriesStateHandler(
            widgetId = "compose-stories-row-widget-id",
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Row.circles,
            playerStyle = BlazeStoryPlayerStyle.base(),
            dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)),
            // If a widget with this identifier exists in the CMS, its layout (UI) and data source are derived from there.
            widgetRemoteIdentifier = "<ID_FROM_CMS>",
            widgetDelegate = composeWidgetDelegate
        )
    }

    val storiesGridStateHandler = remember {
        BlazeComposeWidgetStoriesStateHandler(
            widgetId = "compose-stories-Grid-widget-id",
            widgetLayout = BlazeWidgetLayout.Presets.StoriesWidget.Grid.twoColumnsVerticalRectangles,
            playerStyle = BlazeStoryPlayerStyle.base(),
            dataSourceType = BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)),
            // If a widget with this identifier exists in the CMS, its layout (UI) and data source are derived from there.
            widgetRemoteIdentifier = "<ID_FROM_CMS>",
            widgetDelegate = composeWidgetDelegate
        )
    }

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
                widgetStoriesStateHandler = storiesRowStateHandler
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Moments Follow Tabs", modifier = Modifier.padding(16.dp))
            MomentsFollowTabsWidget(
                yourPicksDataSource = widgetsFeedViewModel.yourPicksDataSource,
                widgetDelegate = composeWidgetDelegate,
                modifier = Modifier
                    .height(360.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Compose Stories Grid ", modifier = Modifier.padding(16.dp))
            BlazeComposeStoriesWidgetGridView(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                widgetStoriesStateHandler = storiesGridStateHandler
            )
        }
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
                    widgetId = "compose-moments-follow-tabs-widget-id",
                    widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
                    tabsConfiguration = makeMomentsFollowTabsConfiguration(state, accentColor),
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
 * Owns the in-session follow-refresh behavior, mirroring the Widgets sample's
 * MixedWidgetsFragment so both samples stay aligned:
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
            isYourPicksTabActive = sourceId?.endsWith(YOUR_PICKS_CONTAINER_ID) == true
        }

        override fun onTabSelected(playerType: BlazePlayerType, sourceId: String?, tabIndex: Int) {
            isYourPicksTabActive = sourceId?.endsWith(YOUR_PICKS_CONTAINER_ID) == true
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

private fun makeYourPicksTab(dataSource: BlazeDataSourceType) = BlazeMomentsContainerTabItem(
    containerId = YOUR_PICKS_CONTAINER_ID,
    title = "Your Picks",
    dataSource = dataSource
)

private fun makeMomentsFollowTabsConfiguration(
    tabsState: MomentsFollowTabsWidgetState,
    accentColor: Int,
) = BlazeMomentsWidgetTabsConfiguration(
    containerSourceId = "compose-moments-follow-tabs-container",
    containerTabsDelegate = tabsState.containerTabsDelegate,
    tabs = listOf(
        BlazeMomentsContainerTabItem(
            containerId = "trending-tab",
            title = "Trending",
            dataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL)
            ),
        ),
        BlazeMomentsContainerTabItem(
            containerId = "for-you-tab",
            title = "For you",
            dataSource = BlazeDataSourceType.Labels(
                blazeWidgetLabel = BlazeWidgetLabel.singleLabel(MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL)
            )
        ),
        tabsState.yourPicksTab,
    ),
    playerStyle = makeFollowMomentsPlayerStyle(accentColor),
    controller = tabsState.momentsTabsController
)

/**
 * Shows the follow button in the moments player. The entity offered to follow
 * is resolved in a fallback order: player -> team -> property.
 * The followed state is highlighted so it clearly stands out from the unfollowed one.
 */
private fun makeFollowMomentsPlayerStyle(accentColor: Int): BlazeMomentsPlayerStyle =
    BlazeMomentsPlayerStyle.base().apply {
        followEntity.isVisible = true
        followEntity.entityType = BlazeFollowEntityType.Player(
            fallbackType = BlazeFollowEntityType.Team(
                fallbackType = BlazeFollowEntityType.Property(
                    fallbackType = null
                )
            )
        )
        followEntity.followState.avatar.borderColor = accentColor
        followEntity.followState.chip.backgroundColor = accentColor
        followEntity.followState.chip.iconColor = Color.BLACK
    }