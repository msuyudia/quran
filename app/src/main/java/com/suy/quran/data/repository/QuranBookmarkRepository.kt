package com.suy.quran.data.repository

import com.suy.quran.data.db.QuranBookmarkDao
import com.suy.quran.data.models.QuranBookmarkEntity
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.models.VerseEntity
import javax.inject.Inject

class QuranBookmarkRepository @Inject constructor(private val dao: QuranBookmarkDao) {

    val getBookmarks = dao.getBookmarks()

    fun searchBookmarks(query: String) = dao.searchBookmark(query)

    suspend fun insertBookmark(folder: QuranFolderEntity, verseEntity: VerseEntity): Long {
        return dao.insertBookmark(QuranBookmarkEntity(folder, verseEntity))
    }

    suspend fun deleteBookmark(quranBookmarkEntity: QuranBookmarkEntity): Int {
        return dao.deleteBookmark(quranBookmarkEntity)
    }

    suspend fun deleteBookmarkByFolder(folderId: String): Int {
        return dao.deleteBookmarkByFolder(folderId)
    }

    suspend fun deleteBookmarks() {
        dao.deleteBookmarks()
    }

}