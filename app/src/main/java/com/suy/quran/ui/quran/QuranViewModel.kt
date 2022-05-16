package com.suy.quran.ui.quran

import androidx.lifecycle.*
import com.suy.quran.data.models.Chapter
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.repository.ChapterRepository
import com.suy.quran.data.repository.QuranBookmarkRepository
import com.suy.quran.data.repository.QuranFolderRepository
import com.suy.quran.data.repository.VerseRepository
import com.suy.quran.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val chapterRepo: ChapterRepository,
    private val verseRepo: VerseRepository
) : ViewModel() {

    val chapters = chapterRepo.chapters.asLiveData(Dispatchers.IO)

    fun fetchChapters() = liveData<Resource<List<Chapter>>>(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            chapterRepo.fetchChapters().catch {
                emit(Resource.error(it.message))
            }.collect { chapters ->
                chapterRepo.insertChapters(chapters)
                var isError = false
                chapters.forEach { chapter ->
                    if (isError) return@forEach
                    chapter.number?.let { chapterNumber ->
                        verseRepo.fetchChapter(chapterNumber).catch {
                            deleteDatabase()
                            emit(Resource.error(it.message))
                            isError = true
                            return@catch
                        }.collect { chapter ->
                            verseRepo.insertVerses(chapter)
                        }
                    }
                }
                if (!isError) emit(Resource.success(chapters))
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message))
        }
    }

    private suspend fun deleteDatabase() {
        chapterRepo.deleteChapters()
        verseRepo.deleteVerses()
    }

}