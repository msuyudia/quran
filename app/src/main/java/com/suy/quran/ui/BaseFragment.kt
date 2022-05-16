package com.suy.quran.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.suy.quran.utils.tryCatch

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    abstract fun inflate(): VB

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var dialog: Dialog? = null

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
        if (dialog == null) context?.let { dialog = LoadingDialog(it) }
    }

    fun showToast(
        message: String?, default: String = "Error occurred", length: Int = Toast.LENGTH_SHORT
    ) {
        context?.let { Toast.makeText(it, message ?: default, length).show() }
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
            if (dialog?.isShowing == false) dialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        try {
            if (dialog?.isShowing == true) dialog?.dismiss()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}