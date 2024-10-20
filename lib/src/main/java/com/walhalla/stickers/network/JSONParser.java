package com.walhalla.stickers.network;

import com.walhalla.ui.DLog;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONParser {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    static JSONObject jObj = null;

    public JSONObject makeHttpRequest(String url, String str2, HashMap<Object, Object> list) {
        DLog.d("{URL}" + url);
        try {
            if ("POST".equals(str2)) {


                FormBody.Builder builder = new FormBody.Builder();
                for (Map.Entry<Object, Object> entry : list.entrySet()) {
                    builder.add((String) entry.getKey(), String.valueOf(entry.getValue()));
                }
                Request request = new Request.Builder()
                        .url(url)
                        .post(builder.build())
                        .build();
                Response response = client.newCall(request).execute();
                return new JSONObject(response.body().string());
            } else if ("GET".equals(str2)) {

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                //String format = URLEncodedUtils.format(list, "utf-8");
                Response response = client.newCall(request).execute();
                return new JSONObject(response.body().string());
            }

        } catch (Exception e) {
            DLog.handleException(e);
        }
        return jObj;
    }
}
