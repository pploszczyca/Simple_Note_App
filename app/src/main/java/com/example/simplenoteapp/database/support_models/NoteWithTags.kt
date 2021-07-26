package com.example.simplenoteapp.database.support_models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.simplenoteapp.database.models.Note
import com.example.simplenoteapp.database.models.NoteTag
import com.example.simplenoteapp.database.models.Tag
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
