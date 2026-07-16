package com.wscsports.blaze_sample_android.samples.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.follow.databinding.ActivityFollowTabsComposeBinding

/** Hosts the Jetpack Compose variant of the Moments Follow Tabs example, see [FollowTabsScreen]. */
class FollowTabsComposeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityFollowTabsComposeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        binding.appbar.setupView("Compose View") {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.followTabsComposeView.setContent {
            FollowTabsScreen()
        }
    }
}
