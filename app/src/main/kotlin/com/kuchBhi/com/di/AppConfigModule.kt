package com.kuchBhi.com.di

import com.kuchBhi.com.BuildConfig
import com.kuchBhi.com.core.common.di.ApiBaseUrl
import com.kuchBhi.com.core.common.di.IsDebugBuild
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @ApiBaseUrl
    fun providesApiBaseUrl(): String = BuildConfig.API_BASE_URL

    @Provides
    @IsDebugBuild
    fun providesIsDebugBuild(): Boolean = BuildConfig.DEBUG
}
