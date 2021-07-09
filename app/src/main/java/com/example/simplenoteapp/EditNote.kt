package com.example.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class EditNote : AppCompatActivity() {

    private var editTitle: TextInputLayout ? = null
    private var editContents: TextInputLayout ? = null
    private var saveButton: MaterialButton ? = null
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTitle = findViewById(R.id.editNoteTitle)
        editContents = findViewById(R.id.editNoteContents)
        saveButton = findViewById(R.id.editSaveNoteButton)

        note = intent.getSerializableExtra("NOTE_ARGUMENT") as? Note

        val notesDao = AppDatabase.getInstance(applicationContext).notesDao()

        editTitle?.editText!!.setText(note?.title)
        editContents?.editText!!.setText(note?.contents)

        saveButton?.setOnClickListener{
            note?.title = editTitle?.editText!!.text.toString()
            note?.contents = editContents?.editText!!.text.toString()

            notesDao.insert(note!!)
            finish()
        }
    }
}