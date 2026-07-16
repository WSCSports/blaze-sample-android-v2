package com.wscsports.blaze_sample_android.samples.follow.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single followed entity (player, team or property).
 * [followedAt] keeps the follow time, so consumers can rank content
 * by the most recently followed entities. Re-following refreshes it.
 */
@Entity(tableName = "followed_entities")
internal data class FollowedEntity(
    @PrimaryKey val entityId: String,
    val followedAt: Long
)
