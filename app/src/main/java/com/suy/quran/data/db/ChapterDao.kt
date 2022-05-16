package com.suy.quran.data.db

import androidx.room.*
import com.suy.quran.data.models.Chapter
import com.suy.quran.data.models.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>): List<Long>

    @Query("SELECT * FROM ChapterEntity")
    fun getChapters(): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM ChapterEntity WHERE latinName = :query || meaning = :query")
    fun searchChapter(query: String): Flow<List<ChapterEntity>>

    @Query("DELETE FROM ChapterEntity")
    suspend fun deleteChapters()

}