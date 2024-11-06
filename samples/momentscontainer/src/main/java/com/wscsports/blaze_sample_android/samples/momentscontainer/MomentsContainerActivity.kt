package com.wscsports.blaze_sample_android.samples.momentscontainer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.blaze.blazesdk.data_source.BlazeDataSourceType
import com.blaze.blazesdk.data_source.BlazeWidgetLabel
import com.blaze.blazesdk.shared.BlazeSDK
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.momentscontainer.databinding.ActivityMomentsContainerBinding

class MomentsContainerActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMomentsContainerBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        setupBottomNavigation()
        // Prepare moments container for faster loading
        BlazeSDK.prepareMoments(BlazeDataSourceType.Labels(BlazeWidgetLabel.singleLabel(MOMENTS_LABEL)))
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Moments Container") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBottomNavigation() {
        val navController = findNavController(R.id.moments_player_container)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    companion object {
        const val MOMENTS_LABEL = "moments"
    }

}