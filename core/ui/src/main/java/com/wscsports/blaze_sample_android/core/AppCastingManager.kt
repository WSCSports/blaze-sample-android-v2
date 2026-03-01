package com.wscsports.blaze_sample_android.core

import android.util.Log
import com.blaze.blazesdk.delegates.BlazeCastingDelegate
import com.blaze.blazesdk.delegates.BlazeCastingState
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.shared.BlazeSDK

/**
 * A sample singleton that demonstrates how to integrate with the BlazeSDK casting feature.
 *
 * This class implements [BlazeCastingDelegate] and registers itself as the delegate for
 * [BlazeSDK.castingManager], allowing the host app to receive casting state change callbacks
 * and control active casting sessions.
 *
 * Usage:
 * ```kotlin
 * AppCastingManager.getInstance().initialize()
 * ```
 */
class AppCastingManager private constructor() : BlazeCastingDelegate {

    /**
     * Registers this manager as the casting delegate for the BlazeSDK.
     * Call this once during app startup (e.g. in Application.onCreate) before any casting
     * functionality is used.
     */
    fun initialize() {
        BlazeSDK.castingManager.delegate = this
    }

    /**
     * Instructs the BlazeSDK to stop any currently active casting session.
     * This can be called from the host app when the user explicitly ends casting
     * (e.g. via a dedicated "Stop Casting" UI control).
     */
    fun stopActiveCastingSessionFromSdk() {
        BlazeSDK.castingManager.stopActiveCastingSession()
    }

    /**
     * Called by the BlazeSDK whenever the casting state changes for a given player.
     *
     * @param playerType The type of player whose casting state changed (e.g. Stories, Videos).
     * @param sourceId   An optional identifier for the content source that is being cast.
     * @param newState   The new [BlazeCastingState] (e.g. connected, disconnected).
     */
    override fun onCastingStateChanged(playerType: BlazePlayerType, sourceId: String?, newState: BlazeCastingState) {
        Log.d(TAG, "onCastingStateChanged: playerType = $playerType, sourceId = $sourceId, newState = ${newState.name}")
    }

    companion object {
        private const val TAG = "App_Casting"

        @Volatile
        private var INSTANCE: AppCastingManager? = null

        /**
         * Returns the singleton instance of [AppCastingManager], creating it if necessary.
         * Thread-safe via double-checked locking.
         */
        fun getInstance(): AppCastingManager {
            return INSTANCE ?: synchronized(this) {
                val instance = AppCastingManager()
                INSTANCE = instance
                instance
            }
        }
    }

}