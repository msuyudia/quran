package com.suy.quran.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("nomor")
    val number: String? = null,
    @SerializedName("nama")
    val name: String? = null,
    @SerializedName("nama_latin")
    val latinName: String? = null,
    @SerializedName("jumlah_ayat")
    val totalVerses: String? = null,
    @SerializedName("tempat_turun")
    val dropOff: String? = null,
    @SerializedName("arti")
    val meaning: String? = null,
    @SerializedName("deskripsi")
    val description: String? = null,
    @SerializedName("audio")
    val audio: String? = null,
    @SerializedName("ayat")
    val verses: List<Verse>? = null
)

@Entity
data class ChapterEntity(
    @PrimaryKey
    @SerializedName("nomor")
    @ColumnInfo(name = "chapter_number")
    val number: String,
    @SerializedName("nama")
    @ColumnInfo(name = "chapter_name")
    val name: String? = null,
    @SerializedName("nama_latin")
    val latinName: String? = null,
    @SerializedName("jumlah_ayat")
    val totalVerses: String? = null,
    @SerializedName("tempat_turun")
    val dropOff: String? = null,
    @SerializedName("arti")
    val meaning: String? = null,
    @SerializedName("deskripsi")
    val description: String? = null,
    @SerializedName("audio")
    val audio: String? = null
)
