package com.wscsports.blaze_sample_android.core

import android.util.Log
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazeCTAActionType
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLinkActionHandleType
import com.blaze.blazesdk.shared.results.BlazeResult

/**
 * Implementation of the [BlazeWidgetDelegate] interface.
 * All implementations are optional.
 */
class WidgetDelegateImpl : BlazeWidgetDelegate {

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

    override fun onItemClicked(
        sourceId: String?,
        itemId: String,
        itemTitle: String) {
        Log.d(TAG, "sourceId - $sourceId, itemId - $itemId, itemTitle - $itemTitle")
    }

    companion object {
        const val TAG = "WidgetDelegateImpl"
    }
}