package com.example.simplenoteapp.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import java.io.Serializable

data class NoteWithTags(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteID",
        entityColumn = "tagID",
        associateBy = Junction(NoteTag::class)
    )
    val tags: List<Tag>
): Serializable
