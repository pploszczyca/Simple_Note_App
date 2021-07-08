package com.example.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplenoteapp.models.Note
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class EditNote : AppCompatActivity() {

    private var editTitle: TextInputLayout ? = null
    private var editContents: TextInputLayout ? = null
    private var saveButton: MaterialButton ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTitle = findViewById(R.id.editNoteTitle)
        editContents = findViewById(R.id.editNoteContents)
        saveButton = findViewById(R.id.editSaveNoteButton)

        val note: Note? = intent.getSerializableExtra("NOTE_ARGUMENT") as? Note

        editTitle?.editText!!.setText(note?.title)
        editContents?.editText!!.setText(note?.contents)

    }
}