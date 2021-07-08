package com.example.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.example.simplenoteapp.models.Note

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var listView:ListView ? = null
    private var noteAdapters:NoteAdapters ? = null
    private var arrayList: ArrayList<Note> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.notesList)
        arrayList = SampleDataProvider.getSampleNotes()
        noteAdapters = NoteAdapters(applicationContext, arrayList!!)
        listView?.adapter = noteAdapters
        listView?.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val noteItem: Note = arrayList?.get(position)!!
//        Toast.makeText(applicationContext, "Clicked note with title: ${noteItem.title}", Toast.LENGTH_SHORT).show()

        val intent: Intent = Intent(applicationContext, EditNote::class.java)
        intent.putExtra("NOTE_ARGUMENT", noteItem)
        startActivity(intent)
    }
}