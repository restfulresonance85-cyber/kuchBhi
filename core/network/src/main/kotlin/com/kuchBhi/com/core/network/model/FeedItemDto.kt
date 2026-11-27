package com.kuchBhi.com.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedItemDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("summary")
    val summary: String,
    @SerialName("category")
    val category: String,
    @SerialName("published_at")
    val publishedAtIso: String,
)

@Serializable
data class FeedResponseDto(
    @SerialName("items")
    val items: List<FeedItemDto>,
)
