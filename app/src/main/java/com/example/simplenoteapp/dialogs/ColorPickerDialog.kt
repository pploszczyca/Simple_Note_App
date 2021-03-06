package com.example.simplenoteapp.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.simplenoteapp.R
import com.example.simplenoteapp.database.models.Note
import com.google.android.material.slider.Slider

class ColorPickerDialog(private val note: Note): DialogFragment() {
    private lateinit var colorView: View
    private lateinit var redSlider: Slider
    private lateinit var greenSlider: Slider
    private lateinit var blueSlider: Slider

    @SuppressLint("NewApi")
    private fun calculateHexColor(): Int = Color.rgb(redSlider.value.toInt(), greenSlider.value.toInt(), blueSlider.value.toInt())

    private val sliderChange = Slider.OnChangeListener { slider, value, fromUser ->
        colorView.setBackgroundColor(calculateHexColor())
    }

    private fun setNoteColorAndSetBackground(newColor: Int) {
        note.color = newColor
        activity?.findViewById<ConstraintLayout>(R.id.note_edit)?.setBackgroundColor(note.color)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)

        val builder =  AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.color_picker_layout, null)

        colorView = view.findViewById(R.id.color_picker_view)
        redSlider = view.findViewById(R.id.red_color_picker)
        greenSlider = view.findViewById(R.id.green_color_picker)
        blueSlider = view.findViewById(R.id.blue_color_picker)

        for((slider, colorValue) in arrayOf(
            Pair(redSlider, Color.red(note.color).toFloat()),
            Pair(greenSlider, Color.green(note.color).toFloat()),
            Pair(blueSlider, Color.blue(note.color).toFloat()))
        ){
            slider.addOnChangeListener(sliderChange)
            slider.value = colorValue
        }

        builder.setView(view)
            .setPositiveButton("Set") {
                _, _ -> setNoteColorAndSetBackground(calculateHexColor())
            }
            .setNegativeButton("Reset") {
                _, _ -> setNoteColorAndSetBackground(Color.TRANSPARENT)
            }
        return builder.create()
    }
}