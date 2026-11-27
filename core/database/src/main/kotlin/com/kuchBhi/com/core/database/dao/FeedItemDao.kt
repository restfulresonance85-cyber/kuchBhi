package com.kuchBhi.com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kuchBhi.com.core.database.model.FeedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedItemDao {

    @Query("SELECT * FROM feed_items ORDER BY published_at_iso DESC")
    fun observeAll(): Flow<List<FeedItemEntity>>

    @Query("SELECT * FROM feed_items WHERE id = :id")
    suspend fun getById(id: String): FeedItemEntity?

    @Upsert
    suspend fun upsertAll(items: List<FeedItemEntity>)

    @Query("UPDATE feed_items SET is_bookmarked = NOT is_bookmarked WHERE id = :id")
    suspend fun toggleBookmark(id: String)

    @Query("DELETE FROM feed_items")
    suspend fun clearAll()
}
