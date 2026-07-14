package com.wscsports.blaze_sample_android.samples.widgets.screens

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsConfiguration
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.blaze.blazesdk.follow.models.BlazeFollowEntityType
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_1_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.MOMENTS_CONTAINER_TABS_2_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.Constants.STORIES_WIDGET_DEFAULT_LABEL
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.samples.widgets.MixedWidgetsViewModel
import com.wscsports.blaze_sample_android.samples.widgets.R
import com.wscsports.blaze_sample_android.samples.widgets.WidgetsViewModel
import com.wscsports.blaze_sample_android.samples.widgets.databinding.FragmentMixedWidgetsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR

/**
 * MixedWidgetsFragment is a Fragment that displays a mix feed of Blaze widgets:
 * Stories-row, Moments follow tabs, and Stories-grid.
 * It manages reload widgets data with pull-to-refresh [SwipeRefreshLayout].
 *
 * The moments widget is initialized with three tabs, where the "Your Picks" tab
 * is built from the locally persisted followed entities. Follow changes swap that
 * tab's data source and refresh it in the background right away; the widget itself
 * is rebuilt with a fresh configuration once the player is dismissed.
 */
class MixedWidgetsFragment: Fragment(R.layout.fragment_mixed_widgets),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(FragmentMixedWidgetsBinding::bind)
    private val viewModel: WidgetsViewModel by activityViewModels()
    private val mixedWidgetsViewModel: MixedWidgetsViewModel by viewModels { MixedWidgetsViewModel.Factory }

    private val momentsTabsController = BlazeMomentsWidgetTabsController()

    /**
     * The SDK shares this instance: the container shallow-copies the tab item, so the
     * in-player content holder ends up referencing this very object. Follow changes are
     * applied by mutating its fields in place — never by replacing the instance — which
     * makes an in-session non-active tabs reload fetch the fresh query. A reload always
     * refetches the data source captured at prefetch, so this mutation can only go away
     * once the SDK offers a session-safe way to update a tab's data source in place.
     */
    private lateinit var yourPicksLiveDataSource: BlazeDataSourceType.Labels
    private lateinit var yourPicksTab: BlazeMomentsContainerTabItem

    private var hasPendingWidgetReinit = false
    private var hasPendingYourPicksReload = false
    private var isYourPicksTabActive = false

    /**
     * Tracks whether "Your Picks" is the tab the user is currently watching — the active
     * tab must never be reloaded mid-playback. A follow change made while watching
     * "Your Picks" is instead applied the moment the user switches to another tab,
     * so returning to it within the same player session already shows fresh content.
     */
    private val momentsContainerTabsDelegate = object : BlazePlayerContainerTabsDelegate {

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            isYourPicksTabActive = sourceId?.endsWith(YOUR_PICKS_CONTAINER_ID) == true
        }

        override fun onTabSelected(playerType: BlazePlayerType, sourceId: String?, tabIndex: Int) {
            isYourPicksTabActive = sourceId?.endsWith(YOUR_PICKS_CONTAINER_ID) == true
            if (!isYourPicksTabActive && hasPendingYourPicksReload) {
                hasPendingYourPicksReload = false
                // Swap for momentsTabsController.reloadTab(YOUR_PICKS_CONTAINER_ID) once public.
                momentsTabsController.reloadNonActiveTabs()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initWidgets()
        setRefreshListener()
    }

    /**
     * Re-initializing the widget while the moments player is open would replace the
     * content under the user mid-playback, so follow changes made inside the player
     * (which runs in its own activity, stopping this fragment) are applied here,
     * once the user comes back.
     */
    override fun onResume() {
        super.onResume()
        if (hasPendingWidgetReinit) {
            hasPendingWidgetReinit = false
            initMomentsRowWidget()
        }
    }

    private fun initWidgets() {
        initStoriesRowWidget()
        initMomentsFollowTabsWidget()
        initStoriesGridWidget()
    }

    private fun initStoriesRowWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )
        binding.storiesRowWidgetView.initWidget(
            widgetLayout = viewModel.storiesRowBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-stories-row-id",
            widgetDelegate = this
        )
    }

    /**
     * The widget is initialized on the first "Your Picks" data source emission and
     * re-initialized with a fresh configuration on every follow change — the SDK removes
     * a tab that loaded empty, so a new [BlazeMomentsContainerTabItem] is created each
     * time to re-add it once content exists.
     *
     * Changes made inside the player (fragment stopped) are applied twofold: non-active
     * tabs reload right away against the in-place-mutated [yourPicksLiveDataSource]
     * (active playback stays untouched), and the full re-init is deferred to [onResume]
     * to cover the active tab and an empty-removed tab.
     *
     * Deliberately NOT collected with `repeatOnLifecycle`: follow changes are emitted
     * while the player activity covers this fragment (lifecycle at CREATED), which is
     * exactly when the in-session handling must run — and a restarted collection would
     * treat every return as the first emission, re-initializing the widget needlessly.
     * Views are only touched behind the RESUMED check or from lifecycle callbacks.
     */
    private fun initMomentsFollowTabsWidget() {
        viewLifecycleOwner.lifecycleScope.launch {
            mixedWidgetsViewModel.yourPicksDataSource.collectIndexed { index, dataSource ->
                if (index == 0) {
                    yourPicksLiveDataSource = dataSource
                    yourPicksTab = makeYourPicksTab(dataSource)
                    initMomentsRowWidget()
                    return@collectIndexed
                }

                applyYourPicksDataSource(dataSource)
                when {
                    // Widget visible, no player on top -> rebuild it with the fresh tabs right away.
                    lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) -> {
                        initMomentsRowWidget()
                    }
                    // Follow changed while watching another tab -> "Your Picks" refetches in the
                    // background (active playback untouched); full re-init still runs on return.
                    !isYourPicksTabActive -> {
                        // Swap for the targeted reload below once BlazeMomentsWidgetTabsController
                        // exposes it publicly — reloadNonActiveTabs also refetches the other tabs:
                        // momentsTabsController.reloadTab(YOUR_PICKS_CONTAINER_ID)
                        momentsTabsController.reloadNonActiveTabs()
                        hasPendingWidgetReinit = true
                    }
                    // Follow changed on "Your Picks" itself -> it can't be reloaded while watched;
                    // it reloads as soon as the user switches away (see onTabSelected) and the
                    // re-init on return keeps the guarantee if they never switch.
                    else -> {
                        hasPendingYourPicksReload = true
                        hasPendingWidgetReinit = true
                    }
                }
            }
        }
    }

    /**
     * Mutates [yourPicksLiveDataSource] in place — the instance is shared with the SDK,
     * so an open player session picks the change up on the next tab (re)load — and
     * recreates the tab item, so the next widget init re-adds the tab in case the SDK
     * removed it after loading empty.
     */
    private fun applyYourPicksDataSource(dataSource: BlazeDataSourceType.Labels) {
        yourPicksLiveDataSource.blazeWidgetLabel = dataSource.blazeWidgetLabel
        yourPicksLiveDataSource.labelsPriority = dataSource.labelsPriority
        yourPicksTab = makeYourPicksTab(yourPicksLiveDataSource)
    }

    private fun makeYourPicksTab(dataSource: BlazeDataSourceType) = BlazeMomentsContainerTabItem(
        containerId = YOUR_PICKS_CONTAINER_ID,
        title = "Your Picks",
        dataSource = dataSource
    )

    private fun initMomentsRowWidget() {
        // A full re-init rebuilds every tab fresh, so no in-session reload is owed anymore.
        hasPendingYourPicksReload = false
        binding.momentsRowWidgetView.initWidget(
            widgetLayout = viewModel.momentsRowBaseLayout,
            tabsConfiguration = makeMomentsFollowTabsConfiguration(makeFollowMomentsPlayerStyle()),
            widgetId = "mixed-widgets-moments-row-id",
            widgetDelegate = this
        )
    }

    /**
     * Shows the follow button in the moments player. The entity offered to follow
     * is resolved in a fallback order: player -> team -> property.
     * The followed state is highlighted so it clearly stands out from the unfollowed one.
     */
    private fun makeFollowMomentsPlayerStyle(): BlazeMomentsPlayerStyle {
        val wscAccentColor = ContextCompat.getColor(requireContext(), CoreUiR.color.wsc_accent)
        return BlazeMomentsPlayerStyle.base().apply {
            followEntity.isVisible = true
            followEntity.entityType = BlazeFollowEntityType.Player(
                fallbackType = BlazeFollowEntityType.Team(
                    fallbackType = BlazeFollowEntityType.Property(
                        fallbackType = null
                    )
                )
            )
            followEntity.followState.avatar.borderColor = wscAccentColor
            followEntity.followState.chip.backgroundColor = wscAccentColor
            followEntity.followState.chip.iconColor = Color.BLACK
        }
    }

    private fun makeMomentsFollowTabsConfiguration(
        playerStyle: BlazeMomentsPlayerStyle,
    ): BlazeMomentsWidgetTabsConfiguration {
        val tabs = listOf(
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
            yourPicksTab,
        )
        return BlazeMomentsWidgetTabsConfiguration(
            containerSourceId = "mixed-widgets-moments-tabs-container",
            tabs = tabs,
            containerTabsDelegate = momentsContainerTabsDelegate,
            playerStyle = playerStyle,
            controller = momentsTabsController
        )
    }

    private fun initStoriesGridWidget() {
        val dataSource = BlazeDataSourceType.Labels(
            blazeWidgetLabel = BlazeWidgetLabel.singleLabel(STORIES_WIDGET_DEFAULT_LABEL)
        )
        binding.storiesGridWidgetView.initWidget(
            widgetLayout = viewModel.storiesGridBaseLayout,
            dataSource = dataSource,
            widgetId = "mixed-widgets-stories-grid-id",
            widgetDelegate = this
        )
    }

    private fun setRefreshListener() {
        binding.mixedWidgetsPullToRefresh.setOnRefreshListener {
            updateDataSource()
            binding.mixedWidgetsPullToRefresh.isRefreshing = false
        }
    }

    // For more information refer to https://dev.wsc-sports.com/docs/android-widgets#/reloaddata
    private fun updateDataSource() {
        with(binding) {
            storiesRowWidgetView.reloadData(isSilentRefresh = false)
            momentsRowWidgetView.reloadData(isSilentRefresh = false)
            storiesGridWidgetView.reloadData(isSilentRefresh = false)
        }
    }

    companion object {
        private const val YOUR_PICKS_CONTAINER_ID = "your-picks-tab"
    }

}
