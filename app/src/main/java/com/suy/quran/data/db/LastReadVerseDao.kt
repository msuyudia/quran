package com.suy.quran.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.suy.quran.data.models.LastReadVerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastReadVerseDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertLastReadVerse(lastReadVerseEntity: LastReadVerseEntity): Long

    @Query("SELECT * FROM LastReadVerseEntity WHERE last_read_verse_id = :id")
    fun getLastReadVerse(id: Int = 1): Flow<LastReadVerseEntity>

}