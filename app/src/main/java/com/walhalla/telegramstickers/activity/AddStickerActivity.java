package com.walhalla.telegramstickers.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.walhalla.stickers.AddNewSticker;
import com.walhalla.stickers.utils.TelegramUtils;
import com.walhalla.telegramstickers.databinding.StickerAddBinding;
import com.walhalla.stickers.utils.NetworkUtils;
import com.walhalla.telegramstickers.R;

public class AddStickerActivity extends Activity implements AddNewSticker.AddNewStickerCallback {


    private String[] arraySpinner;


    private ProgressDialog pDialog;
    private StickerAddBinding binding;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = StickerAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textView.setText(TelegramUtils.ADDSTICKERS);
        binding.btnAddSticker.setOnClickListener(view -> {
            if (!binding.inputTitle.getText().toString().matches("")
                    && !binding.inputLink.getText().toString().matches("")
                    && NetworkUtils.isNetworkConnected(this)) {

                onPreExecute(this);

                String obj = binding.inputTitle.getText().toString();
                String obj2 = binding.inputAuthor.getText().toString();
                String lowerCase = binding.inputCategory.getSelectedItem().toString().toLowerCase();
                String obj3 = binding.inputLink.getText().toString();
                new AddNewSticker(this).execute(obj, obj2, obj3, lowerCase);
            }
        });
    }


    public void onPreExecute(Context context) {
        this.pDialog = new ProgressDialog(context);
        this.pDialog.setMessage(context.getString(R.string.sticker_sending));
        this.pDialog.setIndeterminate(false);
        this.pDialog.setCancelable(false);
        this.pDialog.show();
    }


    @Override
    public void setResultRequest(int result) {
        this.setResult(result, new Intent());
        this.finish();
    }

    @Override
    public void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}

