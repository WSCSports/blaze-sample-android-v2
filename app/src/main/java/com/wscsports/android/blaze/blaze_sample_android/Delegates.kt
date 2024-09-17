package com.wscsports.android.blaze.blaze_sample_android

import android.util.Log
import com.blaze.blazesdk.analytics.models.BlazeAnalyticsEvent
import com.blaze.blazesdk.delegates.BlazePlayerEntryPointDelegate
import com.blaze.blazesdk.delegates.BlazePlayerInContainerDelegate
import com.blaze.blazesdk.delegates.BlazeSDKDelegate
import com.blaze.blazesdk.delegates.BlazeWidgetDelegate
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.features.shared.models.ui_shared.BlazeLinkActionHandleType
import com.blaze.blazesdk.shared.results.BlazeResult

/**
 * [Delegates] shows you how you can override and react to events throughout the SDK.
 * Note - all functions implementations are optional. It's the application developers choice which function to implement.
 * For more information, please refer to:
 *
 * Global Delegates: https://dash.readme.com/project/wsc-blaze/v1.0/docs/android-global-delegate-methods
 * Widget Delegates: https://dev.wsc-sports.com/docs/android-widgets#widget-delegate-methods
 * Entry Point Delegates: https://dev.wsc-sports.com/docs/android-blaze-player-entry-point-delegate
 * Container Delegates: https://dev.wsc-sports.com/docs/android-player-container#blazeplayerincontainerdelegate
 *
 * */
object Delegates {

    // Global Delegates sample implementation
    val globalDelegate = object : BlazeSDKDelegate {

        override fun onErrorThrown(error: BlazeResult.Error) {
            Log.d("BlazeSDKDelegate", "onErrorThrown - $error")
        }

        override fun onEventTriggered(eventData: BlazeAnalyticsEvent) {
            Log.d("BlazeSDKDelegate", "onEventTriggered - $eventData")
        }

    }

    // Entry Points Delegates sample implementation
    val playerEntryPointDelegate = object : BlazePlayerEntryPointDelegate {

        override fun onDataLoadStarted(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerEntryPointDelegate", "onDataLoadStarted - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onDataLoadComplete(
            playerType: BlazePlayerType,
            sourceId: String?,
            itemsCount: Int,
            result: BlazeResult<Unit>
        ) {
            Log.d("BlazePlayerEntryPointDelegate", "onDataLoadComplete - playerType - $playerType, sourceId - $sourceId, itemsCount - $itemsCount, result - $result")
        }

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerEntryPointDelegate", "onPlayerDidAppear - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onPlayerDidDismiss(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerEntryPointDelegate", "onPlayerDidDismiss - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onTriggerCTA(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionType: String,
            actionParam: String
        ): Boolean {
            Log.d("BlazePlayerEntryPointDelegate", "onTriggerCTA - playerType - $playerType, sourceId - $sourceId, actionType - $actionType, actionParam - $actionParam")
            return false
        }

        override fun onTriggerPlayerBodyTextLink(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionParam: String
        ): BlazeLinkActionHandleType {
            Log.d("BlazePlayerEntryPointDelegate", "onTriggerPlayerBodyTextLink - playerType - $playerType, sourceId - $sourceId, actionParam - $actionParam")
            return BlazeLinkActionHandleType.DEEPLINK
        }

    }

    // Widget Delegates sample implementation
    val widgetDelegate = object : BlazeWidgetDelegate {

        override fun onDataLoadStarted(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazeWidgetDelegate", "onDataLoadStarted - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onDataLoadComplete(
            playerType: BlazePlayerType,
            sourceId: String?,
            itemsCount: Int,
            result: BlazeResult<Unit>
        ) {
            Log.d("BlazeWidgetDelegate", "onDataLoadComplete - playerType - $playerType, sourceId - $sourceId, itemsCount - $itemsCount, result - $result")
        }

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazeWidgetDelegate", "onPlayerDidAppear - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onPlayerDidDismiss(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazeWidgetDelegate", "onPlayerDidDismiss - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onTriggerCTA(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionType: String,
            actionParam: String
        ): Boolean {
            Log.d("BlazeWidgetDelegate", "onTriggerCTA - playerType - $playerType, sourceId - $sourceId, actionType - $actionType, actionParam - $actionParam")
            return false
        }

        override fun onTriggerPlayerBodyTextLink(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionParam: String
        ): BlazeLinkActionHandleType {
            Log.d("BlazeWidgetDelegate", "onTriggerPlayerBodyTextLink - playerType - $playerType, sourceId - $sourceId, actionParam - $actionParam")
            return BlazeLinkActionHandleType.DEEPLINK
        }

        override fun onItemClicked(
            sourceId: String?,
            itemId: String,
            itemTitle: String) {
            Log.d("BlazeWidgetDelegate", "sourceId - $sourceId, itemId - $itemId, itemTitle - $itemTitle")
        }

    }

    // Container Delegates sample implementation
    val playerInContainerDelegate = object : BlazePlayerInContainerDelegate {

        override fun onDataLoadStarted(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerInContainerDelegate", "onDataLoadStarted - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onDataLoadComplete(
            playerType: BlazePlayerType,
            sourceId: String?,
            itemsCount: Int,
            result: BlazeResult<Unit>
        ) {
            Log.d("BlazePlayerInContainerDelegate", "onDataLoadComplete - playerType - $playerType, sourceId - $sourceId, itemsCount - $itemsCount, result - $result")
        }

        override fun onPlayerDidAppear(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerInContainerDelegate", "onPlayerDidAppear - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onPlayerDidDismiss(playerType: BlazePlayerType, sourceId: String?) {
            Log.d("BlazePlayerInContainerDelegate", "onPlayerDidDismiss - playerType - $playerType, sourceId - $sourceId")
        }

        override fun onTriggerCTA(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionType: String,
            actionParam: String
        ): Boolean {
            Log.d("BlazePlayerInContainerDelegate", "onTriggerCTA - playerType - $playerType, sourceId - $sourceId, actionType - $actionType, actionParam - $actionParam")
            return false
        }

        override fun onTriggerPlayerBodyTextLink(
            playerType: BlazePlayerType,
            sourceId: String?,
            actionParam: String
        ): BlazeLinkActionHandleType {
            Log.d("BlazePlayerInContainerDelegate", "onTriggerPlayerBodyTextLink - playerType - $playerType, sourceId - $sourceId, actionParam - $actionParam")
            return BlazeLinkActionHandleType.DEEPLINK
        }

    }

}