package com.wscsports.blaze_sample_android.samples.ads

import android.os.Bundle
import android.util.Log
import com.blaze.blazesdk.ads.custom_native.BlazeGoogleCustomNativeAdsHandler
import com.blaze.gam.custom_native.BlazeCustomNativeAdData
import com.blaze.gam.custom_native.BlazeGAMCustomNativeAdsDelegate
import com.blaze.gam.custom_native.BlazeGamCustomNativeAdRequestInformation

/**
 * Implementation of the [BlazeGAMCustomNativeAdsDelegate] interface.
 * All implementations are optional and have default values.
 */
class BlazeGAMCustomNativeAdsDelegateImpl : BlazeGAMCustomNativeAdsDelegate {

    override fun customGAMTargetingProperties(requestData: BlazeGamCustomNativeAdRequestInformation): Map<String, String> {
        return emptyMap()
    }

    override fun publisherProvidedId(requestData: BlazeGamCustomNativeAdRequestInformation): String? {
        return null
    }

    override fun networkExtras(requestData: BlazeGamCustomNativeAdRequestInformation): Bundle? {
        return null
    }

    override fun onGAMCustomNativeAdEvent(eventType: BlazeGoogleCustomNativeAdsHandler.EventType, adData: BlazeCustomNativeAdData) {
        Log.d(TAG, "onGAMCustomNativeAdEvent: type - $eventType,  adData - $adData")
    }

    override fun onGAMCustomNativeAdError(errMsg: String) {
        Log.d(TAG, "onGAMCustomNativeAdError: $errMsg")
    }

    companion object {
        private const val TAG = "GamCustomNativeAdsDelegate"
    }
}