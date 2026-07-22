package com.wscsports.blaze_sample_android.samples.follow.data

import androidx.room.Database
import androidx.room.RoomDatabase

/** Local database of this sample, holding the followed entities. */
@Database(entities = [FollowedEntity::class], version = 1, exportSchema = false)
internal abstract class FollowDatabase : RoomDatabase() {

    abstract fun followedEntityDao(): FollowedEntityDao
}
