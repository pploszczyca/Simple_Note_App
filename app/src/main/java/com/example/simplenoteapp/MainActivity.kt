package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var arrayList: List<Note> ? = null
    private var notesDao: NotesDao ? = null
    private var addNewNoteButton : FloatingActionButton ? = null

    private fun refreshListView(): Unit {
        arrayList = notesDao!!.getAll()
        noteAdapters = NoteAdapters(applicationContext, arrayList!!)
        listView?.adapter = noteAdapters
    }

    private fun startEditNoteActivity(noteToPass: Note): Unit {
        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteToPass)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        SampleDataProvider.setSampleNotes(notesDao!!)

        listView = findViewById(R.id.notesList)
        addNewNoteButton = findViewById(R.id.addNewNoteButton)

        refreshListView()
        listView?.onItemClickListener = this

        addNewNoteButton!!.setOnClickListener {
            startEditNoteActivity(Note())
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        startEditNoteActivity(arrayList?.get(position)!!)
    }

    override fun onRestart() {
        super.onRestart()
        refreshListView()
    }
}