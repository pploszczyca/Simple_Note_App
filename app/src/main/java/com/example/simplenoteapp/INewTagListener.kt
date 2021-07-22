package com.example.simplenoteapp

import com.example.simplenoteapp.database.Tag

interface INewTagListener {
    fun handleNewTag(tag: Tag)
}