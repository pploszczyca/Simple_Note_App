package com.example.simplenoteapp.database

import androidx.room.*

@Dao
abstract class NotesDao {

    // QUERIES
    @Transaction
    @Query("SELECT * FROM notes")
    abstract fun getAllNotesWithTags(): List<NoteWithTags>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteID = :noteID")
    abstract fun getNoteWithTags(noteID: Int): List<NoteWithTags>

    @Query("SELECT * FROM tags")
    abstract fun getAllTags(): List<Tag>

    @Transaction
    @Query("SELECT * FROM tags WHERE tagID = :tagID")
    abstract fun getTagWithNotes(tagID: Int): List<TagWithNotes>

    // INSERTS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg notes: Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(vararg tags: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(vararg noteTag: NoteTag)

    fun insertNotesWithTags(noteWithTags: NoteWithTags) {
        noteWithTags.tags.map { NoteTag(noteID = noteWithTags.note.noteID, tagID = it.tagID) }.forEach { insert(it) }
        noteWithTags.tags.forEach { insert(it) }
        insert(noteWithTags.note)
    }

    // DELETES
    @Delete
    abstract fun delete(note: Note)

    @Delete
    abstract fun delete(tag: Tag)

    @Delete
    abstract fun delete(noteTag: NoteTag)

    // UPDATES
    @Update
    abstract fun update(note: Note)

    @Update
    abstract fun update(tag: Tag)

}