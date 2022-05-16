package com.suy.quran.di

import android.content.Context
import androidx.room.Room
import com.suy.quran.data.db.*
import com.suy.quran.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DB_NAME).createFromAsset("databases/quran.db").build()
    }

    @Provides
    @Singleton
    fun provideChapterDao(appDatabase: AppDatabase): ChapterDao {
        return appDatabase.chapterDao()
    }

    @Provides
    @Singleton
    fun provideVerseDao(appDatabase: AppDatabase): VerseDao {
        return appDatabase.verseDao()
    }

    @Provides
    @Singleton
    fun provideQuranBookmarkDao(appDatabase: AppDatabase): QuranBookmarkDao {
        return appDatabase.quranBookmarkDao()
    }

    @Provides
    @Singleton
    fun provideQuranFolderDao(appDatabase: AppDatabase): QuranFolderDao {
        return appDatabase.quranFolderDao()
    }

    @Provides
    @Singleton
    fun provideLastReadVerseDao(appDatabase: AppDatabase): LastReadVerseDao {
        return appDatabase.lastReadVerseDao()
    }

}