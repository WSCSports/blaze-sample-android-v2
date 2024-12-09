package com.wscsports.blaze_sample_android.samples.ads

import android.util.Log
import com.blaze.blazesdk.ads.ima.BlazeIMAHandlerEventType
import com.blaze.blazesdk.ads.ima.models.BlazeImaAdInfo
import com.blaze.ima.BlazeIMADelegate
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings

/**
 * Implementation of the [BlazeIMADelegate] interface.
 * All implementations are optional and have default values.
 */
class BlazeIMADelegateImpl: BlazeIMADelegate {

    override fun onIMAAdEvent(eventType: BlazeIMAHandlerEventType, adInfo: BlazeImaAdInfo?) {
        Log.d(TAG, "onImaAdEvent: $eventType, adInfo: $adInfo")
    }

    override fun onIMAAdError(errMsg: String) {
        Log.e(TAG, "onImaAdError: $errMsg")
    }

    override fun additionalIMATagQueryParams(): Map<String, String> {
        return emptyMap()
    }

    override fun customIMASettings(): ImaSdkSettings? {
        return null
    }

    override fun overrideAdTagUrl(): String? {
        return null
    }

    companion object {
        private const val TAG = "IMAHandlerDelegate"
    }
}