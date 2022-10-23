package com.mp08.p01calculadoracriptomonedes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

interface AddValueToCryptoInterface {
    fun addValueToCryptoGetData(name: String?, value: String?)
}
interface AddCryptoInterface {
    fun addCryptoGetData(name: String?, value: String?)
}

class MainActivity : AppCompatActivity(), AddCryptoInterface, AddValueToCryptoInterface {
    private lateinit var display: TextView
    private lateinit var btnCurrency: Button
    private lateinit var value: String
    private lateinit var stableCurrency: String
    private lateinit var currenciesNames: Array<String>
    private lateinit var currenciesValues: Array<String>
    private lateinit var currentCurrency: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("value", value)
        outState.putStringArray("currenciesNames", currenciesNames)
        outState.putStringArray("currenciesValues", currenciesValues)
        outState.putString("currentCurrency", currentCurrency)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        setValue(savedInstanceState.getString("value").toString())
        currenciesNames = savedInstanceState.getStringArray("currenciesNames")!!
        currenciesValues = savedInstanceState.getStringArray("currenciesValues")!!
        currentCurrency = savedInstanceState.getString("currentCurrency").toString()
        btnCurrency.text = currentCurrency

        showValue()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun init() {
        display = findViewById<TextView>(R.id.TVResult)
        btnCurrency = findViewById<Button>(R.id.BtnCurrency)
        stableCurrency = getString(R.string._Currency).toString()
        currenciesNames = arrayOf(stableCurrency, "BTC", "ADA", "ETH", "LTC")
        currenciesValues = arrayOf("1", "null", "null", "null", "null")
        currentCurrency = stableCurrency
        addEvents()
        setValue("0")
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
            if (!value.contains(".")) {
                addValue(".")
            }
        }

        btnCurrency.setOnClickListener() {
            btnCurrencyClick()
        }

        btnCurrency.setOnLongClickListener() {
            btnCurrencyLongClick()
            true
        }
    }

    private fun showValue() {
        display.text = value.replace(".", ",")
    }

    private fun setValue(newValue: String) {
        value = if (newValue.contains(".")) {
            val parts = newValue.split(".")

            if (parts[1].length > 8) "${parts[0]}.${parts[1].substring(0, 8)}"
            else if (parts[1] == "0") parts[0]
            else newValue
        } else newValue

        showValue()
    }

    private fun addValue(newValue: String) {
        if (value.split(".").size == 2 && value.split(".")[1].length == 8) {
            return
        }
        value = value.plus(newValue)
        showValue()
    }

    private fun deleteLast() {
        value = when {
            value.length > 1 -> value.substring(0, value.length - 1)
            else -> "0"
        }
        showValue()
    }

    private fun btnCurrencyClick () {
        val dialog = MaterialAlertDialogBuilder(this)
        val items = currenciesNames.plus("Add new currency")

        dialog.setTitle("Exchange")
        dialog.setItems(items) { _, which ->
            if (which == items.size - 1) addNewCrypto()
            else exchangeCurrency(items[which])
        }

        dialog.show()
    }

    private fun btnCurrencyLongClick (){
        val dialog = MaterialAlertDialogBuilder(this)
        val currencies = currenciesNames.drop(currenciesNames.indexOf(stableCurrency) + 1).toTypedArray()

        dialog.setTitle("Set prices")
        dialog.setItems(currencies) { _, which ->
            changeCryptoValue(currencies[which])
        }

        dialog.show()
    }

    private fun addNewCrypto() {
        val addCryptoDialog = AddCryptoDialog()
        addCryptoDialog.show(supportFragmentManager, "AddCryptoDialog")
    }

    private fun exchangeCurrency(name: String) {
        val currencyIndex = currenciesNames.indexOf(name)
        if (currencyIndex == -1) {
            Snackbar.make(findViewById(R.id.BtnCurrency), "This currency is not available", Snackbar.LENGTH_SHORT).show()
        } else if (currenciesValues[currencyIndex] == "null") {
            changeCryptoValue(name)
        } else {
            exchangeCryptoValue(name)
        }
    }

    private fun exchangeCryptoValue(name: String) {
        if (currentCurrency == name) return
        else if (name == stableCurrency) {
            setValue((value.toDouble() * currenciesValues[currenciesNames.indexOf(currentCurrency)].toDouble()).toString())
        } else {
            setValue((value.toDouble() * currenciesValues[currenciesNames.indexOf(currentCurrency)].toDouble()).toString())
            setValue((value.toDouble() / currenciesValues[currenciesNames.indexOf(name)].toDouble()).toString())
        }
        currentCurrency = name
        btnCurrency.text = name
    }

    private fun changeCryptoValue(name: String) {
        val addValueToCryptoDialog = AddValueToCrypto(name)
        addValueToCryptoDialog.show(supportFragmentManager, "AddValueToCryptoDialog")
    }

    override fun addCryptoGetData(name: String?, value: String?) {
        if (name != null && value != null) {
            currenciesNames = currenciesNames.plus(name)
            currenciesValues = currenciesValues.plus(value)

            Snackbar.make(findViewById(R.id.BtnCurrency), "Currency added", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(findViewById(R.id.BtnCurrency), "Error adding currency", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun addValueToCryptoGetData(name: String?, value: String?) {
        if (name != null && value != null) {
            if (currenciesValues[currenciesNames.indexOf(name)] == "null") {
                currenciesValues[currenciesNames.indexOf(name)] = value
                Snackbar.make(findViewById(R.id.BtnCurrency), "Currency value added", Snackbar.LENGTH_SHORT).show()
                exchangeCurrency(name)
            } else {
                currenciesValues[currenciesNames.indexOf(name)] = value
                Snackbar.make(findViewById(R.id.BtnCurrency), "Currency value changed", Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(findViewById(R.id.BtnCurrency), "Error changing value", Snackbar.LENGTH_SHORT).show()
        }
    }

}
