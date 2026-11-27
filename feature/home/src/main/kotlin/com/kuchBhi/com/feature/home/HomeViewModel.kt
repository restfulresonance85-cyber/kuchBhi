package com.kuchBhi.com.feature.home

import androidx.lifecycle.viewModelScope
import com.kuchBhi.com.core.domain.usecase.ObserveFeedItemsUseCase
import com.kuchBhi.com.core.domain.usecase.RefreshFeedUseCase
import com.kuchBhi.com.core.domain.usecase.ToggleBookmarkUseCase
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.ui.mvi.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeFeedItems: ObserveFeedItemsUseCase,
    private val refreshFeed: RefreshFeedUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase,
) : BaseMviViewModel<HomeUiState, HomeIntent, HomeEffect>(
    initialState = HomeUiState(),
) {

    init {
        observeFeed()
        syncFeed()
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> syncFeed()
            is HomeIntent.ToggleBookmark -> onToggleBookmark(intent.itemId)
            HomeIntent.OpenSettings -> sendEffect(HomeEffect.NavigateToSettings)
            HomeIntent.DismissError -> updateState { copy(errorMessage = null) }
        }
    }

    private fun observeFeed() {
        viewModelScope.launch {
            observeFeedItems()
                .catch { throwable ->
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Failed to load feed.",
                        )
                    }
                }
                .collect { items ->
                    updateState {
                        copy(
                            isLoading = false,
                            items = items,
                        )
                    }
                }
        }
    }

    private fun syncFeed() {
        viewModelScope.launch {
            updateState { copy(isRefreshing = true, errorMessage = null) }
            when (val result = refreshFeed()) {
                is AppResult.Success -> {
                    updateState { copy(isRefreshing = false, isLoading = false) }
                }
                is AppResult.Error -> {
                    updateState {
                        copy(
                            isRefreshing = false,
                            isLoading = false,
                            errorMessage = if (items.isEmpty()) result.message else null,
                        )
                    }
                    sendEffect(HomeEffect.ShowSnackbar(result.message))
                }
            }
        }
    }

    private fun onToggleBookmark(itemId: String) {
        viewModelScope.launch {
            when (val result = toggleBookmark(itemId)) {
                is AppResult.Success -> Unit
                is AppResult.Error -> sendEffect(HomeEffect.ShowSnackbar(result.message))
            }
        }
    }
}
