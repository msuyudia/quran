package com.suy.quran.ui.folder

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.rasalexman.kdispatcher.IKDispatcher
import com.rasalexman.kdispatcher.call
import com.suy.quran.data.models.VerseEntity
import com.suy.quran.databinding.FragmentBsdAddQuranBookmarkBinding
import com.suy.quran.databinding.FragmentBsdAddQuranFolderBinding
import com.suy.quran.ui.BaseBottomSheetDialogFragment
import com.suy.quran.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddQuranFolderBSDFragment: BaseBottomSheetDialogFragment<FragmentBsdAddQuranFolderBinding>(), IKDispatcher {

    override fun inflate() = FragmentBsdAddQuranFolderBinding.inflate(layoutInflater)

    private val viewModel by viewModels<QuranFolderViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListener()
    }

    private fun setUpListener() {
        binding.etFolderName.afterTextChanged { text ->
            binding.btnAddFolder.isEnabled = text.isNotBlank()
        }
        binding.btnAddFolder.setOnClickListener { insertFolder() }
    }

    private fun insertFolder() {
        val folderName = binding.etFolderName.text?.toString() ?: ""
        viewModel.insertFolder(folderName).observe(this) {
            it?.apply {
                when (status) {
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {
                        hideLoading()
                        showToast(message)
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        hide()
                        call(ListenerEvents.SUCCESS_ADD_FOLDER, folderName)
                    }
                }
            }
        }
    }

    fun showDialog(fm: FragmentManager, tag: String = "AddQuranFolder") {
        showBSD(fm, tag)
    }

}