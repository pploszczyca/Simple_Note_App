package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.simplenoteapp.adapters.NoteAdapters
import com.example.simplenoteapp.database.*
import com.example.simplenoteapp.database.models.Note
import com.example.simplenoteapp.database.models.Tag
import com.example.simplenoteapp.database.support_models.NoteWithTags
import com.example.simplenoteapp.dialogs.AddingNewTagDialog
import com.example.simplenoteapp.enums.GridOption
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ITagListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapters: NoteAdapters
    private lateinit var notesDao: NotesDao
    private lateinit var addNewNoteButton : FloatingActionButton
    private lateinit var navigationView: NavigationView
    private lateinit var clearTagFilterMenuItem: MenuItem
    private lateinit var changeGridNumberMenuItem: MenuItem
    private lateinit var tagsSubMenu: Menu
    private lateinit var recyclerViewLayoutManager: StaggeredGridLayoutManager
    private var actualGridOption = GridOption.SINGLE_COLUMN

    private fun refreshListView() {
        noteAdapters = NoteAdapters(notesDao.getAllNotesWithTags())
        recyclerViewLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = noteAdapters
    }

    private fun startEditNoteActivity(noteToPass: Note, view: View?) {
        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra(EditNote.NOTE_ARGUMENT, noteToPass)
        val options = ActivityOptionsCompat.makeScaleUpAnimation(view!!, 0, 0, view.width, view.height).toBundle()
        startActivity(intent, options)
    }

    private fun setUpNavigationView() {
        tagsSubMenu = navigationView.menu.findItem(R.id.tagsItem).subMenu

        notesDao.getAllTags().forEach { tag -> handleNewTag(tag) }      // initialize existing tags

        navigationView.menu.findItem(R.id.addNewTag).setOnMenuItemClickListener {
            AddingNewTagDialog(this).show(supportFragmentManager, "Add new tag fragment")
            true
        }

        navigationView.menu.findItem(R.id.drawerEditTagsButton).setOnMenuItemClickListener {
            startActivity(Intent(applicationContext, TagsEditActivity::class.java))
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigationView = findViewById(R.id.navigationDrawerMenu)
        recyclerView = findViewById(R.id.notesList)
        addNewNoteButton = findViewById(R.id.addNewNoteButton)
        notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        // For drawer navigation view
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Providing sample data
        SampleDataProvider.setSampleNotes(notesDao)
        SampleDataProvider.setSampleTags(notesDao)

        refreshListView()

        addNewNoteButton.setOnClickListener {
            startEditNoteActivity(Note(), it)
        }

        setUpNavigationView()
    }

    override fun onRestart() {
        super.onRestart()
        refreshListView()
        changeSpanCountAndMenuItemIcon(actualGridOption)
    }

    private fun setNewListOfNotesToNoteAdapter (listNoteWithTags: List<NoteWithTags>, isClearTagVisible: Boolean, titleString: String): Boolean {
        noteAdapters.arrayList = listNoteWithTags
        noteAdapters.notifyDataSetChanged()
        clearTagFilterMenuItem.isVisible = isClearTagVisible
        toolbar.title = titleString
        return true
    }

    private fun changeSpanCountAndMenuItemIcon(gridOption: GridOption) {
        recyclerViewLayoutManager.spanCount = gridOption.spanCount
        changeGridNumberMenuItem.icon = getDrawable(gridOption.iconID)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        val searchMenuItem = menu!!.findItem(R.id.app_bar_search)

        clearTagFilterMenuItem = menu.findItem(R.id.clearTagFilter)
        changeGridNumberMenuItem = menu.findItem(R.id.changeGridNumberButton)

        val searchView: SearchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = "Search Here!"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                noteAdapters.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = onQueryTextSubmit(newText)
        })

        clearTagFilterMenuItem.setOnMenuItemClickListener {
            setNewListOfNotesToNoteAdapter(notesDao.getAllNotesWithTags(), false, getString(R.string.app_name))
        }

        changeGridNumberMenuItem.setOnMenuItemClickListener {
            actualGridOption = actualGridOption.next()
            changeSpanCountAndMenuItemIcon(actualGridOption)
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun handleNewTag(tag: Tag) {
        tagsSubMenu.add(tag.name).setOnMenuItemClickListener {
            drawerLayout.close()
            setNewListOfNotesToNoteAdapter(notesDao.getNotesThatHaveSpecificTag(tag.tagID), true, tag.name)
        }.icon = ContextCompat.getDrawable(this, R.drawable.ic_label)
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isOpen -> drawerLayout.close()
            clearTagFilterMenuItem.isVisible -> setNewListOfNotesToNoteAdapter(notesDao.getAllNotesWithTags(), false, getString(R.string.app_name))
            else -> super.onBackPressed()
        }
    }
}