package com.example.simplenoteapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tags")
data class Tag(
    @ColumnInfo(name = "name")
    var name: String = "",

    @PrimaryKey(autoGenerate = true)
    var tagID: Int = 0
):Serializable
