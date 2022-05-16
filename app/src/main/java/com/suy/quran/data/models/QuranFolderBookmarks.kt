package com.suy.quran.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class QuranFolderVerse(
    @SerializedName("folder")
    val folder: QuranFolderEntity? = null,
    @SerializedName("verse")
    val verse: VerseEntity? = null
)

data class QuranFolderBookmarks(
    @SerializedName("folder")
    val folder: QuranFolderEntity? = null,
    @SerializedName("bookmarks")
    val bookmarks: List<QuranBookmarkEntity>? = null
)

@Entity
data class QuranFolderEntity(
    @SerializedName("name")
    @ColumnInfo(name = "folder_name")
    val name: String = "",
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "folder_id")
    val id: String = name.replace(" ", "_").lowercase()
)