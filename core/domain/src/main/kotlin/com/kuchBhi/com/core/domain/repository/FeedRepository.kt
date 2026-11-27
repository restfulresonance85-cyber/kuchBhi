package com.kuchBhi.com.core.domain.repository

import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.model.FeedItem
import kotlinx.coroutines.flow.Flow

/**
 * Contract for observing and synchronizing feed items.
 * Implemented in :core:data following an offline-first strategy.
 */
interface FeedRepository {
    fun observeFeedItems(): Flow<List<FeedItem>>
    suspend fun refreshFeed(): AppResult<Unit>
    suspend fun toggleBookmark(itemId: String): AppResult<Unit>
}
