package com.kuchBhi.com.core.model

import kotlinx.coroutines.CancellationException

/**
 * Standard enterprise wrapper for synchronous and asynchronous domain outcomes.
 */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Error(
        val message: String,
        val cause: Throwable? = null,
    ) : AppResult<Nothing>
}

inline fun <T> runAppCatching(block: () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (cancellation: CancellationException) {
    throw cancellation
} catch (throwable: Throwable) {
    AppResult.Error(
        message = throwable.message ?: "An unexpected error occurred.",
        cause = throwable,
    )
}
