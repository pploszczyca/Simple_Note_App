package com.example.simplenoteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Tag::class, NoteTag::class], version = 5)
abstract class AppDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase {
            synchronized(this) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context!!,
                        AppDatabase::class.java, "simple-notes-db"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
                return INSTANCE!!
            }
        }
    }
}