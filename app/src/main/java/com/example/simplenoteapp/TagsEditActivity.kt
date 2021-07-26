package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.simplenoteapp.adapters.TagAdapters
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.NotesDao
import com.example.simplenoteapp.database.models.Tag
import com.example.simplenoteapp.dialogs.AddingNewTagDialog
import kotlinx.android.synthetic.main.activity_tags_edit.*

class TagsEditActivity : AppCompatActivity(), ITagListener {
    private lateinit var listView: ListView
    private lateinit var tagAdapters: TagAdapters
    private lateinit var trashButton: MenuItem
    private lateinit var notesDao: NotesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags_edit)
        setSupportActionBar(editTagsToolbar)

        listView = findViewById(R.id.editTagsListView)
        notesDao = AppDatabase.getInstance(applicationContext).notesDao()
        tagAdapters = TagAdapters(applicationContext, notesDao.getAllTags(), this, supportFragmentManager)

        listView.adapter = tagAdapters
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tags_edit_menu, menu)
        trashButton = menu!!.findItem(R.id.deleteTagsButton)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.deleteTagsButton -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.remove)
                    .setMessage(R.string.remove_tags)
                    .setPositiveButton(R.string.remove) {
                        _, _ ->
                            tagAdapters.getCheckedCardsSet().forEach { notesDao.delete(it) }
                            tagAdapters.clearCheckedCardsSet()
                            tagAdapters.tagList = notesDao.getAllTags()
                            tagAdapters.notifyDataSetChanged()
                            trashButton.isVisible = false
                    }
                    .setNegativeButton(R.string.cancel) {
                        _, _ ->
                    }
                    .show()

                return true
            }

            R.id.addNewTagInEditActivity -> {
                AddingNewTagDialog(this).show(supportFragmentManager, "Adding new tag")
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun handleNewTag(tag: Tag) {
        trashButton.isVisible = (tagAdapters.amountOfCheckedCards() != 0)
        tagAdapters.addTagToList(tag)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}