package com.suy.quran.ui.chapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.data.repository.ChapterRepository
import com.suy.quran.data.repository.VerseRepository
import com.suy.quran.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val verseRepository: VerseRepository
) : ViewModel() {

    val chapters: LiveData<List<ChapterEntity>> =
        chapterRepository.chapters.asLiveData(Dispatchers.IO)

    fun searchVerse(query: String) = liveData<Resource<List<VerseEntity>>>(Dispatchers.IO) {
        try {
            verseRepository.searchVerse(query).catch {
                emit(Resource.error(it.message))
            }.collect {
                emit(Resource.success(it))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error(e.message))
        }
    }
}