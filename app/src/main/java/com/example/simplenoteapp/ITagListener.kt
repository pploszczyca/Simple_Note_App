package com.example.simplenoteapp

import com.example.simplenoteapp.database.models.Tag

interface ITagListener {
    fun handleNewTag(tag: Tag)
}