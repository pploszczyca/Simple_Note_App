package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var notesDao: NotesDao ? = null
    private var addNewNoteButton : FloatingActionButton ? = null

    private fun refreshListView(): Unit {
        noteAdapters = NoteAdapters(applicationContext, notesDao!!.getAll())
        listView?.adapter = noteAdapters
    }

    private fun startEditNoteActivity(noteToPass: Note, view: View?): Unit {
        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteToPass)
        val options = ActivityOptionsCompat.makeScaleUpAnimation(view!!, 0, 0, view!!.width, view!!.height).toBundle()
        startActivity(intent, options)
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
            startEditNoteActivity(Note(), it)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        startEditNoteActivity(noteAdapters!!.getItem(position) as Note, view)
    }

    override fun onRestart() {
        super.onRestart()
        refreshListView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        val menuItem = menu!!.findItem(R.id.app_bar_search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Here!"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                noteAdapters!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapters!!.filter.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}