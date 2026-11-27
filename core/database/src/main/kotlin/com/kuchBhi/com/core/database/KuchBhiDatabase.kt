package com.kuchBhi.com.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kuchBhi.com.core.database.dao.FeedItemDao
import com.kuchBhi.com.core.database.model.FeedItemEntity

@Database(
    entities = [
        FeedItemEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class KuchBhiDatabase : RoomDatabase() {
    abstract fun feedItemDao(): FeedItemDao
}
