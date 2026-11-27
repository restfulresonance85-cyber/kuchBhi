package com.kuchBhi.com.feature.home

import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.ui.mvi.UiEffect
import com.kuchBhi.com.core.ui.mvi.UiIntent
import com.kuchBhi.com.core.ui.mvi.UiState

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val items: List<FeedItem> = emptyList(),
    val errorMessage: String? = null,
) : UiState

sealed interface HomeIntent : UiIntent {
    data object Refresh : HomeIntent
    data class ToggleBookmark(val itemId: String) : HomeIntent
    data object OpenSettings : HomeIntent
    data object DismissError : HomeIntent
}

sealed interface HomeEffect : UiEffect {
    data object NavigateToSettings : HomeEffect
    data class ShowSnackbar(val message: String) : HomeEffect
}
