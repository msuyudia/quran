package com.suy.quran.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout

fun AppCompatEditText?.afterTextChanged(function: (text: String) -> Unit) {
    this?.apply {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(e: Editable?) {
                function(e?.toString() ?: "")
            }
        })
    }
}

fun TabLayout?.onTabSelected(function: (position: Int?) -> Unit) {
    this?.apply {
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                function(tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
}

fun View.visible() {
    if (isGone || isInvisible) visibility = View.VISIBLE
}

fun View.gone() {
    if (isVisible || isInvisible) visibility = View.GONE
}

fun View.invisible() {
    if (isVisible || isGone) visibility = View.INVISIBLE
}