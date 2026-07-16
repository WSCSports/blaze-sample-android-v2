package com.wscsports.blaze_sample_android.samples.follow

import android.util.Log
import com.blaze.blazesdk.delegates.BlazeFollowEntitiesDelegate
import com.blaze.blazesdk.delegates.models.BlazeFollowEntityClickedParams
import com.blaze.blazesdk.follow.models.BlazeFollowEntity
import com.blaze.blazesdk.shared.BlazeSDK
import com.wscsports.blaze_sample_android.samples.follow.data.FollowRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Keeps follows alive across app restarts, with a clear ownership split:
 * the SDK owns the in-session follow state (it updates its player UI
 * optimistically on click), while [FollowRepository] owns the persisted state,
 * seeding the SDK once on start and recording every follow click after that.
 *
 * Clicks are drained by a single consumer, so each write commits before the
 * next one starts and the database always lands in click order — a
 * launch-per-click could commit a rapid follow/unfollow pair in reverse.
 */
object BlazeFollowSynchronizer {

    private const val TAG = "BlazeFollowSynchronizer"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val followClicks = Channel<BlazeFollowEntityClickedParams>(Channel.UNLIMITED)

    /**
     * Call once, after a successful BlazeSDK.init.
     *
     * The delegate is installed synchronously — on the same (main) thread the
     * clicks arrive on — so no click can ever go unrecorded; clicks arriving
     * while the seed runs wait in the channel.
     */
    fun start() {
        installFollowClicksDelegate()
        scope.launch {
            seedSdkWithPersistedFollows()
            persistFollowClicksInClickOrder()
        }
    }

    private fun installFollowClicksDelegate() {
        BlazeSDK.followEntitiesManager.delegate = object : BlazeFollowEntitiesDelegate {
            override fun onFollowEntityClicked(followEntityParams: BlazeFollowEntityClickedParams) {
                followClicks.trySend(followEntityParams)
            }
        }
    }

    /**
     * Seeding merges into the SDK's set instead of replacing it: the set is empty
     * at startup, so the result is the same — except for a follow clicked before
     * the seed finishes, which a replace would wipe from the player UI.
     */
    private suspend fun seedSdkWithPersistedFollows() {
        val persistedEntityIds = FollowRepository.followedEntityIds.first()
        BlazeSDK.followEntitiesManager.insertFollowedEntities(
            persistedEntityIds.map(::BlazeFollowEntity).toSet()
        )
    }

    private suspend fun persistFollowClicksInClickOrder() {
        for (click in followClicks) {
            try {
                FollowRepository.setFollowed(
                    entityId = click.followEntity.entityId,
                    isFollowed = click.newFollowingState
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The consumer must outlive a failed write, or every later
                // click would silently stop being persisted.
                Log.e(TAG, "Persisting follow click failed", e)
            }
        }
    }
}
