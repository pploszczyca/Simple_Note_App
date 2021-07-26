package com.example.simplenoteapp.database.support_models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.simplenoteapp.database.models.Note
import com.example.simplenoteapp.database.models.NoteTag
import com.example.simplenoteapp.database.models.Tag

data class TagWithNotes(
    @Embedded val tag: Tag,
    @Relation(
        parentColumn = "tagID",
        entityColumn = "noteID",
        associateBy = Junction(NoteTag::class)
    )
    val notes: List<Note>
)
