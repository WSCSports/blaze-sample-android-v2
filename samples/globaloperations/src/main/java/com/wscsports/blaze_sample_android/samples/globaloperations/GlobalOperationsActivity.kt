package com.wscsports.blaze_sample_android.samples.globaloperations

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.blaze.blazesdk.style.players.moments.BlazeMomentsPlayerStyle
import com.blaze.blazesdk.style.players.stories.BlazeStoryPlayerStyle
import com.wscsports.blaze_sample_android.core.ui.R.*
import com.wscsports.blaze_sample_android.core.ui.applySafeAreaPadding
import com.wscsports.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.globaloperations.databinding.ActivityGlobalConfigurationBinding

/**
 * Activity for handling global BlazeSDK operations.
 * Note: BlazeSDK operations are global and will affect all BlazeSDK instances in the app.
 */
class GlobalOperationsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityGlobalConfigurationBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.applySafeAreaPadding()
        setupAppbar()
        setClickListeners()
        setDefaultPlayersStyle()
    }

    private fun setupAppbar() {
        binding.appbar.setupView(getString(string.global_settings_title)) {
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
        val momentPlayerStyle = BlazeMomentsPlayerStyle.Companion.base()
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
                setGeoRestrictionFromInput()
            }
            switchDoNotTrackUser.setOnCheckedChangeListener { _, isChecked ->
                BlazeSDK.setDoNotTrack(isChecked)
            }
        }
    }

    /**
     * Set external user id for BlazeSDK.
     * More information about external user id can be found in the documentation
     * https://dev.wsc-sports.com/docs/android-methods-and-parameters#/external-user
     */
    private fun setExternalUserIdFromInput() {
        binding.externalUserIdEditText.text?.toString()?.let { externalUserId ->
            BlazeSDK.setExternalUserId(externalUserId) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setExternalUserIdFromInput: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e("GlobalConfigurationActivity", "setExternalUserIdFromInput: doOnFailure: message = $message, cause = $cause")
                }
            }
        }
    }

    /**
     * Set geo restriction for BlazeSDK.
     * More information about geo location can be found in the documentation
     * https://dev.wsc-sports.com/docs/android-methods-and-parameters#/geolocation-restrictions
     */
    private fun setGeoRestrictionFromInput() {
        binding.geoLocationEditText.text?.toString()?.let { geoLocation ->
            BlazeSDK.updateGeoRestriction(geoLocation) { result ->
                result.doOnSuccess {
                    Log.d("GlobalConfigurationActivity", "setGeoRestrictionFromInput: doOnSuccess")
                }
                result.doOnFailure { message, cause ->
                    Log.e(
                        "GlobalConfigurationActivity",
                        "setGeoRestrictionFromInput: doOnFailure: message = $message, cause = $cause"
                    )
                }
            }
        }
    }

}