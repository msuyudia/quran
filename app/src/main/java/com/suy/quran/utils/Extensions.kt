package com.suy.quran.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.gson.Gson
import java.lang.Exception

fun Any?.toJson(): String = try {
    Gson().toJson(this)
} catch (e: Exception) {
    ""
}

fun <T> String?.fromJson(clazz: Class<T>): T? = try {
    Gson().fromJson(this, clazz)
} catch (e: Exception) {
    null
}

fun tryCatch(function: () -> Unit) {
    try {
        function.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

suspend fun suspendTryCatch(function: suspend () -> Unit) {
    try {
        function.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun EditText.showKeyboard() {
    tryCatch {
        context?.let {
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(this, 0)
        }
    }
}