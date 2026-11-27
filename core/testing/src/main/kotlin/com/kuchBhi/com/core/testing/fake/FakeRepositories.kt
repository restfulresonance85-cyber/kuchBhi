package com.kuchBhi.com.core.testing.fake

import com.kuchBhi.com.core.domain.repository.FeedRepository
import com.kuchBhi.com.core.domain.repository.UserPreferencesRepository
import com.kuchBhi.com.core.model.AppResult
import com.kuchBhi.com.core.model.FeedItem
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeFeedRepository(
    initialItems: List<FeedItem> = emptyList(),
) : FeedRepository {

    private val itemsFlow = MutableStateFlow(initialItems)
    var refreshResult: AppResult<Unit> = AppResult.Success(Unit)

    override fun observeFeedItems(): Flow<List<FeedItem>> = itemsFlow.asStateFlow()

    override suspend fun refreshFeed(): AppResult<Unit> = refreshResult

    override suspend fun toggleBookmark(itemId: String): AppResult<Unit> {
        itemsFlow.update { items ->
            items.map { item ->
                if (item.id == itemId) item.copy(isBookmarked = !item.isBookmarked) else item
            }
        }
        return AppResult.Success(Unit)
    }

    fun emitItems(items: List<FeedItem>) {
        itemsFlow.value = items
    }
}

class FakeUserPreferencesRepository(
    initialPreferences: UserPreferences = UserPreferences(),
) : UserPreferencesRepository {

    private val preferencesFlow = MutableStateFlow(initialPreferences)

    override val userPreferences: Flow<UserPreferences> = preferencesFlow.asStateFlow()

    override suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        preferencesFlow.update { it.copy(themeConfig = themeConfig) }
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        preferencesFlow.update { it.copy(useDynamicColor = useDynamicColor) }
    }

    override suspend fun setAnalyticsEnabled(enabled: Boolean) {
        preferencesFlow.update { it.copy(analyticsEnabled = enabled) }
    }
}
