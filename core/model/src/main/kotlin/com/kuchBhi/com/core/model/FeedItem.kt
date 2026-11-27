package com.kuchBhi.com.core.model

/**
 * Pure domain model representing a feed item displayed in the application.
 */
data class FeedItem(
    val id: String,
    val title: String,
    val summary: String,
    val category: String,
    val publishedAtIso: String,
    val isBookmarked: Boolean = false,
)
