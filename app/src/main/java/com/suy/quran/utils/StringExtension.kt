package com.suy.quran.utils

fun String?.clearLatin() =
    this?.replace("<u>", "")?.replace("</u>", "")?.replace("<strong>", "")?.replace("</strong>", "")