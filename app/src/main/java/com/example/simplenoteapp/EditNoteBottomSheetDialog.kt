package com.example.simplenoteapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNoteBottomSheetDialog: BottomSheetDialogFragment() {
    private var deleteButton: Button ? = null
    private var note: Note ?= null
    private var notesDao: NotesDao? = null

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.edit_note_bottom_sheet_layout, container, false)
        deleteButton = view.findViewById(R.id.editNoteDeleteButton)
        note = arguments?.getSerializable("note") as Note
        notesDao = AppDatabase.getInstance(requireContext()).notesDao()

        deleteButton!!.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("DELETE")
            builder.setMessage("Do you want to delete this note?")
            builder.setPositiveButton("Delete") {
                    dialog, id ->
                Toast.makeText(context, "Deleted note with title: ${note!!.title}", Toast.LENGTH_SHORT).show()
                notesDao!!.delete(note!!)
                activity?.finish()
            }
            builder.setNegativeButton("No") {
                    dialog, id ->
            }
            builder.show()
            true
        }

        return view
    }
}