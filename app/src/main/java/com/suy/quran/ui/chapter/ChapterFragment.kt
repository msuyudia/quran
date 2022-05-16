package com.suy.quran.ui.chapter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.play.core.review.ReviewManager
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.rasalexman.kdispatcher.subscribe
import com.rasalexman.kdispatcher.unsubscribe
import com.suy.quran.R
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.FragmentChapterBinding
import com.suy.quran.ui.BaseFragment
import com.suy.quran.ui.bookmark.AddQuranBookmarkBSDFragment
import com.suy.quran.ui.quran.QuranActivity
import com.suy.quran.ui.verse.VerseActivity
import com.suy.quran.utils.ListenerEvents
import com.suy.quran.utils.Status
import com.suy.quran.utils.gone
import com.suy.quran.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChapterFragment : BaseFragment<FragmentChapterBinding>(), IKDispatcher {

    @Inject
    lateinit var reviewManager: ReviewManager

    override fun inflate() = FragmentChapterBinding.inflate(layoutInflater)

    private val viewModel by viewModels<ChapterViewModel>()

    private val chapterAdapter by lazy { ChapterAdapter() }

    private val searchVerseAdapter by lazy { ChapterVerseAdapter() }

    private fun getQuery() =
        if (activity is QuranActivity) (activity as QuranActivity).query else ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    override fun onResume() {
        super.onResume()
        showChapters()
        call(ListenerEvents.SEARCH_HINT, "Cari Surat/Ayat")
        subscribe<String>(ListenerEvents.SEARCH_VERSE_QUERY) {
            showChapters()
        }
        subscribe<ChapterEntity>(ListenerEvents.CLICK_CHAPTER) { event ->
            val chapter = event.data
            if (chapter == null) showToast("Gagal membuka surat karena surat tidak diketahui") else activity?.let {
                startActivity(VerseActivity.getIntent(it, chapter))
            }
        }
        subscribe<VerseEntity>(ListenerEvents.CLICK_VERSE) { event ->
            val verse = event.data
            if (verse == null) showToast("Gagal membuka ayat karena ayat tidak diketahui") else activity?.let {
                startActivity(VerseActivity.getIntent(it, verse.chapter, verse.number))
            }
        }
        subscribe<VerseEntity>(ListenerEvents.CLICK_VERSE_BOOKMARK) { event ->
            val verse = event.data
            if (verse == null) showToast("Gagal membuka ayat karena ayat tidak diketahui")
            else showAddBookmarkBSD(verse)
        }
        subscribe<String>(ListenerEvents.SUCCESS_ADD_BOOKMARK) { event ->
            val successMessage = event.data
            if (!successMessage.isNullOrBlank()) showToast(successMessage)
            reviewManager.requestReviewFlow().addOnCompleteListener { task ->
                if (task.isSuccessful) activity?.let {
                    reviewManager.launchReviewFlow(it, task.result)
                }
            }
        }
    }

    override fun onPause() {
        unsubscribe(ListenerEvents.SEARCH_VERSE_QUERY)
        unsubscribe(ListenerEvents.CLICK_CHAPTER)
        unsubscribe(ListenerEvents.CLICK_VERSE)
        unsubscribe(ListenerEvents.CLICK_VERSE_BOOKMARK)
        unsubscribe(ListenerEvents.SUCCESS_ADD_BOOKMARK)
        super.onPause()
    }

    private fun setUpView() {
        binding.apply {
            rvChapter.adapter = chapterAdapter
            rvSearchVerse.adapter = searchVerseAdapter
        }
    }

    private fun showAddBookmarkBSD(verse: VerseEntity) {
        AddQuranBookmarkBSDFragment.newInstance(verse).showDialog(childFragmentManager)
    }

    private fun showChapters() {
        if (getQuery().isBlank()) getChapters() else searchVerse()
    }

    private fun getChapters() {
        binding.tvTotalVerses.gone()
        binding.rvChapter.visible()
        binding.rvSearchVerse.gone()
        viewModel.chapters.observe(this) { chapters ->
            chapterAdapter.setList(chapters)
        }
    }

    private fun searchVerse() {
        viewModel.searchVerse(getQuery()).observe(this) {
            it?.apply {
                when (status) {
                    Status.ERROR -> {
                        showToast(message)
                        searchVerseAdapter.clear()
                    }
                    Status.SUCCESS -> {
                        val list = data.orEmpty()
                        binding.rvChapter.gone()
                        binding.rvSearchVerse.visible()
                        searchVerseAdapter.setList(list)
                        binding.tvTotalVerses.visible()
                        binding.tvTotalVerses.text = getString(R.string.total_verses, list.size)
                    }
                    Status.LOADING -> {}
                }
            }
        }
    }
}