package com.walhalla.stickers.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.walhalla.abcsharedlib.Share
import com.walhalla.ui.DLog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object CoreUtil {
    fun makeLocalExportFileName(activity: Context, fileNamePrefix: String?): File {
        //File mm = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileNamePrefix + System.currentTimeMillis() + ".png");
        //File mm = new File(activity.getExternalCacheDir(), fileNamePrefix + System.currentTimeMillis() + ".png");
        val mm = File(activity.cacheDir, fileNamePrefix + System.currentTimeMillis() + ".png")
        //File mm = new File("/data/local/tmp/external/", fileNamePrefix + System.currentTimeMillis() + ".png");
        val m = mm.getParentFile()
        if (m != null && !m.exists()) {
            val r = m.mkdir()
            //DLog.d("@@" + r);
        }
//        boolean w = mm.canWrite();
//        DLog.d("@canWrite@" + w);
        return mm
    }

    fun getLocalBitmapUri(context: Context, file: File, bitmap: Bitmap): Uri? {
        //boolean w = file.canWrite();
        //DLog.d("<@@@>" + file.getAbsolutePath() + " " + "@canWrite@" + w);
        val APPLICATION_ID = context.packageName
        var bmpUri: Uri? = null
        try {
            //boolean w0 = file.createNewFile();
            //DLog.d("@createNewFile@" + w0);
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(context, APPLICATION_ID + Share.KEY_FILE_PROVIDER, file)
        } catch (e: FileNotFoundException) {
            DLog.handleException(e)
        } catch (e: IOException) {
            DLog.handleException(e)
        } catch (e: SecurityException) {
            DLog.handleException(e)
        }
        return bmpUri
    }
}