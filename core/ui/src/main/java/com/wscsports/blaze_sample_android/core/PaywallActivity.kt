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
 * A stand-in for your app's paywall.
 * The SDK's fullscreen player is its own activity, so the paywall opens as a translucent activity on top of it and shows a dialog.
 */
class PaywallActivity : Activity() {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val reason = intent.getStringExtra(EXTRA_REASON)
            ?.let { BlazeContentRestrictionReason.valueOf(it) }
            ?: BlazeContentRestrictionReason.UNKNOWN
        showPaywall(reason)
    }

    override fun onDestroy() {
        // A dialog still showing when the activity goes away (for example on rotation) leaks its window.
        dialog?.dismiss()
        super.onDestroy()
    }

    private fun showPaywall(reason: BlazeContentRestrictionReason) {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(titleFor(reason))
            .setMessage("Reason: ${reason.value}")
            .setNegativeButton("Close") { _, _ -> finish() }
            .setOnCancelListener { finish() }

        if (canWatchWithViewerToken(reason)) {
            val tokenInput = EditText(this).apply { hint = "Viewer token" }
            val padding = (24 * resources.displayMetrics.density).toInt()
            builder
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
        }

        dialog = builder.show()
    }

    private fun titleFor(reason: BlazeContentRestrictionReason): String = when (reason) {
        BlazeContentRestrictionReason.SUBSCRIPTION_REQUIRED -> "Subscribe to watch"
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_MISSING,
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_INVALID,
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_EXPIRED -> "Sign in to watch"
        else -> "This content isn't available"
    }

    private fun canWatchWithViewerToken(reason: BlazeContentRestrictionReason): Boolean = when (reason) {
        BlazeContentRestrictionReason.SUBSCRIPTION_REQUIRED,
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_MISSING,
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_INVALID,
        BlazeContentRestrictionReason.EXTERNAL_USER_TOKEN_EXPIRED -> true
        else -> false
    }

    companion object {
        private const val EXTRA_REASON = "extra_reason"

        fun newIntent(context: Context, reason: BlazeContentRestrictionReason): Intent =
            Intent(context, PaywallActivity::class.java).putExtra(EXTRA_REASON, reason.name)
    }
}
