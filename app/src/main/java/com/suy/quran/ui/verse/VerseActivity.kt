package com.suy.quran.ui.verse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.subscribe
import com.rasalexman.kdispatcher.unsubscribe
import com.suy.quran.R
import com.suy.quran.data.models.ChapterEntity
import com.suy.quran.data.models.QuranFolderVerse
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.ActivityVerseBinding
import com.suy.quran.ui.BaseActivity
import com.suy.quran.ui.bookmark.AddQuranBookmarkBSDFragment
import com.suy.quran.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerseActivity : BaseActivity<ActivityVerseBinding>(), IKDispatcher {

    @Inject
    lateinit var reviewManager: ReviewManager

    companion object {
        fun getIntent(context: Context, chapter: ChapterEntity?, verse: String? = "1") =
            Intent(context, VerseActivity::class.java).putExtra(
                Constants.EXTRA_CHAPTER, chapter.toJson()
            ).putExtra(Constants.EXTRA_VERSE_NUMBER, verse?.toIntOrNull() ?: 1)
    }

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityVerseBinding.inflate(layoutInflater)

    private val viewModel by viewModels<VerseViewModel>()

    private val adapter by lazy { VerseAdapter() }

    private val chapter by lazy {
        intent.getStringExtra(Constants.EXTRA_CHAPTER).fromJson(ChapterEntity::class.java)
    }

    private val verseNumber by lazy {
        intent.getIntExtra(Constants.EXTRA_VERSE_NUMBER, 1)
    }

    private var isFirstOnCreate = true
    private var lastReadPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setUpListener()
        getAllVerses()
    }

    override fun onResume() {
        super.onResume()
        subscribe<VerseEntity>(ListenerEvents.CLICK_VERSE_BOOKMARK) { event ->
            val verse = event.data
            if (verse != null) showAddBookmarkBSD(verse) else showToast("Gagal memberi bookmark karena ayat tidak diketahui")
        }
        subscribe<String>(ListenerEvents.SUCCESS_ADD_BOOKMARK) { event ->
            val successMessage = event.data
            if (!successMessage.isNullOrBlank()) showToast(successMessage)
            reviewManager.requestReviewFlow().addOnCompleteListener { task ->
                if (task.isSuccessful) reviewManager.launchReviewFlow(this, task.result)
            }
        }
    }

    override fun onPause() {
        unsubscribe(ListenerEvents.CLICK_VERSE_BOOKMARK)
        unsubscribe(ListenerEvents.SUCCESS_ADD_BOOKMARK)
        if (lastReadPosition > -1) adapter.getLastReadVerse(lastReadPosition)?.let { verse ->
            viewModel.insertLastReadVerse(verse).observe(this) {}
        }
        super.onPause()
    }

    private fun showAddBookmarkBSD(verse: VerseEntity) {
        AddQuranBookmarkBSDFragment.newInstance(verse).showDialog(supportFragmentManager)
    }

    private fun setUpListener() {
        binding.apply {
            btnVerseBack.setOnClickListener { onBackPressed() }
            btnSearchVerse.setOnClickListener {
                if (etSearchVerse.isVisible) {
                    btnSearchVerse.setImageResource(R.drawable.ic_search)
                    hideKeyboard()
                    etSearchVerse.gone()
                    if (!etSearchVerse.text?.toString().isNullOrBlank()) etSearchVerse.setText("")
                    etSearchVerse.clearFocus()
                    tvTitleChapter.visible()
                } else {
                    btnSearchVerse.setImageResource(R.drawable.ic_black_delete)
                    etSearchVerse.visible()
                    etSearchVerse.requestFocus()
                    etSearchVerse.showKeyboard()
                    tvTitleChapter.gone()
                }
            }
            etSearchVerse.afterTextChanged { query ->
                if (query.isBlank()) getAllVerses() else searchVerse()
            }
            rvVerse.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(rv, dx, dy)
                    lastReadPosition = (rv.layoutManager as LinearLayoutManager?)?.findFirstVisibleItemPosition() ?: -1
                }
            })
        }
    }

    private fun setUpView() {
        binding.rvVerse.adapter = adapter
        binding.tvTitleChapter.text = chapter?.latinName ?: ""
    }

    private fun getAllVerses() {
        viewModel.getVerses(chapter?.number ?: "").observe(this) {
            adapter.setList(it)
            if (isFirstOnCreate) {
                isFirstOnCreate = false
                tryCatch {
                    binding.rvVerse.scrollToPosition(verseNumber - 1)
                }
            }
        }
    }

    private fun searchVerse() {
        val query = binding.etSearchVerse.text?.toString() ?: ""
        viewModel.searchVerseByChapter(chapter?.number ?: "", query).observe(this) {
            adapter.setList(it)
        }
    }
}