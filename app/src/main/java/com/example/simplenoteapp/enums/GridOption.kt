package com.example.simplenoteapp.enums

import com.example.simplenoteapp.R

enum class GridOption(val spanCount: Int, val iconID: Int) {
    SINGLE_COLUMN(1, R.drawable.ic_table_rows) {
        override fun next(): GridOption = TWO_COLUMNS
    },
    TWO_COLUMNS(2, R.drawable.ic_border_all) {
        override fun next(): GridOption = THREE_COLUMNS
    },
    THREE_COLUMNS(3, R.drawable.ic_grid_on) {
        override fun next(): GridOption = SINGLE_COLUMN
    };

    abstract fun next(): GridOption
}