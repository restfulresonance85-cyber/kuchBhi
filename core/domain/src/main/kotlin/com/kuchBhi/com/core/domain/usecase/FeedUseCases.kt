package com.kuchBhi.com.core.domain.usecase

import com.kuchBhi.com.core.domain.repository.FeedRepository
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.model.FeedItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFeedItemsUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    operator fun invoke(): Flow<List<FeedItem>> = feedRepository.observeFeedItems()
}

class RefreshFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = feedRepository.refreshFeed()
}

class ToggleBookmarkUseCase @Inject constructor(
    private val feedRepository: FeedRepository,
) {
    suspend operator fun invoke(itemId: String): AppResult<Unit> =
        feedRepository.toggleBookmark(itemId)
}
