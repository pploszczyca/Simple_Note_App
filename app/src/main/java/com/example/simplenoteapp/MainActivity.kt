package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NoteWithTags
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var notesDao: NotesDao ? = null
    private var addNewNoteButton : FloatingActionButton ? = null
    private var navigationView: NavigationView ? = null

    private fun refreshListView(): Unit {
        noteAdapters = NoteAdapters(applicationContext, notesDao!!.getAllNotesWithTags())
        listView?.adapter = noteAdapters
    }

    private fun startEditNoteActivity(noteToPass: Note, view: View?): Unit {
        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteToPass)
        val options = ActivityOptionsCompat.makeScaleUpAnimation(view!!, 0, 0, view!!.width, view!!.height).toBundle()
        startActivity(intent, options)
    }

    private fun setUpNavigationView() {
        val menu = navigationView!!.menu.findItem(R.id.tagsItem).subMenu

        notesDao!!.getAllTags().forEach { tag ->
            menu.add(tag.name).setOnMenuItemClickListener { menuItem ->
                menuItem.isChecked = !menuItem.isChecked
                if(menuItem.isChecked) noteAdapters!!.filter.filter(menuItem.title) else noteAdapters!!.filter.filter("")
                navigationView!!.visibility = NavigationView.INVISIBLE
                true
        } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView = findViewById(R.id.navigationDrawerMenu)
        notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        //Providing sample data
        SampleDataProvider.setSampleNotes(notesDao!!)
        SampleDataProvider.setSampleTags(notesDao!!)

        listView = findViewById(R.id.notesList)
        addNewNoteButton = findViewById(R.id.addNewNoteButton)

        refreshListView()
        listView?.onItemClickListener = this

        addNewNoteButton!!.setOnClickListener {
            startEditNoteActivity(Note(), it)
        }

        setUpNavigationView()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val noteWithTags = noteAdapters!!.getItem(position) as NoteWithTags
        startEditNoteActivity(noteWithTags.note, view)
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // set up back button in the menu
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_draw)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> navigationView!!.visibility = if (navigationView!!.visibility == NavigationView.VISIBLE) NavigationView.INVISIBLE else NavigationView.VISIBLE
        }
        return super.onOptionsItemSelected(item)
    }
}