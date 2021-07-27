package com.example.simplenoteapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenoteapp.EditNote
import com.example.simplenoteapp.R
import com.example.simplenoteapp.database.support_models.NoteWithTags
import com.google.android.material.card.MaterialCardView

class NoteAdapters(var arrayList: List<NoteWithTags>) :RecyclerView.Adapter<NoteAdapters.ViewHolder>(), Filterable {
    private val arrayListCopy: List<NoteWithTags> = arrayList

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.note_title)
        val contents: TextView = view.findViewById(R.id.contents_title)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NoteAdapters.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_card_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: NoteAdapters.ViewHolder, position: Int) {
        val noteWithTags: NoteWithTags = arrayList[position]

        viewHolder.title.text = noteWithTags.note.title
        viewHolder.contents.text = noteWithTags.note.contents
        (viewHolder.itemView as MaterialCardView).setCardBackgroundColor(noteWithTags.note.color)

        viewHolder.itemView.setOnClickListener {
            val intent: Intent = Intent(it!!.context, EditNote::class.java)
            intent.putExtra(EditNote.NOTE_ARGUMENT, arrayList[position].note)
            val options = ActivityOptionsCompat.makeScaleUpAnimation(it, 0, 0, it.width, it.height).toBundle()
            startActivity(it.context, intent, options)
        }
    }

    override fun getItemCount(): Int = arrayList.size

    override fun getFilter(): Filter = object :  Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            results.values = if (constraint == null || constraint.isEmpty()) arrayListCopy else arrayListCopy.filter { noteWithTags -> noteWithTags.note.title.contains(constraint, ignoreCase = true)}
            results.count = arrayList.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            arrayList = results!!.values as List<NoteWithTags>
            notifyDataSetChanged()
        }
    }
}

