package com.example.simplenoteapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Insert
    fun insert(vararg notes: Note)

    @Delete
    fun delete(note: Note)
}