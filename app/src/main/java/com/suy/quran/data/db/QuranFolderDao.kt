package com.suy.quran.data.db

import androidx.room.*
import com.suy.quran.data.models.QuranFolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranFolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(quranFolderEntity: QuranFolderEntity): Long

    @Query("SELECT * FROM QuranFolderEntity")
    fun getFolders(): Flow<List<QuranFolderEntity>>

    @Query("SELECT * FROM QuranFolderEntity WHERE folder_name LIKE '%' || :name || '%'")
    fun searchFolder(name: String): Flow<List<QuranFolderEntity>>

    @Delete
    suspend fun deleteFolder(folder: QuranFolderEntity): Int

    @Query("DELETE FROM QuranFolderEntity")
    suspend fun deleteFolders()

}