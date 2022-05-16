package com.suy.quran.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class QuranBookmarkEntity(
    @Embedded
    @SerializedName("folder")
    val folder: QuranFolderEntity? = null,
    @Embedded
    @SerializedName("verse")
    val verse: VerseEntity? = null,
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "bookmark_id")
    val id: String = "${folder?.name ?: ""}${verse?.chapter?.latinName ?: ""}${verse?.number}"
)
