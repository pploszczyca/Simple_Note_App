package com.example.simplenoteapp

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NoteTag
import com.example.simplenoteapp.database.NotesDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EditNoteBottomSheetDialog: BottomSheetDialogFragment() {
    private var deleteButton: Button ? = null
    private var shareButton: Button ? = null
    private var pickColorButton: Button ? = null
    private var pinButton: Button ? = null
    private var tagsChipGroup: ChipGroup ? = null
    private var note: Note ?= null
    private var notesDao: NotesDao? = null

    private fun createNotificationChannel(): Unit {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context?.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.edit_note_bottom_sheet_layout, container, false)
        deleteButton = view.findViewById(R.id.editNoteDeleteButton)
        shareButton = view.findViewById(R.id.editNoteShareButton)
        pickColorButton = view.findViewById(R.id.editNotePickColorButton)
        pinButton = view.findViewById(R.id.editNotePinButton)
        tagsChipGroup = view.findViewById(R.id.tagsChipGroup)
        note = arguments?.getSerializable("note") as Note
        notesDao = AppDatabase.getInstance(requireContext()).notesDao()

        createNotificationChannel()

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
            dismiss()
        }

        shareButton!!.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${note!!.title}: ${note!!.contents}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            dismiss()
        }

        pickColorButton!!.setOnClickListener {
            val colorPickerDialog = ColorPickerDialog(note!!)
            colorPickerDialog.show(childFragmentManager, "MyFragment")
        }

        pinButton!!.setOnClickListener {
            val builder = NotificationCompat.Builder(requireContext(), "My Notification")
                .setContentTitle(note!!.title)
                .setContentText(note!!.contents)
                .setSmallIcon(R.drawable.ic_push_pin)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(note!!.contents))

            with(NotificationManagerCompat.from(requireContext())) {
                notify(1, builder.build())
            }
            dismiss()
        }

        val tags = notesDao!!.getNoteWithTags(noteID = note!!.noteID).first().tags.map { it.tagID }.toSet()

        for(tag in notesDao!!.getAllTags()) {
            val chip = Chip(context)
            chip.text = tag.name
            chip.isCloseIconVisible = false
            chip.isCheckable = true
            chip.isClickable = true

            if(tags.contains(tag.tagID))    chip.isChecked = true

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                val noteTag = NoteTag(noteID = note!!.noteID, tagID = tag.tagID)
                if (isChecked) notesDao!!.insert(noteTag) else notesDao!!.delete(noteTag)
            }

            tagsChipGroup!!.addView(chip)
        }

        return view
    }
}