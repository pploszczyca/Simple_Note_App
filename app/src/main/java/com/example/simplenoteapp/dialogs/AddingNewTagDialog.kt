package com.example.simplenoteapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.simplenoteapp.ITagListener
import com.example.simplenoteapp.R
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.models.Tag
import com.google.android.material.textfield.TextInputLayout

class AddingNewTagDialog(private val tagListener: ITagListener, private val tag: Tag = Tag()) : DialogFragment() {
    private lateinit var textInput: TextInputLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder =  AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_adding_new_tag_dialog, null)

        textInput = view.findViewById(R.id.addNewTagTextInput)

        textInput.editText?.setText(tag.name)

        builder.setView(view)
            .setTitle(R.string.add_new_tag)
            .setPositiveButton(R.string.add) { _, _ ->
                tag.name = textInput.editText!!.text.toString()
                AppDatabase.getInstance(context).notesDao().insert(tag)
                tagListener.handleNewTag(tag)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }

        return builder.create()
    }

}