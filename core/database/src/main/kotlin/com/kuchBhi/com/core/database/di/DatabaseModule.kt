package com.kuchBhi.com.core.database.di

import android.content.Context
import androidx.room.Room
import com.kuchBhi.com.core.database.KuchBhiDatabase
import com.kuchBhi.com.core.database.dao.FeedItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "kuchbhi-database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesKuchBhiDatabase(
        @ApplicationContext context: Context,
    ): KuchBhiDatabase = Room.databaseBuilder(
        context,
        KuchBhiDatabase::class.java,
        DATABASE_NAME,
    ).build()

    @Provides
    fun providesFeedItemDao(database: KuchBhiDatabase): FeedItemDao =
        database.feedItemDao()
}
