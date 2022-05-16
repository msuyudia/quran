package com.suy.quran.data.repository

import com.suy.quran.data.db.ChapterDao
import com.suy.quran.data.models.Chapter
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.data.remote.ApiService
import com.suy.quran.utils.SafeApiRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
    private val api: ApiService, private val dao: ChapterDao
) : SafeApiRequest() {

    val chapters: Flow<List<ChapterEntity>> = dao.getChapters()

    suspend fun insertChapters(chapters: List<Chapter>): List<Long> =
        dao.insertChapters(chapters.map { chapter ->
            ChapterEntity(
                chapter.number ?: "",
                chapter.name,
                chapter.latinName,
                chapter.totalVerses,
                chapter.dropOff,
                chapter.meaning,
                chapter.description,
                chapter.audio
            )
        })

    suspend fun fetchChapters(): Flow<List<Chapter>> = flow {
        emit(apiRequest { api.getChapters() })
    }

    suspend fun deleteChapters() {
        dao.deleteChapters()
    }
}