package com.suy.quran.data.repository

import com.suy.quran.data.db.QuranFolderDao
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.remote.ApiService
import javax.inject.Inject

class QuranFolderRepository @Inject constructor(private val dao: QuranFolderDao) {

    val getFolders = dao.getFolders()

    fun searchFolder(name: String) = dao.searchFolder(name)

    suspend fun insertFolder(name: String): Long {
        return dao.insertFolder(QuranFolderEntity(name))
    }

    suspend fun deleteFolder(folder: QuranFolderEntity): Int {
        return dao.deleteFolder(folder)
    }

    suspend fun deleteFolders() {
        dao.deleteFolders()
    }

}