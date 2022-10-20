package com.mp08.p01calculadoracriptomonedes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.Currency

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
    private lateinit var currencies: Map<String, Double?>
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
        val items = currenciesNames.plus("Add new currency")

        dialog.setTitle("Select a currency")
        dialog.setItems(items) { _, which ->
            if (which == items.size - 1) addNewCrypto()
            else changeCurrency(items[which])
        }

        dialog.show()
    }

    private fun addNewCrypto() {
        val addCryptoDialog = AddCryptoDialog()
        addCryptoDialog.show(supportFragmentManager, "AddCryptoDialog")
    }

    private fun changeCurrency(name: String) {
        val currencyIndex = currenciesNames.indexOf(name)
        if (currencyIndex == -1) {
            Snackbar.make(findViewById(R.id.BtnCurrency), "This currency is not available", Snackbar.LENGTH_SHORT).show()
        } else if (currenciesValues[currencyIndex] == "null") {
            changeCryptoValue(name)
        } else {
            // Convert currency
            currentCurrency = name
            btnCurrency.text = currentCurrency
        }
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
            currenciesValues[currenciesNames.indexOf(name)] = value

            Snackbar.make(findViewById(R.id.BtnCurrency), "Value added", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(findViewById(R.id.BtnCurrency), "Error adding value", Snackbar.LENGTH_SHORT).show()
        }
    }

}
