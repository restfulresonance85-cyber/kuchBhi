package com.kuchBhi.com.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kuchBhi.com.core.database.dao.FeedItemDao
import com.kuchBhi.com.core.database.model.FeedItemEntity
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.network.datasource.NetworkDataSource
import com.kuchBhi.com.core.network.model.FeedItemDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OfflineFirstFeedRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val feedItemDao: FeedItemDao = mockk(relaxed = true)
    private val networkDataSource: NetworkDataSource = mockk()

    private val repository = OfflineFirstFeedRepository(
        feedItemDao = feedItemDao,
        networkDataSource = networkDataSource,
        ioDispatcher = testDispatcher,
    )

    @Test
    fun `observeFeedItems maps database entities to domain models`() = runTest(testDispatcher) {
        every { feedItemDao.observeAll() } returns flowOf(
            listOf(
                FeedItemEntity(
                    id = "1",
                    title = "Enterprise Android",
                    summary = "Clean architecture overview",
                    category = "Architecture",
                    publishedAtIso = "2026-11-28T00:00:00Z",
                    isBookmarked = true,
                ),
            ),
        )

        val result = repository.observeFeedItems().first()

        assertThat(result).hasSize(1)
        assertThat(result.first().title).isEqualTo("Enterprise Android")
        assertThat(result.first().isBookmarked).isTrue()
    }

    @Test
    fun `refreshFeed preserves existing bookmark status when upserting`() = runTest(testDispatcher) {
        val remoteDto = FeedItemDto(
            id = "42",
            title = "Updated Title",
            summary = "Updated Summary",
            category = "Kotlin",
            publishedAtIso = "2026-11-28T01:00:00Z",
        )
        coEvery { networkDataSource.fetchFeed() } returns listOf(remoteDto)
        coEvery { feedItemDao.getById("42") } returns FeedItemEntity(
            id = "42",
            title = "Old Title",
            summary = "Old Summary",
            category = "Kotlin",
            publishedAtIso = "2026-11-27T01:00:00Z",
            isBookmarked = true,
        )

        val result = repository.refreshFeed()

        assertThat(result).isInstanceOf(AppResult.Success::class.java)
        coVerify {
            feedItemDao.upsertAll(
                match { items -> items.single().isBookmarked },
            )
        }
    }
}
