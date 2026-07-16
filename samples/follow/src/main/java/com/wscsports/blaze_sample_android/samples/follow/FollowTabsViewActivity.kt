package com.wscsports.blaze_sample_android.samples.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.follow.databinding.ActivityFollowTabsViewBinding

/** Hosts the Native View variant of the Moments Follow Tabs example, see [FollowTabsFragment]. */
class FollowTabsViewActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityFollowTabsViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        binding.appbar.setupView("Native View") {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
