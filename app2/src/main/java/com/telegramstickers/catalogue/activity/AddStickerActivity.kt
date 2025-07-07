package com.telegramstickers.catalogue.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.telegramstickers.catalogue.R
import com.telegramstickers.catalogue.databinding.StickerAddBinding
import com.walhalla.stickers.AddNewSticker
import com.walhalla.stickers.AddNewSticker.AddNewStickerCallback
import com.walhalla.stickers.utils.NetworkUtils.isNetworkConnected
import com.walhalla.stickers.utils.TelegramUtils
import java.util.Locale

class AddStickerActivity : Activity(), AddNewStickerCallback {
    //private val arraySpinner: Array<String>

    private var pDialog: ProgressDialog? = null
    private var binding: StickerAddBinding? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = StickerAddBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        binding!!.textView.text = TelegramUtils.ADDSTICKERS
        binding!!.btnAddSticker.setOnClickListener(View.OnClickListener { view: View? ->
            if (!binding!!.inputTitle.getText().toString()
                    .matches("".toRegex()) && !binding!!.inputLink.getText().toString()
                    .matches("".toRegex()) && isNetworkConnected(this)
            ) {
                onPreExecute(this)

                val obj = binding!!.inputTitle.getText().toString()
                val obj2 = binding!!.inputAuthor.getText().toString()
                val lowerCase = binding!!.inputCategory.getSelectedItem().toString()
                    .lowercase(Locale.getDefault())
                val obj3 = binding!!.inputLink.getText().toString()
                AddNewSticker(this).execute(obj, obj2, obj3, lowerCase)
            }
        })
    }


    fun onPreExecute(context: Context) {
        this.pDialog = ProgressDialog(context)
        this.pDialog!!.setMessage(context.getString(R.string.sticker_sending))
        this.pDialog!!.setIndeterminate(false)
        this.pDialog!!.setCancelable(false)
        this.pDialog!!.show()
    }


    override fun setResultRequest(result: Int) {
        this.setResult(result, Intent())
        this.finish()
    }

    override fun hideDialog() {
        if (pDialog != null && pDialog!!.isShowing()) {
            pDialog!!.dismiss()
        }
    }
}
