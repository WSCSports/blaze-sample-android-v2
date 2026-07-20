package com.wscsports.blaze_sample_android.samples.follow

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wscsports.blaze_sample_android.core.ui.R as CoreUiR
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.follow.compose.FollowTabsComposeActivity
import com.wscsports.blaze_sample_android.samples.follow.views.FollowTabsViewActivity
import com.wscsports.blaze_sample_android.samples.follow.databinding.ActivityFollowBinding

/**
 * Entry point of the Follow Entities sample: follow button in the moments
 * player, local persistence of the followed entities and a personalized
 * "Your Picks" tab built from them. The same example is implemented twice —
 * pick the Native View or the Jetpack Compose variant from the list.
 * For more information, see:
 * https://dev.wsc-sports.com/docs/android-follow-entities#/.
 */
class FollowActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityFollowBinding::inflate)
    private var implementationsAdapter: FollowImplementationListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        initRecyclerView()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(CoreUiR.string.follow_title)) {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initRecyclerView() {
        implementationsAdapter = FollowImplementationListAdapter { implementationType ->
            navigateToImplementation(implementationType)
        }
        binding.followImplementationsRecyclerView.adapter = implementationsAdapter
        implementationsAdapter?.submitList(FollowImplementationType.entries)
    }

    private fun navigateToImplementation(implementationType: FollowImplementationType) {
        val activityClass = when (implementationType) {
            FollowImplementationType.NATIVE_VIEW -> FollowTabsViewActivity::class.java
            FollowImplementationType.COMPOSE_VIEW -> FollowTabsComposeActivity::class.java
        }
        startActivity(Intent(this, activityClass))
    }
}
