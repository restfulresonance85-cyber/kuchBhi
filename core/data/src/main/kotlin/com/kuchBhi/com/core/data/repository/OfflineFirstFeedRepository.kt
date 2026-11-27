package com.kuchBhi.com.core.data.repository

import com.kuchBhi.com.core.common.dispatchers.Dispatcher
import com.kuchBhi.com.core.common.dispatchers.KuchBhiDispatchers
import com.kuchBhi.com.core.data.mapper.asEntity
import com.kuchBhi.com.core.data.mapper.asExternalModel
import com.kuchBhi.com.core.database.dao.FeedItemDao
import com.kuchBhi.com.core.domain.repository.FeedRepository
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.model.runAppCatching
import com.kuchBhi.com.core.network.datasource.NetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Offline-first implementation of [FeedRepository].
 * Room acts as the Single Source of Truth (SSOT); network responses update local storage.
 */
class OfflineFirstFeedRepository @Inject constructor(
    private val feedItemDao: FeedItemDao,
    private val networkDataSource: NetworkDataSource,
    @Dispatcher(KuchBhiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : FeedRepository {

    override fun observeFeedItems(): Flow<List<FeedItem>> =
        feedItemDao.observeAll().map { entities ->
            entities.map { it.asExternalModel() }
        }

    override suspend fun refreshFeed(): AppResult<Unit> = withContext(ioDispatcher) {
        runAppCatching {
            val remoteItems = networkDataSource.fetchFeed()
            val entities = remoteItems.map { dto ->
                val existing = feedItemDao.getById(dto.id)
                dto.asEntity(existingBookmark = existing?.isBookmarked ?: false)
            }
            feedItemDao.upsertAll(entities)
        }
    }

    override suspend fun toggleBookmark(itemId: String): AppResult<Unit> = withContext(ioDispatcher) {
        runAppCatching {
            feedItemDao.toggleBookmark(itemId)
        }
    }
}
