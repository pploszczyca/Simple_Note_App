package com.example.simplenoteapp

import com.example.simplenoteapp.database.Tag

interface ITagListener {
    fun handleNewTag(tag: Tag)
}