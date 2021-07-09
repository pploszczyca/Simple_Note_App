package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.room.Room
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var arrayList: List<Note> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "simple-notes-db"
        ).allowMainThreadQueries().build()      // TODO: Change it to asynchronous
        val notesDao = db.notesDao()

        SampleDataProvider.setSampleNotes(notesDao)

        listView = findViewById(R.id.notesList)
        arrayList = notesDao.getAll()
        noteAdapters = NoteAdapters(applicationContext, arrayList!!)
        listView?.adapter = noteAdapters
        listView?.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val noteItem: Note = arrayList?.get(position)!!

        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteItem)
        startActivity(intent)
    }
}