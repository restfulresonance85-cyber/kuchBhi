package com.kuchBhi.com.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.kuchBhi.com.core.domain.usecase.ObserveFeedItemsUseCase
import com.kuchBhi.com.core.domain.usecase.RefreshFeedUseCase
import com.kuchBhi.com.core.domain.usecase.ToggleBookmarkUseCase
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.testing.MainDispatcherRule
import com.kuchBhi.com.core.testing.fake.FakeFeedRepository
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sampleItem = FeedItem(
        id = "item-1",
        title = "KuchBhi Launch",
        summary = "Multi-module MVI architecture",
        category = "Release",
        publishedAtIso = "2026-11-28T00:00:00Z",
        isBookmarked = false,
    )

    @Test
    fun `initial load populates items from repository`() = runTest {
        val repository = FakeFeedRepository(initialItems = listOf(sampleItem))
        val viewModel = createViewModel(repository)

        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.items).containsExactly(sampleItem)
    }

    @Test
    fun `toggleBookmark intent updates item bookmark status`() = runTest {
        val repository = FakeFeedRepository(initialItems = listOf(sampleItem))
        val viewModel = createViewModel(repository)

        viewModel.onIntent(HomeIntent.ToggleBookmark(sampleItem.id))

        assertThat(viewModel.uiState.value.items.single().isBookmarked).isTrue()
    }

    @Test
    fun `openSettings intent emits NavigateToSettings effect`() = runTest {
        val repository = FakeFeedRepository()
        val viewModel = createViewModel(repository)

        viewModel.effects.test {
            viewModel.onIntent(HomeIntent.OpenSettings)
            assertThat(awaitItem()).isEqualTo(HomeEffect.NavigateToSettings)
        }
    }

    @Test
    fun `refresh failure emits snackbar effect`() = runTest {
        val repository = FakeFeedRepository(initialItems = listOf(sampleItem)).apply {
            refreshResult = AppResult.Error("Network unavailable")
        }
        val viewModel = createViewModel(repository)

        viewModel.effects.test {
            viewModel.onIntent(HomeIntent.Refresh)
            assertThat(awaitItem()).isEqualTo(HomeEffect.ShowSnackbar("Network unavailable"))
        }
    }

    private fun createViewModel(repository: FakeFeedRepository): HomeViewModel = HomeViewModel(
        observeFeedItems = ObserveFeedItemsUseCase(repository),
        refreshFeed = RefreshFeedUseCase(repository),
        toggleBookmark = ToggleBookmarkUseCase(repository),
    )
}
