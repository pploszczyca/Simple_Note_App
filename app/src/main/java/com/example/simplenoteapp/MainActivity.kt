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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.example.simplenoteapp.database.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener, ITagListener {
    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var notesDao: NotesDao ? = null
    private var addNewNoteButton : FloatingActionButton ? = null
    private var navigationView: NavigationView ? = null
    private var clearTagFilterButton: MenuItem ? = null
    private var tagsSubMenu: Menu ? = null

    private fun refreshListView() {
        noteAdapters = NoteAdapters(applicationContext, notesDao!!.getAllNotesWithTags())
        listView?.adapter = noteAdapters
    }

    private fun startEditNoteActivity(noteToPass: Note, view: View?) {
        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteToPass)
        val options = ActivityOptionsCompat.makeScaleUpAnimation(view!!, 0, 0, view.width, view.height).toBundle()
        startActivity(intent, options)
    }

    private fun setUpNavigationView() {
        tagsSubMenu = navigationView!!.menu.findItem(R.id.tagsItem).subMenu

        notesDao!!.getAllTags().forEach { tag -> handleNewTag(tag) }

        navigationView!!.menu.findItem(R.id.addNewTag).setOnMenuItemClickListener {
            AddingNewTagDialog(this).show(supportFragmentManager, "Add new tag fragment")
            true
        }

        navigationView!!.menu.findItem(R.id.drawerEditTagsButton).setOnMenuItemClickListener {
            startActivity(Intent(applicationContext, TagsEditActivity::class.java))
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // For drawer navigation view
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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

    private fun setNewListOfNotesToNoteAdapter (listNoteWithTags: List<NoteWithTags>, isClearTagVisible: Boolean, titleString: String): Boolean {
        noteAdapters!!.arrayList = listNoteWithTags
        noteAdapters!!.notifyDataSetChanged()
        clearTagFilterButton!!.isVisible = isClearTagVisible
        toolbar.title = titleString
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        val searchMenuItem = menu!!.findItem(R.id.app_bar_search)
        clearTagFilterButton = menu.findItem(R.id.clearTagFilter)
        val searchView: SearchView = searchMenuItem.actionView as SearchView
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

        clearTagFilterButton!!.setOnMenuItemClickListener {
            setNewListOfNotesToNoteAdapter(notesDao!!.getAllNotesWithTags(), false, getString(R.string.app_name))
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun handleNewTag(tag: Tag) {
        tagsSubMenu!!.add(tag.name).setOnMenuItemClickListener {
            drawerLayout.close()
            setNewListOfNotesToNoteAdapter(notesDao!!.getNotesThatHaveSpecificTag(tag.tagID), true, tag.name)
        }.icon = ContextCompat.getDrawable(this, R.drawable.ic_label)
    }
}