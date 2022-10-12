package com.mp08.p01calculadoracriptomonedes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.Currency

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var value: String
    private var currencies = mapOf("BTC" to null, "ADA" to null, "ETH" to null, "LTC" to null)
    private var selectedCurrency = "Currency"
    private lateinit var btnCurrency: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById<TextView>(R.id.TVResult)
        btnCurrency = findViewById<Button>(R.id.BtnCurrency)
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

        findViewById<View>(R.id.BtnCurrency).setOnClickListener() {
            btnCurrencyClick(findViewById(R.id.BtnCurrency))
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
        value = if (value.length > 1) {
            value.substring(0, value.length - 1)
        } else {
            "0"
        }
        showValue()
    }

    private fun btnCurrencyClick (view: View) {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Selecciona una moneda")
        val items = arrayOf(getString(R.string._Currency), "Add Crypto")
        val currencies = currencies.keys.toTypedArray().plus(items)
        dialog.setItems(currencies) { _, which ->
            changeCurrency(currencies[which], view)
        }

        dialog.show()
    }

    private fun changeCurrency(currency: String, view: View) {
        if (currency == getString(R.string._Currency)) {
            Snackbar.make(view, "Selecciona una moneda", Snackbar.LENGTH_SHORT).show()
        } else if (currency == "Add Crypto") {
            addCrypto()
        } else {
            Snackbar.make(view, "Has seleccionado $currency", Snackbar.LENGTH_SHORT).show()
            btnCurrency.text = currency
        }
    }

    private fun addCrypto() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Añade una moneda")


        dialog.show()
    }

}