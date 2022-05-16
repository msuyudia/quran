package com.suy.quran.ui.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.repository.QuranFolderRepository
import com.suy.quran.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class QuranFolderViewModel @Inject constructor(
    private val repo: QuranFolderRepository
) : ViewModel() {

    fun insertFolder(name: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val inserted = repo.insertFolder(name)
            emit(if (inserted > 0) Resource.success(true) else Resource.error("Gagal membuat folder $name"))
        } catch (e: Exception) {
            emit(Resource.error(e.message))
        }
    }

}