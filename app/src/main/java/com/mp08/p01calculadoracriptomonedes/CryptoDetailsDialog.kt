package com.mp08.p01calculadoracriptomonedes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CryptoDetailsDialog(private val currencyName: String, private val currencyValue: String) : DialogFragment() {

    private var mCallback: CryptoDetailsInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.dialog_crypto_details, container, false)
        val btnOk: Button = view.findViewById(R.id.ButtonSubmitDialog)
        val btnCancel: Button = view.findViewById(R.id.ButtonCancelDialog)
        val editTextCurrencyName: EditText = view.findViewById(R.id.ETNameDialog)
        val editTextCurrencyValue: EditText = view.findViewById(R.id.ETValueDialog)
        val textViewTitle: TextView = view.findViewById(R.id.TVTitleDialog)
        val createCurrency: Boolean = currencyName == "null"

        if (!createCurrency) {
            editTextCurrencyName.setText(currencyName)
            editTextCurrencyName.isEnabled = false
            textViewTitle.text = "Set the new rate for $currencyName"
            if (currencyValue != "null") {
                editTextCurrencyValue.setText(currencyValue)
            }
        } else {
            textViewTitle.text = "Add the details to create a new currency"
        }

        btnOk.setOnClickListener{
            var name: String = editTextCurrencyName.text.toString()
            var value: String = editTextCurrencyValue.text.toString()

            if (name.replace(" ", "") == "") {
                name = "null"
            }
            if (value.replace(" ", "") == "") {
                value = "null"
            }

            if (createCurrency) {
                mCallback?.createCrypto(name, value)
            } else {
                mCallback?.modifyCrypto(name, value)
            }

            this.dismiss()
        }

        btnCancel.setOnClickListener{
            this.dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()

        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = activity as CryptoDetailsInterface
        } catch (e: ClassCastException) {
            Log.e("Tag", "onAttach: ClassCastException: " + e.message)
        }
    }

}