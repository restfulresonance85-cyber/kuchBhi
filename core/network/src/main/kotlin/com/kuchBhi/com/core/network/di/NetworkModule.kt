package com.kuchBhi.com.core.network.di

import com.kuchBhi.com.core.common.di.ApiBaseUrl
import com.kuchBhi.com.core.common.di.IsDebugBuild
import com.kuchBhi.com.core.network.api.KuchBhiApiService
import com.kuchBhi.com.core.network.datasource.NetworkDataSource
import com.kuchBhi.com.core.network.datasource.RetrofitNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT_SECONDS = 30L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @IsDebugBuild isDebugBuild: Boolean,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (isDebugBuild) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            },
        )
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        @ApiBaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        networkJson: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun providesKuchBhiApiService(retrofit: Retrofit): KuchBhiApiService =
        retrofit.create(KuchBhiApiService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingsModule {
    @Binds
    fun bindsNetworkDataSource(
        impl: RetrofitNetworkDataSource,
    ): NetworkDataSource
}
