package com.wscsports.blaze_sample_android.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * App-wide local database. New sample features requiring persistence
 * should add their entities and daos here.
 */
@Database(entities = [FollowedEntity::class], version = 1, exportSchema = false)
internal abstract class SampleDatabase : RoomDatabase() {

    abstract fun followedEntityDao(): FollowedEntityDao

    companion object {

        private const val DATABASE_NAME = "blaze-sample.db"

        @Volatile
        private var instance: SampleDatabase? = null

        fun getInstance(context: Context): SampleDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    SampleDatabase::class.java,
                    DATABASE_NAME
                )
                    // Sample app stores only reproducible demo data, so schema
                    // changes just recreate the database instead of migrating.
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}
