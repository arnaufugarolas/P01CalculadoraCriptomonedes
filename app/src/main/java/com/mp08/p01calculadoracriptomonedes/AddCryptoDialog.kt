package com.mp08.p01calculadoracriptomonedes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class AddCryptoDialog: DialogFragment() {

    private var mCallback: AddCryptoInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.dialog_add_crypto, container, false)
        val btnOk: Button = view.findViewById(R.id.btnAdd2)
        val btnCancel: Button = view.findViewById(R.id.btnCancel2)

        btnOk.setOnClickListener{
            val name: String = view.findViewById<TextView>(R.id.ETName).text.toString()
            val value: String = view.findViewById<TextView>(R.id.ETValue).text.toString()

            mCallback?.addCryptoGetData(name, value);
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
        val height = (resources.displayMetrics.heightPixels * 0.45).toInt()

        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = activity as AddCryptoInterface
        } catch (e: ClassCastException) {
            Log.e("Tag", "onAttach: ClassCastException: " + e.message)
        }
    }

}