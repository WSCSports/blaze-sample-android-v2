package com.wscsports.blaze_sample_android

import android.util.Log
import com.blaze.blazesdk.delegates.BlazeFollowEntitiesDelegate
import com.blaze.blazesdk.delegates.models.BlazeFollowEntityClickedParams
import com.blaze.blazesdk.shared.BlazeSDK

object FollowEntitiesManager: BlazeFollowEntitiesDelegate {

    fun initialize() {
        // Set this manager as the delegate for SDK callbacks
        BlazeSDK.followEntitiesManager.delegate = this

        // 1. Load existing follow entities from local/remote storage

        // 2. Update the SDK with the followed entities:
//        BlazeSDK.followEntitiesManager.insertFollowedEntities(followEntities)

    }

    override fun onFollowEntityClicked(followEntityParams: BlazeFollowEntityClickedParams) {
        Log.d("FollowEntitiesManager", "onFollowEntityClicked - $followEntityParams")

        if (followEntityParams.newFollowingState) {
            // Update local/remote storage that this entity if followed
        } else {
            // Update local/remote storage that this entity if not followed
        }

    }

}