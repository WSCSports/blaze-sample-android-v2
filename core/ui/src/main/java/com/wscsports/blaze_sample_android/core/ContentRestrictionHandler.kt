package com.wscsports.blaze_sample_android.core

import android.content.Context
import android.content.Intent
import com.blaze.blazesdk.entitlements.models.BlazeContentRestrictionHandleType
import com.blaze.blazesdk.entitlements.models.BlazeContentRestrictionReason

/**
 * The one place the sample decides what happens when the viewer isn't allowed to watch an item.
 * The sample's shared delegates and the entry point delegate forward onContentRestricted here.
 */
object ContentRestrictionHandler {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun onContentRestricted(reason: BlazeContentRestrictionReason): BlazeContentRestrictionHandleType {
        // Started from the application context, so it needs a task flag; it still opens on top of the player.
        appContext.startActivity(
            PaywallActivity.newIntent(appContext, reason).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        // The app shows its own paywall.
        return BlazeContentRestrictionHandleType.HANDLED_BY_APP
    }
}
