package com.example.simplenoteapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.simplenoteapp.database.Note
import com.google.android.material.slider.Slider

class ColorPickerDialog(val note: Note): DialogFragment() {
    private var colorView: View ? = null
    private var redSlider: Slider ? = null
    private var greenSlider: Slider ? = null
    private var blueSlider: Slider ? = null

    @SuppressLint("NewApi")
    private fun calculateHexColor(): Int = Color.rgb(redSlider!!.value.toInt(), greenSlider!!.value.toInt(), blueSlider!!.value.toInt())

    private val sliderChange = Slider.OnChangeListener { slider, value, fromUser ->
        colorView!!.setBackgroundColor(calculateHexColor())
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

        redSlider!!.addOnChangeListener(sliderChange)
        greenSlider!!.addOnChangeListener(sliderChange)
        blueSlider!!.addOnChangeListener(sliderChange)

        redSlider!!.value = Color.red(note.color).toFloat()
        blueSlider!!.value = Color.blue(note.color).toFloat()
        greenSlider!!.value = Color.green(note.color).toFloat()

        builder.setView(view)
            .setPositiveButton("Set") {
                _, _ ->
                note.color = calculateHexColor()
            }
        return builder.create()
    }
}