package com.kuchBhi.com.core.data.di

import com.kuchBhi.com.core.data.repository.DefaultUserPreferencesRepository
import com.kuchBhi.com.core.data.repository.OfflineFirstFeedRepository
import com.kuchBhi.com.core.domain.repository.FeedRepository
import com.kuchBhi.com.core.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsFeedRepository(
        impl: OfflineFirstFeedRepository,
    ): FeedRepository

    @Binds
    fun bindsUserPreferencesRepository(
        impl: DefaultUserPreferencesRepository,
    ): UserPreferencesRepository
}
