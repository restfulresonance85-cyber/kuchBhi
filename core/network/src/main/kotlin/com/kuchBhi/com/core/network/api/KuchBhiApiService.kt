package com.kuchBhi.com.core.network.api

import com.kuchBhi.com.core.network.model.FeedResponseDto
import retrofit2.http.GET

interface KuchBhiApiService {
    @GET("feed")
    suspend fun getFeed(): FeedResponseDto
}
