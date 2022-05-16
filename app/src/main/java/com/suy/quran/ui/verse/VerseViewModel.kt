package com.suy.quran.ui.verse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.data.repository.LastReadVerseRepository
import com.suy.quran.data.repository.VerseRepository
import com.suy.quran.utils.Resource
import com.suy.quran.utils.suspendTryCatch
import com.suy.quran.utils.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VerseViewModel @Inject constructor(
    private val verseRepo: VerseRepository,
    private val lastReadRepo: LastReadVerseRepository
) : ViewModel() {

    fun getVerses(chapterNumber: String) =
        verseRepo.getVerses(chapterNumber).asLiveData(Dispatchers.IO)

    fun searchVerseByChapter(chapterNumber: String, query: String) =
        verseRepo.searchVerseByChapter(chapterNumber, query).asLiveData(Dispatchers.IO)

    fun insertLastReadVerse(verse: VerseEntity) = liveData(Dispatchers.IO) {
        try {
            emit(lastReadRepo.insertLastReadVerse(verse))
        } catch (e: Exception) {
            emit(0)
        }
    }

}