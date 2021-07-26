package com.example.simplenoteapp.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.simplenoteapp.ITagListener
import com.example.simplenoteapp.R
import com.example.simplenoteapp.database.models.Tag
import com.example.simplenoteapp.dialogs.AddingNewTagDialog
import com.google.android.material.card.MaterialCardView

class TagAdapters(var context: Context, var tagList: List<Tag>, private val tagListener: ITagListener, private val fragmentManager: FragmentManager): BaseAdapter() {
    private val checkedCardsSet: MutableSet<Tag> = mutableSetOf()

    override fun getCount(): Int =  tagList.size

    override fun getItem(position: Int): Any = tagList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    private fun changeChecked(materialCardView: MaterialCardView, tag: Tag) {
        materialCardView.isChecked = !materialCardView.isChecked
        if(materialCardView.isChecked) checkedCardsSet.add(tag) else checkedCardsSet.remove(tag)
        tagListener.handleNewTag(tag)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val materialCardView = View.inflate(context, R.layout.tag_edit_card_view, null) as MaterialCardView
        val tag = getItem(position) as Tag

        val tagEditTextView: TextView = materialCardView.findViewById(R.id.tagEditTextView)

        materialCardView.setOnClickListener {
            if(checkedCardsSet.size > 0)
                changeChecked(materialCardView, tag)
            else
                AddingNewTagDialog(tagListener, tag).show(fragmentManager, "Update tag fragment")
        }

        materialCardView.setOnLongClickListener {
            changeChecked(materialCardView, tag)
            true
        }

        tagEditTextView.text = tag.name

        return materialCardView
    }

    fun getCheckedCardsSet(): Set<Tag> = checkedCardsSet.toSet()
    fun clearCheckedCardsSet(): Unit = checkedCardsSet.clear()
    fun amountOfCheckedCards(): Int = checkedCardsSet.size

    fun addTagToList(tag: Tag) {
        if(!tagList.contains(tag)) {
            tagList += tag
            notifyDataSetChanged()
        }
    }
}