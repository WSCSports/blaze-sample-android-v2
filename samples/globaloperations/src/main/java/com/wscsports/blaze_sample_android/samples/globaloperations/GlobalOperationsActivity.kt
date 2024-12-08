package com.wscsports.blaze_sample_android.samples.globaloperations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.wscsports.android.blaze.blaze_sample_android.core.ui.R.color
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.android.blaze.blaze_sample_android.samples.globaloperations.databinding.ActivityGlobalConfigurationBinding

class GlobalOperationsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityGlobalConfigurationBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        setClickListeners()
        setDefaultPlayersStyle()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Global Operations") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Optional - Set default style for Blaze players.
     * If not implemented, the default style will be BlazeStoryPlayerStyle.base() and BlazeMomentsPlayerStyle.base()
     * More information about Blaze players style customization can be found in the documentation
     * https://dev.wsc-sports.com/docs/android-blaze-story-player-style
     */
    private fun setDefaultPlayersStyle() {
        val storyPlayerStyle = BlazeStoryPlayerStyle.Companion.base()
            .apply {
            backgroundColor = getColor(color.black)
            title.textColor = getColor(color.wsc_accent)
            title.textSize = 16f
        }
        val momentPlayerStyle = com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle.Companion.base()
            .apply {
            backgroundColor = getColor(color.black)
            headingText.textSize = 22f
            bodyText.textSize = 18f
        }

        BlazeSDK.setDefaultStoryPlayerStyle(storyPlayerStyle)
        BlazeSDK.setDefaultMomentsPlayerStyle(momentPlayerStyle)
    }

    private fun setClickListeners() {
        with(binding) {
            externalUserIdButton.setOnClickListener {
                setExternalUserIdFromInput()
            }
            geoLocationButton.setOnClickListener {
                setGeoLocationFromInput()
            }
            switchDoNotTrackUser.setOnCheckedChangeListener { _, isChecked ->
                BlazeSDK.setDoNotTrack(isChecked)
            }
        }
    }

    private fun setExternalUserIdFromInput() {
        binding.externalUserIdEditText.text?.toString()?.let { externalUserId ->
            BlazeSDK.setExternalUserId(externalUserId) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setExternalUserId: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e(
                        "GlobalConfigurationActivity",
                        "setExternalUserId: doOnFailure: message = $message, cause = $cause"
                    )
                }
            }
        }
    }

    private fun setGeoLocationFromInput() {
        binding.geoLocationEditText.text?.toString()?.let { geoLocation ->
            BlazeSDK.updateGeoRestriction(geoLocation) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setGeoLocation: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e(
                        "GlobalConfigurationActivity",
                        "setGeoLocation: doOnFailure: message = $message, cause = $cause"
                    )
                }
            }
        }
    }

}