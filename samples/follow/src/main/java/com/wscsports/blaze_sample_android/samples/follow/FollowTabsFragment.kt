package com.wscsports.blaze_sample_android.samples.follow

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.moments.container.tabs.models.BlazeMomentsContainerTabItem
import com.blaze.blazesdk.features.moments.widgets.tabs.BlazeMomentsWidgetTabsController
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR
import com.wscsports.blaze_sample_android.samples.follow.databinding.FragmentFollowTabsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * The View-system variant of the Moments Follow Tabs example: a tabs-backed
 * moments widget with the personalized "Your Picks" tab built from the locally
 * persisted followed entities. Follow changes swap that tab's data source and
 * refresh it in the background right away; the widget itself is rebuilt with a
 * fresh configuration once the player is dismissed.
 */
class FollowTabsFragment : Fragment(R.layout.fragment_follow_tabs),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(FragmentFollowTabsBinding::bind)
    private val viewModel: FollowViewModel by viewModels()

    private val momentsTabsController = BlazeMomentsWidgetTabsController()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMomentsFollowTabsWidget()
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
            initWidget()
        }
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
            viewModel.yourPicksDataSource.collectIndexed { index, dataSource ->
                if (index == 0) {
                    yourPicksLiveDataSource = dataSource
                    yourPicksTab = makeYourPicksTab(dataSource)
                    initWidget()
                    return@collectIndexed
                }

                applyYourPicksDataSource(dataSource)
                when {
                    // Widget visible, no player on top -> rebuild it with the fresh tabs right away.
                    lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) -> {
                        initWidget()
                    }
                    // Follow changed while watching another tab -> "Your Picks" refetches in the
                    // background (active playback untouched); full re-init still runs on return.
                    !isYourPicksTabActive -> {
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

    private fun initWidget() {
        // A full re-init rebuilds every tab fresh, so no in-session reload is owed anymore.
        hasPendingYourPicksReload = false
        binding.momentsFollowTabsWidgetView.initWidget(
            widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
            tabsConfiguration = makeMomentsFollowTabsConfiguration(
                containerSourceId = CONTAINER_SOURCE_ID,
                yourPicksTab = yourPicksTab,
                containerTabsDelegate = momentsContainerTabsDelegate,
                controller = momentsTabsController,
                accentColor = ContextCompat.getColor(requireContext(), CoreUiR.color.wsc_accent)
            ),
            widgetId = "follow-moments-tabs-view-id",
            widgetDelegate = this
        )
    }

    companion object {
        private const val CONTAINER_SOURCE_ID = "follow-moments-tabs-view"
    }
}
