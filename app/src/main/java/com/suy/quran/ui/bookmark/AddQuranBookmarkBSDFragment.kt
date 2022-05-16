package com.suy.quran.ui.bookmark

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.rasalexman.kdispatcher.subscribe
import com.rasalexman.kdispatcher.unsubscribe
import com.suy.quran.R
import com.suy.quran.data.models.QuranFolderEntity
import com.suy.quran.data.models.QuranFolderVerse
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.FragmentBsdAddQuranBookmarkBinding
import com.suy.quran.ui.BaseBottomSheetDialogFragment
import com.suy.quran.ui.folder.AddQuranFolderBSDFragment
import com.suy.quran.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddQuranBookmarkBSDFragment: BaseBottomSheetDialogFragment<FragmentBsdAddQuranBookmarkBinding>(), IKDispatcher {

    companion object {
        fun newInstance(verseEntity: VerseEntity): AddQuranBookmarkBSDFragment {
            val fragment =  AddQuranBookmarkBSDFragment()
            val bundle = Bundle()
            bundle.putString(Constants.EXTRA_VERSE, verseEntity.toJson())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun inflate() = FragmentBsdAddQuranBookmarkBinding.inflate(layoutInflater)

    private val viewModel by viewModels<QuranBookmarkViewModel>()

    private val adapter by lazy { AddQuranBookmarkAdapter() }

    private var verse: VerseEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            verse = it.getString(Constants.EXTRA_VERSE).fromJson(VerseEntity::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListener()
        getFolders()
    }

    override fun onResume() {
        super.onResume()
        subscribe<QuranFolderEntity>(ListenerEvents.CLICK_FOLDER) { event ->
            val folder = event.data
            if (folder == null) showToast("Gagal memberi bookmark karena nama folder tidak diketahui") else {
                if (verse != null) insertBookmark(folder, verse!!)
                else showToast("Gagal memberi bookmark karena ayat tidak diketahui")
            }
        }
        subscribe<String>(ListenerEvents.SUCCESS_ADD_FOLDER) { event ->
            val folderName = event.data
            if (!folderName.isNullOrBlank())
                showToast(getString(R.string.success_add_folder, folderName))
        }
    }

    override fun onPause() {
        unsubscribe(ListenerEvents.CLICK_FOLDER)
        unsubscribe(ListenerEvents.SUCCESS_ADD_FOLDER)
        super.onPause()
    }

    private fun setUpView() {
        binding.rvQuranBookmarkFolder.adapter = adapter
    }

    private fun setUpListener() {
        binding.tvAddNewFolder.setOnClickListener {
            AddQuranFolderBSDFragment().showDialog(childFragmentManager)
        }
    }

    private fun getFolders() {
        viewModel.getFolders().observe(this) { folders ->
            adapter.setList(folders.orEmpty())
        }
     }

    private fun insertBookmark(folder: QuranFolderEntity, verseEntity: VerseEntity) {
        viewModel.insertBookmark(folder, verseEntity).observe(this) {
            it?.apply {
                when (status) {
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        call(ListenerEvents.SUCCESS_ADD_BOOKMARK, data.orEmpty())
                        hide()
                    }
                }
            }
        }
    }

    fun showDialog(fm: FragmentManager, tag: String = "AddQuranBookmark") {
        showBSD(fm, tag)
    }

}