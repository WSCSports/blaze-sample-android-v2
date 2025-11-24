package com.wscsports.blaze_sample_android

import android.app.Application
import android.util.Log
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.shared.BlazeSDK

/** Use the [Application] class to initialize the BlazeSDK.
 * Note - you won't be able to use BlazeSDK before calling BlazeSDK.init
 * */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        BlazeSDK.init(
            // Add your own API key in local.properties file as BLAZE_API_KEY=YOUR_API_KEY
            apiKey = BuildConfig.BLAZE_API_KEY,
            cachingLevel = BlazeCachingLevel.DEFAULT,
            cachingSize = 512,
            sdkDelegate = Delegates.globalDelegate,
            playerEntryPointDelegate = Delegates.playerEntryPointDelegate,
            completionBlock = {
                Log.d("Application", "BlazeSDK.init success completionBlock..")
            },
            errorBlock = { error ->
                Log.e("Application", "BlazeSDK.init errorBlock -> , Init Error = $error")
            },
            externalUserId = null,
            geoLocation = null,
            forceLayoutDirection = null
        )
    }

}