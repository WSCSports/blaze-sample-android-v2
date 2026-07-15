package com.wscsports.blaze_sample_android

import com.blaze.blazesdk.delegates.BlazeFollowEntitiesDelegate
import com.blaze.blazesdk.delegates.models.BlazeFollowEntityClickedParams
import com.blaze.blazesdk.follow.models.BlazeFollowEntity
import com.blaze.blazesdk.shared.BlazeSDK
import com.wscsports.blaze_sample_android.core.data.FollowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Keeps follows alive across app restarts, with a clear ownership split:
 * the SDK owns the in-session follow state (it updates its player UI
 * optimistically on click), while [FollowRepository] owns the persisted state,
 * seeding the SDK once on start and recording every follow click after that.
 *
 * Clicks are queued through a [Channel] drained by a single consumer, so each
 * write commits before the next one starts and the database always lands in
 * click order — a launch-per-click could commit a rapid follow/unfollow pair
 * in reverse.
 */
class BlazeFollowSynchronizer(
    private val followRepository: FollowRepository,
    private val scope: CoroutineScope
) {

    private val followClicks = Channel<BlazeFollowEntityClickedParams>(Channel.UNLIMITED)

    fun start() {
        enqueueFollowClicks()
        scope.launch {
            seedSdkWithPersistedFollows()
            persistFollowClicksInClickOrder()
        }
    }

    private fun enqueueFollowClicks() {
        BlazeSDK.followEntitiesManager.delegate = object : BlazeFollowEntitiesDelegate {
            override fun onFollowEntityClicked(followEntityParams: BlazeFollowEntityClickedParams) {
                followClicks.trySend(followEntityParams)
            }
        }
    }

    private suspend fun seedSdkWithPersistedFollows() {
        val persistedEntityIds = followRepository.followedEntityIds.first()
        BlazeSDK.followEntitiesManager.setFollowedEntities(
            persistedEntityIds.map(::BlazeFollowEntity).toSet()
        )
    }

    private suspend fun persistFollowClicksInClickOrder() {
        for (click in followClicks) {
            followRepository.setFollowed(
                entityId = click.followEntity.entityId,
                isFollowed = click.newFollowingState
            )
        }
    }
}