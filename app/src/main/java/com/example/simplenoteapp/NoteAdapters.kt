package com.example.simplenoteapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.simplenoteapp.database.Note

class NoteAdapters(var context: Context, var arrayList: List<Note>) :BaseAdapter(), Filterable {
    private val arrayListCopy: List<Note> = arrayList

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

        return view
    }

    override fun getFilter(): Filter = object :  Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = if (constraint == null || constraint.isEmpty()) arrayListCopy else arrayListCopy.filter { it.title.contains(constraint, ignoreCase = true) }
            results.count = arrayList.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            arrayList = results!!.values as List<Note>
            notifyDataSetChanged()
        }
    }
}

