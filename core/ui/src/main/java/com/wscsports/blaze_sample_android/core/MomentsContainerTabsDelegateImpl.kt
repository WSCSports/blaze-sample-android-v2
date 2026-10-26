package com.wscsports.blaze_sample_android.core

import android.util.Log
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.models.BlazeCTAActionType
import com.blaze.blazesdk.delegates.models.BlazeInteractionLinkHandleType
import com.blaze.blazesdk.delegates.models.BlazePlayerEvent
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.delegates.models.BlazeRestrictedContentParams
import com.blaze.blazesdk.entitlements.models.BlazeContentRestrictionHandleType
import com.blaze.blazesdk.entitlements.models.BlazeContentRestrictionReason
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLinkActionHandleType
import com.blaze.blazesdk.shared.results.BlazeResult

class MomentsContainerTabsDelegateImpl(
    private val onTabReselected: ((tabIndex: Int) -> Unit)? = null
) : BlazePlayerContainerTabsDelegate {

    companion object {
        private const val TAG = "MomentsContainerTabsDelegate"
    }

    override fun onDataLoadComplete(
        playerType: BlazePlayerType,
        sourceId: String?,
        itemsCount: Int,
        result: BlazeResult<Unit>
    ) {
        Log.d(TAG, "onDataLoadComplete: playerType=$playerType, sourceId=$sourceId, itemsCount=$itemsCount, result=$result")
        super.onDataLoadComplete(playerType, sourceId, itemsCount, result)
    }

    override fun onDataLoadStarted(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onDataLoadStarted: playerType=$playerType, sourceId=$sourceId")
        super.onDataLoadStarted(playerType, sourceId)
    }

    override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidAppear: playerType=$playerType, sourceId=$sourceId")
        super.onPlayerDidAppear(playerType, sourceId)
    }

    override fun onPlayerDidDismiss(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidDismiss: playerType=$playerType, sourceId=$sourceId")
        super.onPlayerDidDismiss(playerType, sourceId)
    }

    override fun onPlayerEventTriggered(
        playerType: BlazePlayerType,
        sourceId: String?,
        event: BlazePlayerEvent
    ) {
        Log.d(TAG, "onPlayerEventTriggered: playerType=$playerType, sourceId=$sourceId, event=$event")
        super.onPlayerEventTriggered(playerType, sourceId, event)
    }

    override fun onTriggerCTA(
        playerType: BlazePlayerType,
        sourceId: String?,
        actionType: BlazeCTAActionType,
        actionParam: String
    ): Boolean {
        Log.d(TAG, "onTriggerCTA: playerType=$playerType, sourceId=$sourceId, actionType=$actionType, actionParam=$actionParam")
        return super.onTriggerCTA(playerType, sourceId, actionType, actionParam)
    }

    override fun onTriggerPlayerBodyTextLink(
        playerType: BlazePlayerType,
        sourceId: String?,
        actionParam: String
    ): BlazeLinkActionHandleType {
        Log.d(TAG, "onTriggerPlayerBodyTextLink: playerType=$playerType, sourceId=$sourceId, actionParam=$actionParam")
        return super.onTriggerPlayerBodyTextLink(playerType, sourceId, actionParam)
    }

    // Called when an Engagement Layer asks to open a link.
    // Return DEEPLINK (default) to open externally, WEB for the SDK's in-app web view, or HANDLED if the app handled it.
    override fun onTriggerInteractionLink(
        playerType: BlazePlayerType,
        sourceId: String?,
        interactionId: String,
        interactionType: String,
        url: String
    ): BlazeInteractionLinkHandleType {
        Log.d(TAG, "onTriggerInteractionLink: playerType=$playerType, sourceId=$sourceId, interactionId=$interactionId, interactionType=$interactionType, url=$url")
        return BlazeInteractionLinkHandleType.DEEPLINK
    }

    // Called when the viewer isn't allowed to play an item.
    // The sample shows its own paywall, see ContentRestrictionHandler.
    override fun onContentRestricted(
        playerType: BlazePlayerType,
        sourceId: String?,
        reason: BlazeContentRestrictionReason,
        contentParams: BlazeRestrictedContentParams
    ): BlazeContentRestrictionHandleType {
        Log.d(TAG, "onContentRestricted: playerType=$playerType, sourceId=$sourceId, reason=$reason, contentParams=$contentParams")
        return ContentRestrictionHandler.onContentRestricted(reason)
    }

    override fun onTabSelected(
        playerType: BlazePlayerType,
        sourceId: String?,
        tabIndex: Int
    ) {
        Log.d(TAG, "onTabSelected: playerType=$playerType, sourceId=$sourceId, tabIndex=$tabIndex")
        super.onTabSelected(playerType, sourceId, tabIndex)
    }

    // Called only when the user taps the already-selected tab, not on swipes or programmatic selection.
    override fun onTabReselected(
        playerType: BlazePlayerType,
        sourceId: String?,
        tabIndex: Int
    ) {
        Log.d(TAG, "onTabReselected: playerType=$playerType, sourceId=$sourceId, tabIndex=$tabIndex")
        onTabReselected?.invoke(tabIndex)
    }
}

