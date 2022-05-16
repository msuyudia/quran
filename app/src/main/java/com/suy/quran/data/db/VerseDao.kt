package com.suy.quran.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.suy.quran.data.models.QuranBookmarkEntity
import com.suy.quran.data.models.VerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVerses(verses: List<VerseEntity>): List<Long>

    @Query("SELECT * FROM VerseEntity WHERE chapter_number = :number")
    fun getVerses(number: String): Flow<List<VerseEntity>>

    @Query("SELECT * FROM VerseEntity WHERE latinName LIKE '%' || :query || '%' OR indonesia LIKE '%' || :query || '%'")
    fun searchVerse(query: String): Flow<List<VerseEntity>>

    @Query("SELECT * FROM VerseEntity WHERE chapter_number = :chapterNumber AND indonesia LIKE '%' || :query || '%'")
    fun searchVerseByChapter(chapterNumber: String, query: String): Flow<List<VerseEntity>>

    @Query("DELETE FROM VerseEntity WHERE verse_id = :id")
    suspend fun deleteVerse(id: String)

    @Query("DELETE FROM VerseEntity")
    suspend fun deleteVerses()
}