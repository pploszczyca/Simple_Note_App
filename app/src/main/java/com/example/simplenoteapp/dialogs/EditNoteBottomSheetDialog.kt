package com.example.simplenoteapp.dialogs

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
import com.example.simplenoteapp.R
import com.example.simplenoteapp.database.*
import com.example.simplenoteapp.database.models.Note
import com.example.simplenoteapp.database.models.NoteTag
import com.example.simplenoteapp.database.models.Tag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EditNoteBottomSheetDialog: BottomSheetDialogFragment() {
    private lateinit var deleteButton: Button
    private lateinit var shareButton: Button
    private lateinit var pickColorButton: Button
    private lateinit var pinButton: Button
    private lateinit var tagsChipGroup: ChipGroup
    private lateinit var note: Note
    private lateinit var notesDao: NotesDao

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = context?.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun initializeChips() {
        //  ids of tags that are connected with note
        val tags = if (note.noteID != 0) notesDao.getNoteWithTags(noteID = note.noteID).first().tags.map { it.tagID }.toSet() else setOf<Tag>()

        for(tag in notesDao.getAllTags()) {
            val chip = Chip(context)
            Chip(context)
            chip.text = tag.name
            chip.isCloseIconVisible = false
            chip.isCheckable = true
            chip.isClickable = true
            chip.isChecked = tags.contains(tag.tagID)

            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                val noteTag = NoteTag(noteID = note.noteID, tagID = tag.tagID)
                if (isChecked) notesDao.insert(noteTag) else notesDao.delete(noteTag)
            }

            chip.setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.remove))
                    .setMessage("${getString(R.string.remove_tag)}: ${chip.text}?")
                    .setPositiveButton(R.string.remove) {
                            dialog, id ->
                        notesDao.delete(tag)
                        dismiss()
                    }
                    .setNegativeButton(R.string.cancel) {
                            dialog, id ->
                    }
                    .show()
                true
            }

            tagsChipGroup.addView(chip)
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

        if(note.noteID == 0)      // hide delete, share, pin and tags options for new note
            for(optionsView in listOf(deleteButton, shareButton, pinButton, tagsChipGroup))
                optionsView.visibility = View.GONE

        deleteButton.setOnClickListener {       // deleting note from database
            AlertDialog.Builder(context)
                .setTitle("DELETE")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Delete") {
                    dialog, id ->
                        Toast.makeText(context, "Deleted note with title: ${note.title}", Toast.LENGTH_SHORT).show()
                        notesDao.delete(note)
                        activity?.finish()
                        dismiss()
                }
                .setNegativeButton("No") {
                    dialog, id ->
                        dismiss()
                }
                .show()
        }

        shareButton.setOnClickListener {        // sharing title and contents of note to other application
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${note.title}: ${note.contents}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            dismiss()
        }

        pickColorButton.setOnClickListener {        // changing note color
            val colorPickerDialog = ColorPickerDialog(note)
            colorPickerDialog.show(childFragmentManager, "MyFragment")
        }

        pinButton.setOnClickListener {      // pinning note to notification bar
            val builder = NotificationCompat.Builder(requireContext(), "My Notification")
                .setContentTitle(note.title)
                .setContentText(note.contents)
                .setSmallIcon(R.drawable.ic_push_pin)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText(note.contents))

            with(NotificationManagerCompat.from(requireContext())) {
                notify(1, builder.build())
            }
            dismiss()
        }

        initializeChips()

        return view
    }
}