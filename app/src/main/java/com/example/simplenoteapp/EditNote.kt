package com.example.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.models.Note
import com.example.simplenoteapp.database.NotesDao
import com.example.simplenoteapp.dialogs.EditNoteBottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class EditNote : AppCompatActivity() {
    private lateinit var editTitle: TextInputLayout
    private lateinit var editContents: TextInputLayout
    private lateinit var note: Note
    private lateinit var notesDao: NotesDao
    private lateinit var mainLayout: ConstraintLayout

    private fun saveNoteAndFinishActivity() {
        note.title = editTitle.editText!!.text.toString()
        note.contents = editContents.editText!!.text.toString()

        if (note.title.isNotEmpty() || note.contents.isNotEmpty()) notesDao.insert(note)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTitle = findViewById(R.id.editNoteTitle)
        editContents = findViewById(R.id.editNoteContents)
        mainLayout = findViewById(R.id.note_edit)

        note = intent.getSerializableExtra("NOTE_ARGUMENT") as Note

        notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        editTitle.editText!!.setText(note.title)
        editContents.editText!!.setText(note.contents)
        mainLayout.setBackgroundColor(note.color)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_menu, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)       // set up back button in the menu

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                saveNoteAndFinishActivity()
                return true
            }
            R.id.editNoteShowBottomButton -> {
                val bottomSheet = EditNoteBottomSheetDialog()
                val bundle = Bundle()
                bundle.putSerializable("note", note)
                bottomSheet.arguments = bundle
                bottomSheet.show(supportFragmentManager, "editNoteBottomSheet")
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        saveNoteAndFinishActivity()
        super.onBackPressed()
    }
}