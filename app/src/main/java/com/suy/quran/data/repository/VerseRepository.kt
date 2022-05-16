package com.suy.quran.data.repository

import android.util.Log
import com.suy.quran.data.db.VerseDao
import com.suy.quran.data.models.Chapter
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.data.remote.ApiService
import com.suy.quran.utils.Constants
import com.suy.quran.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerseRepository @Inject constructor(
    private val api: ApiService, private val dao: VerseDao
) : SafeApiRequest() {

    fun getVerses(chapterNumber: String): Flow<List<VerseEntity>> = dao.getVerses(chapterNumber)

    suspend fun insertVerses(chapter: Chapter): List<Long> =
        dao.insertVerses(chapter.verses.orEmpty().map { verse ->
            VerseEntity(
                ChapterEntity(
                    chapter.number ?: "",
                    chapter.name,
                    chapter.latinName,
                    chapter.totalVerses,
                    chapter.dropOff,
                    chapter.meaning,
                    chapter.description,
                    chapter.audio
                ),
                verse.number ?: "",
                verse.arab,
                verse.latin,
                verse.indonesia
            )
        })

    suspend fun fetchChapter(chapterNumber: String): Flow<Chapter> = flow {
        emit(apiRequest { api.getChapter(chapterNumber) })
    }

    fun searchVerseByChapter(chapterNumber: String, query: String): Flow<List<VerseEntity>> = dao.searchVerseByChapter(chapterNumber, query)

    fun searchVerse(query: String): Flow<List<VerseEntity>> = dao.searchVerse(query)

    suspend fun deleteVerses() {
        dao.deleteVerses()
    }

}