package com.mp08.p01calculadoracriptomonedes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

interface CryptoDetailsInterface {
    fun createCrypto(name: String, value: String)
    fun modifyCrypto(name: String, value: String)
}

class MainActivity : AppCompatActivity(), CryptoDetailsInterface {
    private lateinit var display: TextView
    private lateinit var btnCurrency: Button
    private lateinit var value: String
    private lateinit var stableCurrency: String
    private lateinit var currenciesNames: ArrayList<String>
    private lateinit var currenciesValues: ArrayList<String>
    private lateinit var currentCurrency: String


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        init()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("value", value)
        outState.putStringArrayList("currenciesNames", currenciesNames)
        outState.putStringArrayList("currenciesValues", currenciesValues)
        outState.putString("currentCurrency", currentCurrency)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        setValue(savedInstanceState.getString("value").toString())
        currenciesNames = savedInstanceState.getStringArrayList("currenciesNames")!!
        currenciesValues = savedInstanceState.getStringArrayList("currenciesValues")!!
        currentCurrency = savedInstanceState.getString("currentCurrency").toString()
        btnCurrency.text = currentCurrency

        showValueOnDisplay()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun init() {
        display = findViewById(R.id.TVResult)
        btnCurrency = findViewById(R.id.ButtonCurrency)
        stableCurrency = getString(R.string.StableCurrency)
        currenciesNames = arrayListOf(stableCurrency, "BTC", "ADA", "ETH", "LTC")
        currenciesValues = arrayListOf("1", "null", "null", "null", "null")
        currentCurrency = stableCurrency

        addEvents()
        setValue("0")
    }

    private fun addEvents() {
        for (i in 0..9) {
            val id = resources.getIdentifier("ButtonNumber$i", "id", packageName)
            val btn = findViewById<View>(id)
            btn.setOnClickListener {
                if (value == "0") {
                    setValue(i.toString())
                } else {
                    addValue(i.toString())
                }
            }
        }
        findViewById<View>(R.id.ButtonCE).setOnClickListener {
            setValue("0")
        }
        findViewById<View>(R.id.ButtonDeleteLast).setOnClickListener {
            deleteLastCharacterOfValue()
        }
        findViewById<View>(R.id.ButtonComa).setOnClickListener {
            if (!value.contains(".")) {
                addValue(".")
            }
        }
        btnCurrency.setOnClickListener {
            currencyButtonClickHandler()
        }
        btnCurrency.setOnLongClickListener {
            currencyButtonLongClickHandler()
            true
        }
    }

    private fun showValueOnDisplay() {
        display.text = value.replace(".", ",")
    }

    private fun setValue(newValue: String) {
        newValue.replace(",", ".")
        value = if (newValue.contains(".")) {
            val parts = newValue.split(".")
            if (parts[1].length > 8) {
                "%.8f".format(newValue.toDouble())
            }
            else if (parts[1].matches("^0+$".toRegex())) parts[0]
            else newValue
        } else newValue

        showValueOnDisplay()
    }

    private fun addValue(newValue: String) {
        if (value.contains(".")) {
            if (value.split(".")[1].length >= 8) return
        }
        value = value.plus(newValue)
        showValueOnDisplay()
    }

    private fun deleteLastCharacterOfValue() {
        value = when {
            value.length > 1 -> value.substring(0, value.length - 1)
            else -> "0"
        }
        showValueOnDisplay()
    }

    private fun currencyButtonClickHandler() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle(getString(R.string.TitleExchange))

        val items = arrayListOf<String>()
        items.addAll(currenciesNames)
        items.remove(currentCurrency)
        items.add(getString(R.string.AddCurrency))

        dialog.setItems(items.toTypedArray()) { _, which ->
            if (which == items.size - 1) createCurrencyDialog()
            else exchangeValue(items[which])
        }

        dialog.show()
    }

    private fun createCurrencyDialog() {
        val createCryptoDialog = CryptoDetailsDialog("null", "null")
        createCryptoDialog.show(supportFragmentManager, "AddCryptoDialog")
    }

    private fun exchangeValue(name: String) {
        if (currentCurrency == name) return
        else if (currenciesValues[currenciesNames.indexOf(name)] == "null") {
            modifyCurrencyDialog(name)
        } else {
            try {
                val currentCurrencyRate =
                    currenciesValues[currenciesNames.indexOf(currentCurrency)].toDouble()
                val newCurrencyRate = currenciesValues[currenciesNames.indexOf(name)].toDouble()
                var currentValue = value.toDouble()

                if (name == stableCurrency) {
                    setValue((currentValue * currentCurrencyRate).toString())
                } else {
                    currentValue *= currentCurrencyRate
                    setValue((currentValue / newCurrencyRate).toString())
                }
                currentCurrency = name
                btnCurrency.text = name
            } catch (e: Exception) {
                Log.e("Error", e.toString())
                Snackbar.make(
                    findViewById(R.id.TVResult),
                    getString(R.string.ErrorExchange),
                    Snackbar.LENGTH_SHORT
                ).setTextColor(getColor(R.color.RubineRed)).show()
            }

        }
    }

    private fun currencyButtonLongClickHandler() {
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle(R.string.TitleChangeRate)

        val items = arrayListOf<String>()
        items.addAll(currenciesNames)
        items.remove(stableCurrency)

        dialog.setItems(items.toTypedArray()) { _, which ->
            modifyCurrencyDialog(items[which])
        }

        dialog.show()
    }

    private fun modifyCurrencyDialog(name: String) {
        val modifyCryptoDialog =
            CryptoDetailsDialog(name, currenciesValues[currenciesNames.indexOf(name)])
        modifyCryptoDialog.show(supportFragmentManager, "AddValueToCryptoDialog")
    }

    override fun createCrypto(name: String, value: String) {
        if (name == "null") {
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.EmptyName),
                Snackbar.LENGTH_SHORT
            ).setTextColor(getColor(R.color.RubineRed)).show()
        } else if (currenciesNames.contains(name)) {
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.CryptoAlreadyExists),
                Snackbar.LENGTH_SHORT
            ).setTextColor(getColor(R.color.RubineRed)).show()
        } else {
            currenciesNames.add(name)
            currenciesValues.add(value)
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.CryptoAdded),
                Snackbar.LENGTH_SHORT
            ).show()
            exchangeValue(name)
        }
    }

    override fun modifyCrypto(name: String, value: String) {
        if (name == "null") {
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.EmptyName),
                Snackbar.LENGTH_SHORT
            ).setTextColor(getColor(R.color.RubineRed)).show()
        } else if (!currenciesNames.contains(name)) {
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.CryptoDoesntExist),
                Snackbar.LENGTH_SHORT
            ).setTextColor(getColor(R.color.RubineRed)).show()
        } else {
            currenciesValues[currenciesNames.indexOf(name)] = value
            Snackbar.make(
                findViewById(R.id.ButtonCurrency),
                getString(R.string.CryptoModified),
                Snackbar.LENGTH_SHORT
            ).show()
            exchangeValue(name)
        }
    }
}
