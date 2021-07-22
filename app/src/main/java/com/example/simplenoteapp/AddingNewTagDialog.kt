package com.example.simplenoteapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.DialogFragment
import com.example.simplenoteapp.database.AppDatabase
import com.example.simplenoteapp.database.Tag
import com.google.android.material.textfield.TextInputLayout


class AddingNewTagDialog(private val newTagListener: INewTagListener) : DialogFragment() {
    private var textInput: TextInputLayout ? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val builder =  AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_adding_new_tag_dialog, null)

        textInput = view.findViewById(R.id.addNewTagTextInput)

        builder.setView(view)
            .setTitle(R.string.add_new_tag)
            .setPositiveButton(R.string.add) { _, _ ->
                val tag = Tag(textInput!!.editText!!.text.toString())
                newTagListener.handleNewTag(tag)
                AppDatabase.getInstance(context).notesDao().insert(tag)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }

        return builder.create()
    }

}