package com.walhalla.stickers.downloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import com.walhalla.ui.DLog;

import java.io.File;

public class Config {

    public static final String PREF_APPNAME = "pref_ttk_loader";
    private static final File KEY_DEFAULT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private static final String DOWNLOAD_DIRECTORY = "Stickers";

    public static File externalMemory() {
        File file = Environment.getExternalStorageDirectory();
        DLog.d("EXTERNAL_MEMORY: " + file.getAbsolutePath() + " " + file.isDirectory() + " " + file.canWrite());
        return file;
    }

    public static File pictureFolder(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_APPNAME, Context.MODE_PRIVATE);
        String tmp = preferences.getString("path", KEY_DEFAULT_PATH.getAbsolutePath());
        if (!KEY_DEFAULT_PATH.getAbsolutePath().equals(tmp)) {
            tmp = preferences.getString("path", KEY_DEFAULT_PATH.getAbsolutePath());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tmp = externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
            } else {
                tmp = externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
            }
        }
        File file = KEY_DEFAULT_PATH;

        //file = SharedObjects.externalMemory() + File.separator + DOWNLOAD_DIRECTORY;
        //file = Environment.DIRECTORY_MOVIES;

        String[] bits = file.getAbsolutePath().split("/");
//        for (String bit : bits) {
//            DLog.d("@@@ --> " + bit);
//        }

//        File tmp00 = new File(file);
//        if (!tmp00.exists()) {
//            boolean res = tmp00.mkdirs();
//            DLog.d("\uD83D\uDE0D CREATE FOLDER -> " + res + tmp00.getAbsolutePath());
//            ///storage/sdcard/TTDwn
//        }
        return file;
    }
}
