package com.wscsports.blaze_sample_android

import android.app.Application
import android.util.Log
import com.blaze.blazesdk.prefetch.models.BlazeCachingLevel
import com.blaze.blazesdk.shared.BlazeSDK
import com.wscsports.blaze_sample_android.core.data.FollowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** Use the [Application] class to initialize the BlazeSDK.
 * Note - you won't be able to use BlazeSDK before calling BlazeSDK.init
 * */
class Application : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
                syncFollowedEntities()
            },
            errorBlock = { error ->
                Log.e("Application", "BlazeSDK.init errorBlock -> , Init Error = $error")
            },
            externalUserId = null,
            geoLocation = null,
            forceLayoutDirection = null,
        )
    }

    /** Starts syncing follows once the SDK is ready. Call only after a successful [BlazeSDK.init]. */
    private fun syncFollowedEntities() {
        BlazeFollowSynchronizer(
            followRepository = FollowRepository.getInstance(this),
            scope = applicationScope
        ).start()
    }

}