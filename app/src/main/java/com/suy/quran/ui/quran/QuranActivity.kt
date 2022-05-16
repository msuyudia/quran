package com.suy.quran.ui.quran

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.rasalexman.kdispatcher.subscribe
import com.rasalexman.kdispatcher.unsubscribe
import com.suy.quran.R
import com.suy.quran.databinding.ActivityQuranBinding
import com.suy.quran.ui.BaseActivity
import com.suy.quran.ui.ViewPagerAdapter
import com.suy.quran.ui.bookmark.QuranBookmarkFragment
import com.suy.quran.ui.chapter.ChapterFragment
import com.suy.quran.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuranActivity : BaseActivity<ActivityQuranBinding>(), IKDispatcher {

    companion object {
        private const val CHAPTER_TAB_POSITION = 0
        private const val BOOKMARK_TAB_POSITION = 1
        fun getIntent(context: Context) = Intent(context, QuranActivity::class.java)
    }

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityQuranBinding.inflate(layoutInflater)

    private val viewModel by viewModels<QuranViewModel>()

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    private val chapterFragment by lazy { ChapterFragment() }

    private val bookmarkFragment by lazy { QuranBookmarkFragment() }

    var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setUpListener()
    }

    override fun onResume() {
        super.onResume()
        subscribe<String>(ListenerEvents.SEARCH_HINT) { event ->
            val hint = event.data ?: ""
            binding.etSearchChapter.hint =
                if (hint.isBlank()) getString(R.string.find_chapter_verse) else hint
        }
    }

    override fun onPause() {
        unsubscribe(ListenerEvents.SEARCH_HINT)
        super.onPause()
    }

    private fun getChapters() {
        viewModel.chapters.observe(this) {
            if (it.isNullOrEmpty()) fetchChapters()
        }
    }

    private fun setUpListener() {
        binding.apply {
            etSearchChapter.afterTextChanged { text ->
                query = text
                call(ListenerEvents.SEARCH_VERSE_QUERY, text)
            }
            btnSearchChapter.setOnClickListener {
                if (etSearchChapter.isVisible) {
                    btnSearchChapter.setImageResource(R.drawable.ic_search)
                    hideKeyboard()
                    etSearchChapter.clearFocus()
                    if (!etSearchChapter.text?.toString()
                            .isNullOrBlank()
                    ) etSearchChapter.setText("")
                    etSearchChapter.gone()
                    tvTitleQuran.visible()
                } else {
                    btnSearchChapter.setImageResource(R.drawable.ic_black_delete)
                    etSearchChapter.visible()
                    etSearchChapter.requestFocus()
                    etSearchChapter.showKeyboard()
                    tvTitleQuran.gone()
                }
            }
        }
    }

    private fun setUpView() {
        addFragments()
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, position ->
            when (position) {
                CHAPTER_TAB_POSITION -> tab.text = "Surat"
                BOOKMARK_TAB_POSITION -> tab.text = "Bookmark"
            }
        }.attach()
    }

    private fun addFragments() {
        viewPagerAdapter.addFragment(chapterFragment, bookmarkFragment)
    }

    private fun fetchChapters() {
        viewModel.fetchChapters().observe(this) {
            it?.apply {
                when (status) {
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        showToast("Berhasil membuat database Al-Quran")
                    }
                }
            }
        }
    }

}