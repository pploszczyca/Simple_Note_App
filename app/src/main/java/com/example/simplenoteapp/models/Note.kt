package com.example.simplenoteapp.models

import java.io.Serializable

class Note(var title: String, var contents: String, var id: Int = 0): Serializable {
}