package com.kuchBhi.com.core.data.mapper

import com.kuchBhi.com.core.database.model.FeedItemEntity
import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.network.model.FeedItemDto

fun FeedItemEntity.asExternalModel(): FeedItem = FeedItem(
    id = id,
    title = title,
    summary = summary,
    category = category,
    publishedAtIso = publishedAtIso,
    isBookmarked = isBookmarked,
)

fun FeedItemDto.asEntity(existingBookmark: Boolean = false): FeedItemEntity = FeedItemEntity(
    id = id,
    title = title,
    summary = summary,
    category = category,
    publishedAtIso = publishedAtIso,
    isBookmarked = existingBookmark,
)
