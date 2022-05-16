package com.suy.quran.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class LastReadVerseEntity(
    @Embedded
    @SerializedName("verse")
    val verse: VerseEntity? = null,
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "last_read_verse_id")
    val id: Int = 1
)
