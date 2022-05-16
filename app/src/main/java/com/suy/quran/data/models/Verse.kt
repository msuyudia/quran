package com.suy.quran.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Verse(
    @SerializedName("nomor")
    val number: String? = null,
    @SerializedName("ar")
    val arab: String? = null,
    @SerializedName("tr")
    val latin: String? = null,
    @SerializedName("idn")
    val indonesia: String? = null
)

@Entity
data class VerseEntity(
    @Embedded
    @SerializedName("surat")
    val chapter: ChapterEntity? = null,
    @ColumnInfo(name = "verse_number")
    @SerializedName("nomor")
    val number: String = "",
    @SerializedName("ar")
    val arab: String? = null,
    @SerializedName("tr")
    val latin: String? = null,
    @SerializedName("idn")
    val indonesia: String? = null,
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "verse_id")
    val id: String = "${chapter?.latinName ?: "a"}${number}"
)
