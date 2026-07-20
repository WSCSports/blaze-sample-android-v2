package com.wscsports.blaze_sample_android.samples.follow

import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController

/**
 * Framework-agnostic coordinator for the "Your Picks" refresh behavior, shared by the
 * View ([com.wscsports.blaze_sample_android.samples.follow.views.FollowTabsFragment])
 * and Compose ([com.wscsports.blaze_sample_android.samples.follow.compose.FollowTabsScreen])
 * variants so the choreography lives in one place.
 *
 * The SDK only re-evaluates the tab set on a full widget (re)build, so a follow change
 * is applied depending on where the user is:
 * - widget visible, no player on top -> rebuild it with the fresh tabs right away;
 * - inside the player, watching another tab -> reload the non-active tabs against the
 *   in-place-mutated data source (active playback stays untouched) and defer the full
 *   rebuild to the return;
 * - inside the player, watching "Your Picks" itself -> it can't be reloaded while
 *   watched, so apply it the moment the user switches away, with the rebuild on return
 *   as the backstop.
 *
 * [rebuildWidget] is the only framework-specific hook: the View variant re-calls
 * `initWidget`, the Compose variant bumps a recomposition trigger.
 */
internal class YourPicksRefreshCoordinator(
    containerSourceId: String,
    initialDataSource: BlazeDataSourceType.Labels,
    private val rebuildWidget: () -> Unit,
) {

    val tabsController = BlazeMomentsWidgetTabsController()

    private val yourPicksSourceId = yourPicksSourceId(containerSourceId)
    private val yourPicksLiveDataSource: BlazeDataSourceType.Labels = initialDataSource

    private var hasPendingWidgetReinit = false
    private var hasPendingYourPicksReload = false
    private var isYourPicksTabActive = false

    /**
     * Tracks whether "Your Picks" is the tab the user is currently watching — the active
     * tab must never be reloaded mid-playback.
     */
    val containerTabsDelegate = object : BlazePlayerContainerTabsDelegate {

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            isYourPicksTabActive = sourceId == yourPicksSourceId
        }

        override fun onTabSelected(playerType: BlazePlayerType, sourceId: String?, tabIndex: Int) {
            isYourPicksTabActive = sourceId == yourPicksSourceId
            if (!isYourPicksTabActive && hasPendingYourPicksReload) {
                hasPendingYourPicksReload = false
                tabsController.reloadNonActiveTabs()
            }
        }
    }

    /** The current "Your Picks" tab, always wrapping the up-to-date data source. */
    fun buildYourPicksTab(): BlazeMomentsContainerTabItem = makeYourPicksTab(yourPicksLiveDataSource)

    /** Call on every follow change after the first data source emission. */
    fun onYourPicksChanged(dataSource: BlazeDataSourceType.Labels, isResumed: Boolean) {
        yourPicksLiveDataSource.blazeWidgetLabel = dataSource.blazeWidgetLabel
        yourPicksLiveDataSource.labelsPriority = dataSource.labelsPriority
        when {
            isResumed -> rebuildNow()
            !isYourPicksTabActive -> {
                tabsController.reloadNonActiveTabs()
                hasPendingWidgetReinit = true
            }
            else -> {
                hasPendingYourPicksReload = true
                hasPendingWidgetReinit = true
            }
        }
    }

    /** Call from `onResume()` / `LifecycleResumeEffect` to apply a change deferred while the player was open. */
    fun onResumed() {
        if (hasPendingWidgetReinit) {
            hasPendingWidgetReinit = false
            rebuildNow()
        }
    }

    private fun rebuildNow() {
        // A full rebuild recreates every tab fresh, so no in-session reload is owed anymore.
        hasPendingYourPicksReload = false
        rebuildWidget()
    }
}
