package com.wscsports.blaze_sample_android.samples.follow

import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController

/**
 * Refreshes the personalized "Your Picks" tab when the followed entities change, shared by
 * the View ([com.wscsports.blaze_sample_android.samples.follow.views.FollowTabsFragment])
 * and Compose ([com.wscsports.blaze_sample_android.samples.follow.compose.FollowTabsScreen])
 * variants so the behavior lives in one place. A follow change is applied depending on where
 * the user is:
 * - widget on screen -> rebuild it with the updated tabs right away;
 * - in the player on another tab -> refresh "Your Picks" in the background;
 * - in the player watching "Your Picks" -> apply it once the user switches away (so playback
 *   isn't interrupted), and rebuild on return.
 *
 * [rebuildWidget] is the only framework-specific hook: the View variant re-calls initWidget,
 * the Compose variant triggers recomposition.
 */
internal class YourPicksRefreshCoordinator(
    containerSourceId: String,
    initialDataSource: BlazeDataSourceType.Labels,
    private val rebuildWidget: () -> Unit,
) {

    val tabsController = BlazeMomentsWidgetTabsController()

    private val yourPicksSourceId = yourPicksSourceId(containerSourceId)

    /** The latest "Your Picks" data source; used to rebuild the widget. */
    private var latestDataSource: BlazeDataSourceType.Labels = initialDataSource

    /** The data source the open player is showing; updated in place for an in-session refresh. */
    private var containerDataSource: BlazeDataSourceType.Labels = initialDataSource

    private var hasPendingWidgetReinit = false
    private var hasPendingYourPicksReload = false
    private var isYourPicksTabActive = false

    /**
     * Tracks the tab the user is currently watching, so the active tab is never reloaded
     * mid-playback, and refreshes "Your Picks" the moment the user leaves it if a change was
     * made while it was active.
     */
    val containerTabsDelegate = object : BlazePlayerContainerTabsDelegate {
        override fun onTabSelected(playerType: BlazePlayerType, sourceId: String?, tabIndex: Int) {
            isYourPicksTabActive = sourceId == yourPicksSourceId
            if (!isYourPicksTabActive && hasPendingYourPicksReload) {
                hasPendingYourPicksReload = false
                reloadYourPicks()
            }
        }
    }

    /** A fresh instance per rebuild, so the widget reloads with the current query. */
    fun buildYourPicksTab(): BlazeMomentsContainerTabItem {
        containerDataSource = latestDataSource.copy()
        return makeYourPicksTab(containerDataSource)
    }

    /** Call on every follow change after the first data source emission. */
    fun onYourPicksChanged(dataSource: BlazeDataSourceType.Labels, isResumed: Boolean) {
        latestDataSource = dataSource
        when {
            // Widget on screen, no player on top -> rebuild it with the fresh tabs right away.
            isResumed -> rebuildWidget()
            // In the player watching another tab -> refresh "Your Picks" in the background now,
            // and rebuild on return so a tab removed while empty comes back.
            !isYourPicksTabActive -> {
                reloadYourPicks()
                hasPendingWidgetReinit = true
            }
            // In the player watching "Your Picks" -> apply it when the user switches away
            // (see the delegate), with the rebuild on return as the backstop.
            else -> {
                hasPendingYourPicksReload = true
                hasPendingWidgetReinit = true
            }
        }
    }

    /** Call from `onResume()` / `LifecycleResumeEffect` to apply a change made while the player was open. */
    fun onResumed() {
        if (hasPendingWidgetReinit) {
            hasPendingWidgetReinit = false
            rebuildWidget()
        }
    }

    private fun reloadYourPicks() {
        // Apply the latest query to the shown data source, then refresh the non-active tabs.
        // Keep these fields in sync with the "Your Picks" data source built in FollowViewModel.
        containerDataSource.blazeWidgetLabel = latestDataSource.blazeWidgetLabel
        containerDataSource.labelsPriority = latestDataSource.labelsPriority
        tabsController.reloadNonActiveTabs()
    }
}
