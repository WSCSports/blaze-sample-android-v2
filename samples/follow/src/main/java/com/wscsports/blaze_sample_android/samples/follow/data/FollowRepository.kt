package com.wscsports.blaze_sample_android.samples.follow.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Single source of truth for the entities (players, teams, properties) the user follows.
 * Follows are persisted locally, so they survive app restarts.
 *
 * The API intentionally operates on plain entity ids to keep consumers
 * decoupled from the underlying storage.
 */
object FollowRepository {

    private const val DATABASE_NAME = "follow.db"

    private lateinit var dao: FollowedEntityDao

    /** Call once from the Application class before anything else touches follows. */
    fun initialize(context: Context) {
        if (::dao.isInitialized) return
        val database = Room.databaseBuilder(
            context.applicationContext,
            FollowDatabase::class.java,
            DATABASE_NAME
        )
            // Sample app stores only reproducible demo data, so schema
            // changes just recreate the database instead of migrating.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
        dao = database.followedEntityDao()
    }

    /**
     * Emits the currently followed entity ids and every subsequent change,
     * ordered by the most recently followed first.
     */
    val followedEntityIds: Flow<List<String>>
        get() = dao.observeEntityIds().distinctUntilChanged()

    /** Persists a follow state change for the given entity. */
    suspend fun setFollowed(entityId: String, isFollowed: Boolean) {
        if (isFollowed) {
            dao.upsert(FollowedEntity(entityId, followedAt = System.currentTimeMillis()))
        } else {
            dao.deleteByEntityId(entityId)
        }
    }
}
