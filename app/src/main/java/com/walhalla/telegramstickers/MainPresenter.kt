package com.walhalla.telegramstickers;

import android.content.Context;

import android.os.Handler;

import androidx.appcompat.app.AlertDialog;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.stickers.constants.Constants;
import com.walhalla.stickers.FirebaseKeys;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.network.JSONParser;
import com.walhalla.stickers.presenter.StickersPresenter;
import com.walhalla.telegramstickers.activity.main.MainActivity;

import com.walhalla.telegramstickers.utils.NetworkUtils;
import com.walhalla.ui.DLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainPresenter extends StickersPresenter {
    

    // https://tgstickers-dbce8.web.app/aaa.json
    public static char[] v0 = new char[]{110, 111, 115, 106, 46, 97, 97, 97, 47, 112, 112, 97, 46, 98, 101, 119, 46, 56, 101, 99, 98, 100, 45, 115, 114, 101, 107, 99, 105, 116, 115, 103, 116, 47, 47, 58, 115, 112, 116, 116, 104};

    private String dec0(char[] v) {
        return new StringBuilder((String.valueOf(v))).reverse().toString();
    }
    public static final String PREF_NAME = "Settings";

    private final Context context;

    public final String url_all_stickers;
    private final MainActivity mView;
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler;


    public MainPresenter(Context context, Handler handler, MainActivity activity) {
        super(context);
        this.context = context;
        this.url_all_stickers = dec0(v0);
        this.mView = activity;
        this.handler = handler;
    }

    public boolean isRunning() {
        return !executor.isShutdown();// Проверяем, работает ли ExecutorService
    }

    public void stop() {
        executor.shutdown();// Останавливаем ExecutorService
    }

    public void execute() {
        if (!isRunning()) {

        }
        DLog.d("@@@@" + isRunning());

        executor.execute(() -> {
            int totalStickersLength = 0;
            List<StickerDb> stickersArray = new ArrayList<>();
            JSONParser jParser = new JSONParser();
            JSONObject makeHttpRequest = jParser.makeHttpRequest(url_all_stickers, "GET", new HashMap<>());
            if (makeHttpRequest == null) {
                DLog.d("All Stickers: @@@@@@");
                onPostExecute(stickersArray, totalStickersLength);
                return;
            }

            try {
                if (isValid(makeHttpRequest)) {
                    JSONArray stickers = makeHttpRequest.getJSONArray("stickers");
                    totalStickersLength = stickers.length();
                    for (int i = 0; i < totalStickersLength; i++) {
                        JSONObject object = stickers.getJSONObject(i);

                        String author = "";
                        String imageset = "";
                        String link = "";
                        String created_at = "";
                        try {
                            author = object.getString("author");
                        } catch (Exception ignored) {
                        }

                        try {
                            link = object.getString("link");
                        } catch (Exception ignored) {
                        }

//                        String count = "";
//                        if (object.has(Constants.KEY_NUMIMAGES)) {
//                            count = object.getString(Constants.KEY_NUMIMAGES);
//                        }
                        int count = 0;
                        if (object.has(Constants.KEY_NUMIMAGES)) {
                            count = object.getInt(Constants.KEY_NUMIMAGES);
                        }

                        String categoryName = "";
                        String tmp = dec0(FirebaseKeys.KEY_CATEGORY);
                        if (object.has(tmp)) {
                            categoryName = object.getString(tmp).toLowerCase();
                        }
                        try {
                            if (object.has(Constants.KEY_IMAGESET)) {
                                imageset = object.getString(Constants.KEY_IMAGESET);
                            } else {
                                imageset = link;
                            }
                        } catch (Exception ignored) {
                            imageset = link;
                        }
                        try {
                            created_at = object.getString(Constants.TAG_CREATED_AT);
                        } catch (Exception ignored) {
                        }
                        stickersArray.add(
                                new StickerDb(
                                        i + 1,
                                        //object.getInt(Constants.TAG_PID),
                                        object.getString("name"),
                                        author,
                                        imageset,
                                        count,
                                        categoryName,
                                        link,
                                        created_at)
                        );
                    }
                }
            } catch (Exception e) {
                DLog.handleException(e);
            }
            onPostExecute(stickersArray, totalStickersLength);
        });
    }

    private void onPostExecute(final List<StickerDb> arrayList, int totalStickersLength) {
        sharedPreferences.edit().putLong(KEY_DATE, (System.currentTimeMillis() / 1000) + (ONE_HOUR)).apply();
        if (mView != null) {
            handler.post(() -> {
                if (!arrayList.isEmpty()) {
                    AppDatabase db = LocalDatabaseRepo.getDatabase(context);
                    db.stickerDao().deleteAllStickers();
                    final int total = arrayList.size();
                    for (int i = 0; i < total; i++) {
                        StickerDb item = arrayList.get(i);
                        //if (!db.checkSticker(item.name)) {
                        db.stickerDao().insertSticker(item);
                        //}
                    }
                    //db--close();
                    //DLog.d("[@@ isValidData @@]" + (total == totalStickersLength));
                    if (total == totalStickersLength && totalStickersLength > 0) {
                        sharedPreferences.edit().putBoolean(KEY_VALID_DATA, true).apply();
                    }
                }
                mView.successResult00(arrayList);
            });
        }
    }


    private boolean isValid(JSONObject makeHttpRequest) throws Exception {
        //return makeHttpRequest.getInt("success") == 1;
        return true;
    }

    public void onCreateEvent() {
        AppDatabase db = LocalDatabaseRepo.getDatabase(context);
        List<StickerDb> tmp = db.stickerDao().getAllStickers();
        if (isDelayTimeout() || (tmp != null && tmp.isEmpty())) {
            //db--close();
            if (NetworkUtils.isNetworkConnected(context)||1==1) {
                mView.refreshDB();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.no_internet));
                builder.show();
            }
        } else {
            //db--close();
        }
    }

    public String dec0(int[] intArray) {
        char[] strArray = new char[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            strArray[i] = (char) intArray[i];
        }
        return new StringBuilder((String.valueOf(strArray))).reverse().toString();
    }
}