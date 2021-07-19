package com.example.simplenoteapp.database

import androidx.room.Entity

@Entity(primaryKeys = ["noteID", "tagID"], tableName = "noteTag")
data class NoteTag(
    val noteID: Int = 0,
    val tagID: Int = 0
)
