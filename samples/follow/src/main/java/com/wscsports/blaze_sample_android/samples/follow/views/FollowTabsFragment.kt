package com.wscsports.blaze_sample_android.samples.follow.views

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.style.widgets.BlazeWidgetLayout
import com.wscsports.blaze_sample_android.core.WidgetDelegateImpl
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR
import com.wscsports.blaze_sample_android.samples.follow.FollowViewModel
import com.wscsports.blaze_sample_android.samples.follow.R
import com.wscsports.blaze_sample_android.samples.follow.YourPicksRefreshCoordinator
import com.wscsports.blaze_sample_android.samples.follow.makeMomentsFollowTabsConfiguration
import com.wscsports.blaze_sample_android.samples.follow.databinding.FragmentFollowTabsBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * The View-system variant of the Moments Follow Tabs example: a tabs-backed moments
 * widget with the personalized "Your Picks" tab built from the locally persisted
 * followed entities. The follow-change choreography lives in [YourPicksRefreshCoordinator];
 * this fragment only wires the widget to it and rebuilds via [initWidget].
 */
class FollowTabsFragment : Fragment(R.layout.fragment_follow_tabs),
    BlazeWidgetDelegate by WidgetDelegateImpl() {

    private val binding by viewBinding(FragmentFollowTabsBinding::bind)
    private val viewModel: FollowViewModel by viewModels()

    private var coordinator: YourPicksRefreshCoordinator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectYourPicks()
    }

    override fun onResume() {
        super.onResume()
        coordinator?.onResumed()
    }

    /**
     * Deliberately NOT collected with `repeatOnLifecycle`: follow changes are emitted
     * while the player activity covers this fragment (lifecycle at CREATED), which is
     * exactly when the in-session handling must run — and a restarted collection would
     * treat every return as the first emission, re-initializing the widget needlessly.
     * Views are only touched behind the coordinator's RESUMED check or from onResume.
     */
    private fun collectYourPicks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.yourPicksDataSource.collectIndexed { index, dataSource ->
                if (index == 0) {
                    coordinator = YourPicksRefreshCoordinator(
                        containerSourceId = CONTAINER_SOURCE_ID,
                        initialDataSource = dataSource,
                        rebuildWidget = ::initWidget,
                    )
                    initWidget()
                } else {
                    coordinator?.onYourPicksChanged(
                        dataSource = dataSource,
                        isResumed = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
                    )
                }
            }
        }
    }

    private fun initWidget() {
        val coordinator = coordinator ?: return
        binding.momentsFollowTabsWidgetView.initWidget(
            widgetLayout = BlazeWidgetLayout.Presets.MomentsWidget.Row.verticalAnimatedThumbnailsRectangles,
            tabsConfiguration = makeMomentsFollowTabsConfiguration(
                containerSourceId = CONTAINER_SOURCE_ID,
                yourPicksTab = coordinator.buildYourPicksTab(),
                containerTabsDelegate = coordinator.containerTabsDelegate,
                controller = coordinator.tabsController,
                accentColor = ContextCompat.getColor(requireContext(), CoreUiR.color.wsc_accent),
            ),
            widgetId = "follow-moments-tabs-view-id",
            widgetDelegate = this,
        )
    }

    companion object {
        private const val CONTAINER_SOURCE_ID = "follow-moments-tabs-view"
    }
}
