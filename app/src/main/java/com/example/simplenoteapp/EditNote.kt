package com.example.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class EditNote : AppCompatActivity() {

    private var editTitle: TextInputLayout ? = null
    private var editContents: TextInputLayout ? = null
    private var saveButton: MaterialButton ? = null
    private var note: Note? = null
    private var notesDao: NotesDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTitle = findViewById(R.id.editNoteTitle)
        editContents = findViewById(R.id.editNoteContents)
        saveButton = findViewById(R.id.editSaveNoteButton)

        note = intent.getSerializableExtra("NOTE_ARGUMENT") as? Note

        notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        editTitle?.editText!!.setText(note?.title)
        editContents?.editText!!.setText(note?.contents)

        saveButton?.setOnClickListener{
            note?.title = editTitle?.editText!!.text.toString()
            note?.contents = editContents?.editText!!.text.toString()

            notesDao!!.insert(note!!)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_note_menu, menu)
        val showBottomButton = menu!!.findItem(R.id.editNoteShowBottomButton)
        showBottomButton.isVisible = !(note!!.id == -1)     // hide if the new note is being created

        showBottomButton.setOnMenuItemClickListener {
            val bottomSheet = EditNoteBottomSheetDialog()
            val bundle = Bundle()
            bundle.putSerializable("note", note)
            bottomSheet.arguments = bundle
            bottomSheet.show(supportFragmentManager, "editNoteBottomSheet")
            true
        }

        return super.onCreateOptionsMenu(menu)
    }
}