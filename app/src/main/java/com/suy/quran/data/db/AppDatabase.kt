package com.suy.quran.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suy.quran.data.models.*
import com.suy.quran.utils.Constants

@Database(entities = [ChapterEntity::class, VerseEntity::class, QuranBookmarkEntity::class, QuranFolderEntity::class, LastReadVerseEntity::class], version = Constants.DB_VERSION)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun verseDao(): VerseDao
    abstract fun quranBookmarkDao(): QuranBookmarkDao
    abstract fun quranFolderDao(): QuranFolderDao
    abstract fun lastReadVerseDao(): LastReadVerseDao
}