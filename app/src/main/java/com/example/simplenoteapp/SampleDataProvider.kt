package com.example.simplenoteapp

import com.example.simplenoteapp.database.Note
import com.example.simplenoteapp.database.NotesDao
import com.example.simplenoteapp.database.Tag

object SampleDataProvider {
    fun setSampleNotes(notesDao: NotesDao): Unit{
        if(notesDao.getAllNotesWithTags().isEmpty()) {
            notesDao.insert(notes = arrayOf(
                Note("Test", "Is it working?"),
                Note("Shopping", "Bread, butter, milk"),
                Note("Reminder!", "Say happy birthday to brother"),
                Note("Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec accumsan porttitor massa sit amet dictum. Nulla cursus orci ac erat ullamcorper, quis pharetra metus porttitor. Integer id mauris euismod, eleifend dui efficitur, sagittis orci. Pellentesque efficitur massa at risus ullamcorper tristique. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce placerat quis lacus ac fermentum. Fusce sit amet arcu sit amet purus dictum lobortis sed a arcu.\n" +
                        "\n" +
                        "Mauris posuere, sem a suscipit molestie, ante eros vulputate mauris, quis pellentesque lectus urna vitae sapien. Aliquam et purus condimentum, mollis dui sed, vulputate urna. Donec porttitor ante nec massa viverra, id ultricies diam bibendum. Duis pharetra velit velit, a congue eros luctus a. Nulla et fermentum arcu. Donec mollis ut nunc eu eleifend. Duis ut libero aliquam, aliquet urna in, pharetra dui. In ut sem ac felis finibus venenatis. Etiam ultricies consectetur ligula at iaculis.\n" +
                        "\n"),
                Note("Lorem Ipsum 2", "\n" +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla eget ipsum accumsan, egestas lacus a, volutpat velit. Praesent quis porta est. Quisque pretium tortor sem, et rutrum ipsum tincidunt eu. Vestibulum suscipit erat eget nisl mattis imperdiet. In non turpis aliquam, finibus elit vel, facilisis sapien. Mauris congue, metus sit amet eleifend placerat, libero leo dignissim nibh, vitae molestie ipsum erat at lacus. Aenean odio diam, scelerisque vel felis non, hendrerit bibendum augue. Sed nec ligula eu sapien venenatis pharetra. Aenean at sollicitudin est. Integer id erat sit amet nunc dictum porta id ut tortor.")
            ))
        }
    }

    fun setSampleTags(notesDao: NotesDao): Unit {
        if(notesDao.getAllTags().isEmpty()) {
            notesDao.insert(tags = arrayOf(
                Tag("Shop"),
                Tag("School"),
                Tag("Work"),
                Tag("Entertainment"),
                Tag("Check it later")
            ))
        }
    }
}