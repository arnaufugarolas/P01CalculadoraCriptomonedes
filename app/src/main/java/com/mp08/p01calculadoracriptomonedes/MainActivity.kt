package com.mp08.p01calculadoracriptomonedes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var value: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById<TextView>(R.id.TVResult)
        setValue("0")
        addEvents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("value", value)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        setValue(savedInstanceState.getString("value").toString())
        showValue()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun addEvents() {
        for (i in 0..9) {
            val id = resources.getIdentifier("BtnNum$i", "id", packageName)
            val btn = findViewById<View>(id)
            btn.setOnClickListener {
                if (value == "0") {
                    setValue(i.toString())
                } else {
                    addValue(i.toString())
                }
            }
        }
        findViewById<View>(R.id.BtnCE).setOnClickListener() {
            setValue("0")
        }
        findViewById<View>(R.id.BtnDEL).setOnClickListener() {
            deleteLast()
        }
        findViewById<View>(R.id.BtnComa).setOnClickListener() {
            if (!value.contains(",") && value != "") {
                addValue(",")
            }
        }
    }

    private fun showValue() {
        display.text = value.replace(".", ",")
    }

    private fun setValue(value: String) {
        this.value = value
        showValue()
    }

    private fun addValue(value: String) {
        this.value = this.value.plus(value)
        showValue()
    }

    private fun deleteLast() {
        this.value = this.value.dropLast(1)
        showValue()
    }

}