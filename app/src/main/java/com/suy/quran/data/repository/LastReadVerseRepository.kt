package com.suy.quran.data.repository

import com.suy.quran.data.db.LastReadVerseDao
import com.suy.quran.data.db.QuranFolderDao
import com.suy.quran.data.models.LastReadVerseEntity
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.data.remote.ApiService
import javax.inject.Inject

class LastReadVerseRepository @Inject constructor(private val dao: LastReadVerseDao) {

    val getLastReadVerse = dao.getLastReadVerse()

    suspend fun insertLastReadVerse(verse: VerseEntity): Long {
        return dao.insertLastReadVerse(LastReadVerseEntity(verse))
    }

}