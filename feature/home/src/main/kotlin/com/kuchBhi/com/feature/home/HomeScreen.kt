package com.kuchBhi.com.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuchBhi.com.core.designsystem.component.KuchBhiLoadingIndicator
import com.kuchBhi.com.core.designsystem.component.KuchBhiTopAppBar
import com.kuchBhi.com.core.designsystem.theme.KuchBhiTheme
import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.ui.component.EmptyStateView
import com.kuchBhi.com.core.ui.component.ErrorStateView
import com.kuchBhi.com.core.ui.mvi.ObserveAsEvents

@Composable
fun HomeRoute(
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            HomeEffect.NavigateToSettings -> onNavigateToSettings()
            is HomeEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    HomeScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            KuchBhiTopAppBar(
                title = "KuchBhi",
                actions = {
                    IconButton(onClick = { onIntent(HomeIntent.Refresh) }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh feed",
                        )
                    }
                    IconButton(onClick = { onIntent(HomeIntent.OpenSettings) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open settings",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.items.isEmpty() -> {
                KuchBhiLoadingIndicator(modifier = Modifier.padding(innerPadding))
            }
            uiState.errorMessage != null && uiState.items.isEmpty() -> {
                ErrorStateView(
                    message = uiState.errorMessage,
                    onRetry = { onIntent(HomeIntent.Refresh) },
                    modifier = Modifier.padding(innerPadding),
                )
            }
            uiState.items.isEmpty() -> {
                EmptyStateView(
                    title = "No items yet",
                    description = "Pull down or tap refresh to fetch the latest updates.",
                    actionLabel = "Refresh",
                    onActionClick = { onIntent(HomeIntent.Refresh) },
                    modifier = Modifier.padding(innerPadding),
                )
            }
            else -> {
                FeedList(
                    items = uiState.items,
                    onToggleBookmark = { id -> onIntent(HomeIntent.ToggleBookmark(id)) },
                    contentPadding = innerPadding,
                )
            }
        }
    }
}

@Composable
private fun FeedList(
    items: List<FeedItem>,
    onToggleBookmark: (String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 12.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp,
            start = 16.dp,
            end = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = items, key = { it.id }) { item ->
            FeedCard(
                item = item,
                onToggleBookmark = { onToggleBookmark(item.id) },
            )
        }
    }
}

@Composable
private fun FeedCard(
    item: FeedItem,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.category.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (item.isBookmarked) {
                            Icons.Filled.Bookmark
                        } else {
                            Icons.Outlined.BookmarkBorder
                        },
                        contentDescription = if (item.isBookmarked) "Remove bookmark" else "Add bookmark",
                    )
                }
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    KuchBhiTheme(dynamicColor = false) {
        HomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                items = listOf(
                    FeedItem(
                        id = "1",
                        title = "Welcome to KuchBhi",
                        summary = "Enterprise multi-module Android architecture built with Compose & MVI.",
                        category = "Architecture",
                        publishedAtIso = "2026-11-28T00:00:00Z",
                        isBookmarked = true,
                    ),
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {},
        )
    }
}
