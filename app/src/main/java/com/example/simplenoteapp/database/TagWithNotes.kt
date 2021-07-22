package com.example.simplenoteapp.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TagWithNotes(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagID",
        entityColumn = "noteID",
        associateBy = Junction(NoteTag::class)
    )
    val notes: List<Note>
)
