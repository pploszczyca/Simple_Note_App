package com.example.simplenoteapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "contents")
    var contents: String = "",

    @ColumnInfo(name = "color")
    var color: Int = -1,

    @PrimaryKey(autoGenerate=true)
    var id: Int = 0
): Serializable {
}