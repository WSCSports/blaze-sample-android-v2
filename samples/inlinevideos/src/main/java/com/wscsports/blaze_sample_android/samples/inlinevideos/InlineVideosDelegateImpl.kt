package com.wscsports.blaze_sample_android.samples.inlinevideos

import android.util.Log
import com.blaze.blazesdk.delegates.BlazePlayerInInlineDelegate
import com.blaze.blazesdk.delegates.models.BlazeCTAActionType
import com.blaze.blazesdk.delegates.models.BlazePlayerEvent
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLinkActionHandleType
import com.blaze.blazesdk.shared.results.BlazeResult
import com.blaze.blazesdk.style.shared.models.BlazePlayerCustomActionButtonParams

/**
 * Implementation of the [BlazePlayerInInlineDelegate] interface.
 * All implementations are optional.
 */
class InlineVideosDelegateImpl : BlazePlayerInInlineDelegate {

    override fun onDataLoadStarted(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onDataLoadStarted - playerType - $playerType, sourceId - $sourceId")
    }

    override fun onDataLoadComplete(
        playerType: BlazePlayerType,
        sourceId: String?,
        itemsCount: Int,
        result: BlazeResult<Unit>
    ) {
        Log.d(TAG, "onDataLoadComplete - playerType - $playerType, sourceId - $sourceId, itemsCount - $itemsCount, result - $result")
    }

    override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidAppear - playerType - $playerType, sourceId - $sourceId")
    }

    override fun onPlayerDidDismiss(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidDismiss - playerType - $playerType, sourceId - $sourceId")
    }

    override fun onTriggerCTA(
        playerType: BlazePlayerType,
        sourceId: String?,
        actionType: BlazeCTAActionType,
        actionParam: String
    ): Boolean {
        Log.d(TAG, "onTriggerCTA - playerType - $playerType, sourceId - $sourceId, actionType - $actionType, actionParam - $actionParam")
        return false
    }

    override fun onTriggerPlayerBodyTextLink(
        playerType: BlazePlayerType,
        sourceId: String?,
        actionParam: String
    ): BlazeLinkActionHandleType {
        Log.d(TAG, "onTriggerPlayerBodyTextLink - playerType - $playerType, sourceId - $sourceId, actionParam - $actionParam")
        return BlazeLinkActionHandleType.DEEPLINK
    }

    override fun onPlayerEventTriggered(
        playerType: BlazePlayerType,
        sourceId: String?,
        event: BlazePlayerEvent
    ) {
        Log.d(TAG, "onPlayerEventTriggered - playerType - $playerType, sourceId - $sourceId, event - $event")
    }

    override fun onTriggerCustomActionButton(
        playerType: BlazePlayerType,
        sourceId: String?,
        customParams: BlazePlayerCustomActionButtonParams
    ) {
        Log.d(TAG, "onTriggerCustomActionButton - playerType - $playerType, sourceId - $sourceId, customParams - $customParams")
    }

    override fun onPlayerDidEnterFullScreen(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidEnterFullScreen - playerType - $playerType, sourceId - $sourceId")
    }

    override fun onPlayerDidExitFullScreen(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlayerDidExitFullScreen - playerType - $playerType, sourceId - $sourceId")
    }

    override fun onPlaceholderClicked(playerType: BlazePlayerType, sourceId: String?) {
        Log.d(TAG, "onPlaceholderClicked - playerType - $playerType, sourceId - $sourceId")
    }

    companion object {
        const val TAG = "InlineVideosDelegateImpl"
    }
}
