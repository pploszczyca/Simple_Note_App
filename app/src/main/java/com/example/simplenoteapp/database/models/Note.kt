package com.example.simplenoteapp.database.models

import android.graphics.Color
import androidx.room.*
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "contents")
    var contents: String = "",

    @ColumnInfo(name = "color")
    var color: Int = Color.TRANSPARENT,

    @PrimaryKey(autoGenerate=true)
    var noteID: Int = 0
): Serializable {
}