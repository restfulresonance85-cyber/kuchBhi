package com.kuchBhi.com.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_items")
data class FeedItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "summary")
    val summary: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "published_at_iso")
    val publishedAtIso: String,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean = false,
)
