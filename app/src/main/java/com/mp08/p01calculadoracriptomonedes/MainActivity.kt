package com.mp08.p01calculadoracriptomonedes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById<TextView>(R.id.TVResult)
        addEvents()
    }

    private fun addEvents() {
        for (i in 0..9) {
            val id = resources.getIdentifier("BtnNum$i", "id", packageName)
            val btn = findViewById<View>(id)
            btn.setOnClickListener {
                if (display.text == "0") {
                    display.text = i.toString()
                } else {
                    display.text = display.text.toString().plus(i)
                }
            }
        }
        findViewById<View>(R.id.BtnCE).setOnClickListener() {
            display.text = ""
        }
        findViewById<View>(R.id.BtnDEL).setOnClickListener() {
            display.text = display.text.toString().dropLast(1)
        }
        findViewById<View>(R.id.BtnComa).setOnClickListener() {
            if (!display.text.toString().contains(",") && display.text.toString().isNotEmpty()) {
                display.text = display.text.toString().plus(",")
            }
        }
    }
}