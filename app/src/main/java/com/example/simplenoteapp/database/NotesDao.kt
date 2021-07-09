package com.example.simplenoteapp.database

import androidx.room.*

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note: Note)
}