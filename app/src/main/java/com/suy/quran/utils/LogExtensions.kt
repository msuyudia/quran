package com.suy.quran.utils

import android.util.Log
import java.lang.Exception

fun String.logVerbose() {
    Log.v(Constants.INFO_TAG, this)
}

fun String.logDebug() {
    Log.d(Constants.INFO_TAG, this)
}

fun String.logInfo() {
    Log.i(Constants.INFO_TAG, this)
}

fun String.logWarning() {
    Log.w(Constants.INFO_TAG, this)
}

fun String.logError(exception: Exception) {
    Log.e(Constants.INFO_TAG, this, exception)
}


