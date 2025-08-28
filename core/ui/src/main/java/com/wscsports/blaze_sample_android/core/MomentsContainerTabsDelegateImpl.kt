package com.wscsports.blaze_sample_android.core

import android.util.Log
import com.blaze.blazesdk.delegates.BlazePlayerContainerTabsDelegate
import com.blaze.blazesdk.delegates.models.BlazeCTAActionType
import com.blaze.blazesdk.delegates.models.BlazePlayerEvent
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLinkActionHandleType
import com.blaze.blazesdk.shared.results.BlazeResult

class MomentsContainerTabsDelegateImpl : BlazePlayerContainerTabsDelegate {

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

    override fun onTabSelected(
        playerType: BlazePlayerType,
        sourceId: String?,
        tabIndex: Int
    ) {
        Log.d(TAG, "onTabSelected: playerType=$playerType, sourceId=$sourceId, tabIndex=$tabIndex")
        super.onTabSelected(playerType, sourceId, tabIndex)
    }
}

