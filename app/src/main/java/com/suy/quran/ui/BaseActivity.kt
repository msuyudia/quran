package com.suy.quran.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.Exception

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB

    private var dialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateLayout(layoutInflater)
        setContentView(binding.root)
        createDialog()
    }

    private fun createDialog() {
        if (dialog == null) dialog = LoadingDialog(this)
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

    fun showToast(message: Int, duration: Int = Toast.LENGTH_LONG) {
        showToast(getString(message), duration)
    }

    fun showToast(message: String?, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, message ?: "", duration).show()
    }

    fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}