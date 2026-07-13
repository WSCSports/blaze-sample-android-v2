package com.wscsports.blaze_sample_android.core.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface FollowedEntityDao {

    @Query("SELECT entityId FROM followed_entities ORDER BY followedAt DESC")
    fun observeEntityIds(): Flow<List<String>>

    @Upsert
    suspend fun upsert(entity: FollowedEntity)

    @Query("DELETE FROM followed_entities WHERE entityId = :entityId")
    suspend fun deleteByEntityId(entityId: String)
}
