package com.wscsports.blaze_sample_android.samples.inlinevideos

import com.blaze.blazesdk.delegates.BlazePipDelegate
import com.blaze.blazesdk.delegates.BlazePipState
import com.blaze.blazesdk.delegates.models.BlazePlayerType
import com.blaze.blazesdk.shared.BlazeSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * App-level singleton that wraps [BlazeSDK.pipManager] and exposes PiP state as a [StateFlow].
 *
 * Call [initialize] once (e.g. in your Activity's onCreate) to register as the SDK delegate.
 * Then collect [pipState] in any Fragment or Composable to react to PiP state changes.
 */
class AppPiPManager private constructor() : BlazePipDelegate {

    private val _pipState = MutableStateFlow(BlazePipState.OFF)
    val pipState: StateFlow<BlazePipState> = _pipState.asStateFlow()

    fun initialize() {
        BlazeSDK.pipManager.delegate = this
    }

    override fun onPipStateChanged(playerType: BlazePlayerType, sourceId: String?, newState: BlazePipState) {
        _pipState.value = newState
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPiPManager? = null

        fun getInstance(): AppPiPManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppPiPManager().also { INSTANCE = it }
            }
    }
}
