package com.suy.quran.ui.bookmark

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.rasalexman.kdispatcher.subscribe
import com.rasalexman.kdispatcher.unsubscribe
import com.suy.quran.R
import com.suy.quran.data.models.QuranBookmarkEntity
import com.suy.quran.data.models.QuranFolderBookmarks
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.FragmentQuranBookmarkBinding
import com.suy.quran.ui.AlertBSDFragment
import com.suy.quran.ui.BaseFragment
import com.suy.quran.ui.folder.AddQuranFolderBSDFragment
import com.suy.quran.ui.quran.QuranActivity
import com.suy.quran.ui.verse.VerseActivity
import com.suy.quran.utils.ListenerEvents
import com.suy.quran.utils.Status
import com.suy.quran.utils.logInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuranBookmarkFragment :
    BaseFragment<FragmentQuranBookmarkBinding>(), IKDispatcher {

    override fun inflate() = FragmentQuranBookmarkBinding.inflate(layoutInflater)

    private val viewModel by viewModels<QuranBookmarkViewModel>()

    private val adapter by lazy { QuranBookmarkAdapter() }

    private var lastReadVerse: VerseEntity? = null

    private fun getQuery() = if (activity is QuranActivity) (activity as QuranActivity).query else ""

    override fun onResume() {
        super.onResume()
        showBookmarks()
        call(ListenerEvents.SEARCH_HINT, "Cari Nama Folder/Surat")
        subscribe<VerseEntity>(ListenerEvents.CLICK_VERSE_BOOKMARK) { event ->
            val verse = event.data
            if (verse == null) showToast("Gagal membuka ayat karena ayat tidak diketahui") else {
                activity?.let {
                    startActivity(
                        VerseActivity.getIntent(
                            it, verse.chapter, verse.number
                        )
                    )
                }
            }
        }
        subscribe<QuranBookmarkEntity>(ListenerEvents.DELETE_VERSE_BOOKMARK) { event ->
            val bookmark = event.data
            if (bookmark == null) showToast("Gagal menghapus ayat dari bookmark karena ayat tidak diketahui") else {
                showBSDDeleteBookmark(bookmark)
            }
        }
        subscribe<String>(ListenerEvents.SUCCESS_ADD_FOLDER) { event ->
            val folderName = event.data
            if (!folderName.isNullOrBlank()) {
                showToast(getString(R.string.success_add_folder, folderName))
                showBookmarks()
            }
        }
        subscribe<QuranFolderBookmarks>(ListenerEvents.DELETE_FOLDER) { event ->
            val folder = event.data
            if (folder == null) showToast("Gagal menghapus folder karena folder tidak diketahui")
            else showBSDDeleteFolder(folder)
        }
        subscribe<String>(ListenerEvents.SEARCH_VERSE_QUERY) {
            showBookmarks()
        }
    }

    override fun onPause() {
        unsubscribe(ListenerEvents.CLICK_VERSE_BOOKMARK)
        unsubscribe(ListenerEvents.SUCCESS_ADD_FOLDER)
        unsubscribe(ListenerEvents.DELETE_VERSE_BOOKMARK)
        unsubscribe(ListenerEvents.DELETE_FOLDER)
        unsubscribe(ListenerEvents.SEARCH_VERSE_QUERY)
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        binding.rvBookmarkedVerse.adapter = adapter
        getLastReadVerse()
    }

    private fun setUpListener() {
        binding.containerLastRead.setOnClickListener {
            activity?.let {
                startActivity(
                    VerseActivity.getIntent(
                        it, lastReadVerse?.chapter, lastReadVerse?.number
                    )
                )
            }
        }
        binding.containerCreateNewFolder.setOnClickListener {
            AddQuranFolderBSDFragment().showDialog(childFragmentManager)
        }
    }

    private fun showBookmarks() {
        if (getQuery().isEmpty()) getBookmarks() else searchBookmark()
    }

    private fun showBSDDeleteFolder(folder: QuranFolderBookmarks) {
        val folderName = folder.folder?.name ?: "ini"
        AlertBSDFragment.newInstance(
            R.drawable.ic_warning,
            getString(R.string.title_delete_folder, folderName),
            getString(R.string.desc_delete_folder, folderName, folderName),
            getString(R.string.no),
            getString(R.string.yes),
            object : AlertBSDFragment.Callback {
                override fun onLeftButtonClicked() {
                    //Just for dismiss
                }

                override fun onRightButtonClicked() {
                    deleteFolder(folder)
                }
            }
        ).showDialog(childFragmentManager)
    }

    private fun showBSDDeleteBookmark(bookmark: QuranBookmarkEntity) {
        AlertBSDFragment.newInstance(
            R.drawable.ic_warning,
            getString(
                R.string.title_delete_bookmark_verse,
                bookmark.verse?.chapter?.latinName,
                bookmark.verse?.chapter?.number,
                bookmark.verse?.number,
                bookmark.folder?.name ?: ""
            ),
            leftButtonText = getString(R.string.no),
            rightButtonText = getString(R.string.yes),
            listener = object : AlertBSDFragment.Callback {
                override fun onLeftButtonClicked() {
                    //Just for dismiss
                }

                override fun onRightButtonClicked() {
                    deleteBookmarkVerse(bookmark)
                }
            }
        ).showDialog(childFragmentManager)
    }

    private fun getLastReadVerse() {
        viewModel.getLastReadVerse().observe(this) {
            lastReadVerse = try {
                it.verse
            } catch (e: Exception) {
                null
            }
            binding.containerLastRead.isVisible = lastReadVerse != null
            binding.tvLastReadVerse.text = getString(
                R.string.last_read_verse,
                lastReadVerse?.chapter?.latinName ?: "",
                lastReadVerse?.chapter?.number ?: "",
                lastReadVerse?.number ?: ""
            )
        }
    }

    private fun getBookmarks() {
        viewModel.getBookmarks().observe(this) {
            it?.apply {
                when (status) {
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        adapter.setList(data.orEmpty())
                    }
                    else -> {}
                }
            }
        }
    }


    private fun searchBookmark() {
        viewModel.searchBookmarks(getQuery()).observe(this) {
            it?.apply {
                when (status) {
                    Status.ERROR -> showToast(message)
                    Status.SUCCESS -> {
                        adapter.setList(data.orEmpty())
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun deleteFolder(folder: QuranFolderBookmarks) {
        "folder name: ${folder.folder?.name ?: ""}".logInfo()
        val folderData = folder.folder
        if (folderData != null) viewModel.deleteFolder(folderData).observe(this) {
            it?.apply {
                when (status) {
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showToast(data.orEmpty())
                        showBookmarks()
                    }
                }
            }
        }
    }

    private fun deleteBookmarkVerse(bookmark: QuranBookmarkEntity) {
        "folder name: ${bookmark.folder?.name ?: ""}".logInfo()
        viewModel.deleteBookmark(bookmark).observe(this) {
            it?.apply {
                when (status) {
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showToast(data.orEmpty())
                        showBookmarks()
                    }
                }
            }
        }
    }

}