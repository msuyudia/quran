package com.suy.quran.data.db

import androidx.room.*
import com.suy.quran.data.models.QuranBookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(quranBookmarkEntity: QuranBookmarkEntity): Long

    @Query("SELECT * FROM QuranBookmarkEntity")
    fun getBookmarks(): Flow<List<QuranBookmarkEntity>>

    @Query("SELECT * FROM QuranBookmarkEntity WHERE folder_name LIKE '%' || :query || '%' OR latinName LIKE '%' || :query || '%' OR latin LIKE '%' || :query || '%' ")
    fun searchBookmark(query: String): Flow<List<QuranBookmarkEntity>>

    @Delete
    suspend fun deleteBookmark(quranBookmarkEntity: QuranBookmarkEntity): Int

    @Query("DELETE FROM QuranBookmarkEntity WHERE folder_id = :folderId")
    suspend fun deleteBookmarkByFolder(folderId: String): Int

    @Query("DELETE FROM QuranBookmarkEntity")
    suspend fun deleteBookmarks()

}