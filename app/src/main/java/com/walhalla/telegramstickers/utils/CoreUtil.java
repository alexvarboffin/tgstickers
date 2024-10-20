package com.walhalla.telegramstickers.utils;

import static com.walhalla.abcsharedlib.Share.KEY_FILE_PROVIDER;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.walhalla.ui.DLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CoreUtil {

    public static File makeLocalExportFileName(Context activity, String fileNamePrefix) {
        //File mm = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileNamePrefix + System.currentTimeMillis() + ".png");
        //File mm = new File(activity.getExternalCacheDir(), fileNamePrefix + System.currentTimeMillis() + ".png");
        File mm = new File(activity.getCacheDir(), fileNamePrefix + System.currentTimeMillis() + ".png");
        //File mm = new File("/data/local/tmp/external/", fileNamePrefix + System.currentTimeMillis() + ".png");
        File m = mm.getParentFile();
        if (m != null && !m.exists()) {
            boolean r = m.mkdir();
            //DLog.d("@@" + r);
        }
//        boolean w = mm.canWrite();
//        DLog.d("@canWrite@" + w);
        return mm;
    }

    public static Uri getLocalBitmapUri(Context context, File file, Bitmap bitmap) {
        //boolean w = file.canWrite();
        //DLog.d("<@@@>" + file.getAbsolutePath() + " " + "@canWrite@" + w);
        String APPLICATION_ID = context.getPackageName();
        Uri bmpUri = null;
        try {
            //boolean w0 = file.createNewFile();
            //DLog.d("@createNewFile@" + w0);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(context, APPLICATION_ID + KEY_FILE_PROVIDER, file);
        } catch (FileNotFoundException e) {
            DLog.handleException(e);
        } catch (IOException e) {
            DLog.handleException(e);
        } catch (SecurityException e) {
            DLog.handleException(e);
        }
        return bmpUri;
    }
}
