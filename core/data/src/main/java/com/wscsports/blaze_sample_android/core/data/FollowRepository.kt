package com.wscsports.blaze_sample_android.core.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Single source of truth for the entities (players, teams, properties) the user follows.
 * Follows are persisted locally, so they survive app restarts.
 *
 * The API intentionally operates on plain entity ids to keep consumers
 * decoupled from the underlying storage.
 */
interface FollowRepository {

    /**
     * Emits the currently followed entity ids and every subsequent change,
     * ordered by the most recently followed first.
     */
    val followedEntityIds: Flow<List<String>>

    /** Persists a follow state change for the given entity. */
    suspend fun setFollowed(entityId: String, isFollowed: Boolean)

    companion object {

        @Volatile
        private var instance: FollowRepository? = null

        fun getInstance(context: Context): FollowRepository =
            instance ?: synchronized(this) {
                instance ?: RoomFollowRepository(
                    dao = SampleDatabase.getInstance(context).followedEntityDao()
                ).also { instance = it }
            }
    }
}

internal class RoomFollowRepository(
    private val dao: FollowedEntityDao
) : FollowRepository {

    override val followedEntityIds: Flow<List<String>> =
        dao.observeEntityIds()
            .distinctUntilChanged()

    override suspend fun setFollowed(entityId: String, isFollowed: Boolean) {
        if (isFollowed) {
            dao.upsert(FollowedEntity(entityId, followedAt = System.currentTimeMillis()))
        } else {
            dao.deleteByEntityId(entityId)
        }
    }
}
