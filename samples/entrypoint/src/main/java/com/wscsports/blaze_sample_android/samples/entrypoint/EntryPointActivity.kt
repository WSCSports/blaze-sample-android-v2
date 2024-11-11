package com.wscsports.blaze_sample_android.samples.entrypoint

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blaze.blazesdk.shared.BlazeSDK
import com.blaze.blazesdk.shared.results.doOnFailure
import com.blaze.blazesdk.shared.results.doOnSuccess
import com.wscsports.android.blaze.blaze_sample_android.core.ui.viewBinding
import com.wscsports.blaze_sample_android.samples.entrypoint.databinding.ActivityEntryPointBinding

class EntryPointActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityEntryPointBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupAppbar()
        handleDeepLinkIfNeeded()
        handlePushNotificationIfNeeded()
        setClickListeners()
    }

    private fun setupAppbar() {
        binding.appbar.setupView("Entry Point") {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Process deep link data if the app was opened through a deep link.
     */
    private fun handleDeepLinkIfNeeded() {
        intent?.data?.let { data ->
            if (intent.action == Intent.ACTION_VIEW) {
                handleDeepLink(data.toString())
                // Clearing intent's data -> so universalLink handling won't be triggered again on Activity's recreation
                intent.data = null
            }
        }
    }

    /**
     * Process push notification data if the app was opened through a notification.
     * The data will be received via intent.extras under "WscIasData" key.
     * Refer to [SampleFirebaseMessagingService] for handling push notifications when the app is in the foreground.
     */
    private fun handlePushNotificationIfNeeded() {
        intent.getStringExtra("WscIasData")?.let { data ->
            BlazeSDK.handleNotificationValue(data) {
                it.doOnSuccess {
                    Log.d("appDebug", "EntryPointActivity: handlePushNotificationIfNeeded: handleNotificationValue completed Successfully")
                }.doOnFailure { message, cause ->
                    Log.e("appDebug", "EntryPointActivity: handlePushNotificationIfNeeded: Unable to open Link: $message", cause)
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.simulateDeepLinkButton.setOnClickListener {
            // Mock deep link URI received from intent.data
            val deepLinkUri = "https://prime.mvp.fan/moments/64ee1f9f1396e4277f059613"
            handleDeepLink(deepLinkUri)
        }
    }

    private fun handleDeepLink(deepLinkStr: String) {
        BlazeSDK.handleUniversalLink(deepLinkStr) {
            it.doOnSuccess {
                Log.d("appDebug", "EntryPointActivity: simulateDeepLinkButton: handleUniversalLink completed Successfully")
            }.doOnFailure { message, cause ->
                Log.e("appDebug", "EntryPointActivity: simulateDeepLinkButton: Unable to open Link: $message", cause)
            }
        }
    }

}