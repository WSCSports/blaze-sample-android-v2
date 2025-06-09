package com.wscsports.blaze_sample_android.samples.ads

import android.util.Log
import com.blaze.blazesdk.ads.banners.BlazeGAMBannerHandlerEventType
import com.blaze.gam.banner.BlazeGAMBannerAdsAdData
import com.blaze.gam.banner.BlazeGAMBannerAdsDelegate

/**
 * Implementation of the [BlazeGAMBannerAdsDelegate] interface.
 * All implementations are optional and have default values.
 */
class BlazeGAMBannerAdsDelegateImpl : BlazeGAMBannerAdsDelegate {

    override fun onGAMBannerAdsAdEvent(eventType: BlazeGAMBannerHandlerEventType, adData: BlazeGAMBannerAdsAdData) {
        Log.d(TAG, "onGAMBannerAdsAdEvent: eventType = $eventType")
    }

    override fun onGAMBannerAdsAdError(errorMsg: String, adData: BlazeGAMBannerAdsAdData) {
        Log.e(TAG, "onGAMBannerAdsAdError: errorMsg = $errorMsg")
    }

    companion object {
        private const val TAG = "GamCustomNativeAdsDelegate"
    }

}