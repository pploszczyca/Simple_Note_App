package com.example.simplenoteapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
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
        val deleteButton = menu!!.findItem(R.id.editNoteDeleteButton)
        deleteButton.isVisible = !(note!!.id == -1)         // button only visible for existing notes
        deleteButton.setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(this@EditNote)
            builder.setTitle("DELETE")
            builder.setMessage("Do you want to delete this note?")
            builder.setPositiveButton("Delete") {
                dialog, id ->
                Toast.makeText(this, "Deleted note with title: ${note!!.title}", Toast.LENGTH_SHORT).show()
                notesDao!!.delete(note!!)
                finish()
            }
            builder.setNegativeButton("No") {
                dialog, id ->
            }
            builder.show()
            true
        }

        return super.onCreateOptionsMenu(menu)
    }
}