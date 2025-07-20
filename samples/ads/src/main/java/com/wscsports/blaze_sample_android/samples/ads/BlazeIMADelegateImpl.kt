package com.wscsports.blaze_sample_android.samples.ads

import android.util.Log
import com.blaze.blazesdk.ads.ima.BlazeIMAHandlerEventType
import com.blaze.blazesdk.ads.ima.models.BlazeImaAdInfo
import com.blaze.ima.BlazeIMAAdRequestInformation
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

    override suspend fun additionalIMATagQueryParams(requestData: BlazeIMAAdRequestInformation): Map<String, String> {
        return emptyMap()
    }

    override suspend fun customIMASettings(requestData: BlazeIMAAdRequestInformation): ImaSdkSettings? {
        return null
    }

    override suspend fun overrideAdTagUrl(requestData: BlazeIMAAdRequestInformation): String? {
        return null
    }

    companion object {
        private const val TAG = "IMAHandlerDelegate"
    }
}