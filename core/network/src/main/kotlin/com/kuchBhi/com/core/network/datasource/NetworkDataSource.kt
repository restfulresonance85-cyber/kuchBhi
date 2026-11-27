package com.kuchBhi.com.core.network.datasource

import com.kuchBhi.com.core.network.api.KuchBhiApiService
import com.kuchBhi.com.core.network.model.FeedItemDto
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkDataSource {
    suspend fun fetchFeed(): List<FeedItemDto>
}

@Singleton
class RetrofitNetworkDataSource @Inject constructor(
    private val apiService: KuchBhiApiService,
) : NetworkDataSource {

    override suspend fun fetchFeed(): List<FeedItemDto> = apiService.getFeed().items
}
