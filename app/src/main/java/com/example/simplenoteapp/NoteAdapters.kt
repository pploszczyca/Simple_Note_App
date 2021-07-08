package com.example.simplenoteapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.simplenoteapp.models.Note

class NoteAdapters(var context: Context, var arrayList: ArrayList<Note>) :BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.note_card_view, null)

        val title: TextView = view.findViewById(R.id.noteTitle)
        val contents: TextView = view.findViewById(R.id.contentsTitle)

        val note: Note = arrayList.get(position)

        title.text = note.title
        contents.text = note.contents

        return view!!
    }
}