package com.suy.quran.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.suy.quran.data.models.QuranBookmarkEntity
import com.suy.quran.data.models.QuranFolderBookmarks
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.data.repository.LastReadVerseRepository
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
class QuranBookmarkViewModel @Inject constructor(
    private val verseRepo: VerseRepository,
    private val bookmarkRepo: QuranBookmarkRepository,
    private val folderRepo: QuranFolderRepository,
    private val lastReadVerseRepo: LastReadVerseRepository
) : ViewModel() {

    fun getLastReadVerse() = lastReadVerseRepo.getLastReadVerse.asLiveData(Dispatchers.IO)

    fun getFolders() = folderRepo.getFolders.asLiveData(Dispatchers.IO)

    fun getBookmarks() = liveData<Resource<List<QuranFolderBookmarks>>>(Dispatchers.IO) {
        folderRepo.getFolders.catch {
            emit(Resource.error(it.message))
        }.collect { folders ->
            bookmarkRepo.getBookmarks.catch {
                emit(Resource.error(it.message))
            }.collect { bookmarks ->
                val list = mutableListOf<QuranFolderBookmarks>()
                folders.forEach { folder ->
                    list.add(
                        QuranFolderBookmarks(
                            folder,
                            bookmarks.filter { it.folder?.id == folder.id })
                    )
                }
                emit(Resource.success(list))
            }
        }
    }

    fun searchBookmarks(query: String) =
        liveData<Resource<List<QuranFolderBookmarks>>>(Dispatchers.IO) {
            try {
                folderRepo.searchFolder(query).catch { emit(Resource.error(it.message)) }
                    .collect { folders ->
                        bookmarkRepo.searchBookmarks(query)
                            .catch { emit(Resource.error(it.message)) }
                            .collect { bookmarks ->
                                val list = mutableListOf<QuranFolderBookmarks>()
                                if (folders.isNullOrEmpty()) list.addAll(
                                    bookmarks.groupBy { it.folder }.entries.map { (folder, bookmarks) ->
                                        QuranFolderBookmarks(folder, bookmarks)
                                    }
                                ) else folders.forEach { folder ->
                                    list.add(
                                        QuranFolderBookmarks(
                                            folder,
                                            bookmarks.filter { it.folder?.id == folder.id })
                                    )
                                }
                                emit(Resource.success(list))
                            }
                    }
            } catch (e: Exception) {
                emit(Resource.error(e.message))
            }
        }

    fun deleteFolder(folder: QuranFolderEntity) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val folderDeleted = folderRepo.deleteFolder(folder)
            val bookmarksDeleted = bookmarkRepo.deleteBookmarkByFolder(folder.id)
            if (folderDeleted > 0) emit(
                if (bookmarksDeleted > 0) Resource.success("Berhasil menghapus folder ${folder.name} beserta ayat di dalamnya")
                else Resource.success("Berhasil menghapus folder ${folder.name}, tetapi gagal menghapus ayat di dalam folder ${folder.name}")
            ) else emit(
                if (bookmarksDeleted > 0) Resource.success("Berhasil menghapus ayat di folder ${folder.name}, tetapi gagal menghapus nama folder")
                else Resource.success("Gagal menghapus folder ${folder.name}")
            )
        } catch (e: Exception) {
            emit(Resource.error(e.message))
        }
    }

    fun insertBookmark(folder: QuranFolderEntity, verse: VerseEntity) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val inserted = bookmarkRepo.insertBookmark(folder, verse)
            emit(
                if (inserted > 0) Resource.success("Berhasil  menyimpan QS: ${verse.chapter?.latinName} (${verse.chapter?.number}): Ayat ${verse.number} di folder ${folder.name}")
                else Resource.error("Gagal menyimpan QS: ${verse.chapter?.latinName} (${verse.chapter?.number}): Ayat ${verse.number} di folder ${folder.name}")
            )
        } catch (e: Exception) {
            emit(Resource.error(e.message))
        }
    }

    fun deleteBookmark(bookmark: QuranBookmarkEntity) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val inserted = bookmarkRepo.deleteBookmark(bookmark)
            emit(
                if (inserted > 0) Resource.success("Berhasil menghapus QS: ${bookmark.verse?.chapter?.latinName} (${bookmark.verse?.chapter?.number}): Ayat ${bookmark.verse?.number} dari folder ${bookmark.folder?.name}")
                else Resource.error("Gagal menghapus QS: ${bookmark.verse?.chapter?.latinName} (${bookmark.verse?.chapter?.number}): Ayat ${bookmark.verse?.number} dari folder ${bookmark.folder?.name ?: "ini"}")
            )
        } catch (e: Exception) {
            emit(Resource.error(e.message))
        }
    }

}