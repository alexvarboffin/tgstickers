package com.walhalla.stickers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.walhalla.stickers.network.JSONParser;
import com.walhalla.ui.DLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddNewSticker {

    private static final String TAG_SUCCESS = "success";

    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LINK = "link";
    private final AddNewStickerCallback callback;

    public interface AddNewStickerCallback {

        void setResultRequest(int resultCanceled);

        void hideDialog();
    }


    private final Executor executor = Executors.newSingleThreadExecutor();
    JSONParser jsonParser = new JSONParser();

    public AddNewSticker(AddNewStickerCallback callback) {
        this.callback = callback;
    }


    public void execute(String obj, String obj2, String obj3, String lowerCase) {
        try {
            executor.execute(() -> {
                HashMap<Object, Object> arrayList = new HashMap<>();
                arrayList.put("name", obj);
                arrayList.put(KEY_AUTHOR, obj2);
                arrayList.put(KEY_CATEGORY, lowerCase);
                arrayList.put(KEY_LINK, obj3);

                String url_create_sticker = "";//getString(R.string.stickers_url_add_sticker);
                JSONObject makeHttpRequest = this.jsonParser.makeHttpRequest(url_create_sticker, "POST", arrayList);

                // Обработка результата в UI потоке с использованием Handler
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (makeHttpRequest == null) {
                        if (callback != null) {
                            callback.setResultRequest(Activity.RESULT_CANCELED);
                        }
                        return;
                    }
                    Log.d("Create Response", makeHttpRequest.toString());
                    try {
                        if (makeHttpRequest.getInt(TAG_SUCCESS) == 1) {
                            if (callback != null) {
                                callback.hideDialog();
                                callback.setResultRequest(Activity.RESULT_OK);
                            }
                        } else {
                            if (callback != null) {
                                callback.hideDialog();
                                callback.setResultRequest(Activity.RESULT_CANCELED);
                            }
                        }
                    } catch (JSONException e) {
                        DLog.handleException(e);
                        callback.hideDialog();
                    }
                });
            });
        } catch (Exception e) {
            DLog.handleException(e);
            callback.hideDialog();
        }
    }

}