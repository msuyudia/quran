package com.suy.quran.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.suy.quran.R
import androidx.core.content.ContextCompat.getSystemService
import com.suy.quran.utils.tryCatch


abstract class BaseBottomSheetDialogFragment<VB: ViewBinding>: BottomSheetDialogFragment() {

    abstract fun inflate(): VB

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var loadingDialog: Dialog? = null

    override fun getTheme() = R.style.TopRoundedBottomSheet

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLoadingDialog()
    }

    private fun createLoadingDialog() {
        if (loadingDialog == null) context?.let { loadingDialog = LoadingDialog(it) }
    }

    fun showToast(
        message: String?, default: String = "Error occurred", length: Int = Toast.LENGTH_SHORT
    ) {
        context?.applicationContext?.let {
            Toast.makeText(it, message ?: default, length).show()
        }
    }

    fun showToast(
        message: Int, default: String = "Error occurred", length: Int = Toast.LENGTH_SHORT
    ) {
        try {
            showToast(getString(message), default, length)
        } catch (e: Exception) {
            showToast(null, default, length)
        }
    }

    fun showLoading() {
        try {
            if (loadingDialog?.isShowing == false) loadingDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        try {
            if (loadingDialog?.isShowing == true) loadingDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        try {
            if (isAdded && isVisible) dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideKeyboard() {
        tryCatch {
            activity?.let {
                it.currentFocus?.let { view ->
                    val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
    }

    fun showBSD(fm: FragmentManager, tag: String = "BSD") {
        try {
            val oldFragment = fm.findFragmentByTag(tag)

            if (oldFragment != null) return

            if (!isAdded && !isVisible) {
                fm.executePendingTransactions()
                show(fm, tag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}