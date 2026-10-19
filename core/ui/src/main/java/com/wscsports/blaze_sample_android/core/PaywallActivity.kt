package com.wscsports.blaze_sample_android.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.blaze.blazesdk.entitlements.models.BlazeContentRestrictionReason
import com.blaze.blazesdk.entitlements.models.BlazeFetchedContentPolicy
import com.blaze.blazesdk.shared.BlazeSDK
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A stand-in for your app's paywall, for the reasons a new viewer token can solve.
 * The SDK's fullscreen player is its own activity, so the paywall opens as a translucent activity on top of it and shows a dialog.
 */
class PaywallActivity : Activity() {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showPaywall(reasonFrom(intent))
    }

    // singleTop: another restriction can arrive while the paywall is open, possibly with a different reason.
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        dialog?.dismiss()
        showPaywall(reasonFrom(intent))
    }

    override fun onDestroy() {
        // A dialog still showing when the activity goes away (for example on rotation) leaks its window.
        dialog?.dismiss()
        super.onDestroy()
    }

    private fun showPaywall(reason: BlazeContentRestrictionReason) {
        val tokenInput = EditText(this).apply { hint = "Viewer token" }
        val padding = (24 * resources.displayMetrics.density).toInt()

        dialog = MaterialAlertDialogBuilder(this)
            .setTitle(if (reason == BlazeContentRestrictionReason.SUBSCRIPTION_REQUIRED) "Subscribe to watch" else "Sign in to watch")
            .setMessage("Reason: ${reason.value}")
            .setView(FrameLayout(this).apply {
                setPadding(padding, 0, padding, 0)
                addView(tokenInput)
            })
            .setPositiveButton("Watch") { _, _ ->
                val token = tokenInput.text.toString()
                // The same viewer with a new token, so keep the content already fetched.
                // The open player resolves the item again by itself and starts playing.
                if (token.isNotBlank()) BlazeSDK.setExternalUserToken(token, BlazeFetchedContentPolicy.Keep)
                finish()
            }
            .setNegativeButton("Close") { _, _ -> finish() }
            .setOnCancelListener { finish() }
            .show()
    }

    private fun reasonFrom(intent: Intent): BlazeContentRestrictionReason =
        intent.getStringExtra(EXTRA_REASON)
            ?.let { BlazeContentRestrictionReason.valueOf(it) }
            ?: BlazeContentRestrictionReason.UNKNOWN

    companion object {
        private const val EXTRA_REASON = "extra_reason"

        fun newIntent(context: Context, reason: BlazeContentRestrictionReason): Intent =
            Intent(context, PaywallActivity::class.java).putExtra(EXTRA_REASON, reason.name)
    }
}
