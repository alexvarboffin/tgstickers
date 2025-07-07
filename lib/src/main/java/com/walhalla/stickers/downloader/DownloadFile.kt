package com.walhalla.stickers.downloader;


import static com.walhalla.stickers.presenter.StickerInfoPresenter.PermissionResolver.checkExternalStoragePermission;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.walhalla.stickers.utils.IOUtils;
import com.walhalla.stickers.utils.Utils;
import com.walhalla.ui.DLog;

import java.util.HashSet;


public class DownloadFile {

    private static DownloadFile instance;
    private final HashSet<Long> longHashSet = new HashSet<>();


    private DownloadFile() {
    }

    public static DownloadFile newInstance() {
        if (instance == null) {
            instance = new DownloadFile();
        }
        return instance;
    }



    public void makeImageLoad(Context context, final String url, String stickerPackName) {
        if (IOUtils.hasMarsallow()) {
            if (checkExternalStoragePermission(context)) {
            } else {
                DLog.d("__________________________________");
                return;
            }
        }

        try {
            String fileName = getFileNameFromUrl(url);
            String fileExt = getFileExtensionFromUrl(url);
            String fullFileName = makeFileName(fileName, fileExt);
            DLog.d("[+][+] DownloadFileName: " + fullFileName + "\t\t" + url);


            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(fileName);
            request.setDescription("Downloading");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //request.setDestinationUri(Uri.fromFile(new File(mBaseFolderPath)));
            //ERROR >> request.setDestinationInExternalPublicDir(dir, fullFileName);
            //ERROR >> request.setDestinationInExternalPublicDir(mBaseFolderPath, fullFileName);
            //request.setDestinationInExternalFilesDir(context, mBaseFolderPath, fullFileName);
            //new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
   //@         File file = new File(Config.pictureFolder(context), fullFileName);
   //@         Uri uri = Uri.fromFile(file);
            //Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            //Work in old --> request.setDestinationUri(uri);

            //aka Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            // Config.videoFolder(context).getAbsolutePath()
            //request.setDestinationUri(uri);

            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fullFileName);
            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,"Stickers");

            //work
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fullFileName);


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                request.allowScanningByMediaScanner();
                request.setVisibleInDownloadsUi(true);
            }

            if (manager != null) {
                longHashSet.add(manager.enqueue(request));
            }


            //DLog.d("[+] " + dir + " :: " + mBaseFolderPath);
            Utils.ShowToast0(context, "Downloading Start!");
        } catch (Exception e) {
            DLog.handleException(e);
        }
    }

    private static String getFileExtensionFromUrl(String url) {
        int lastDotIndex = url.lastIndexOf(".");
        return url.substring(lastDotIndex + 1);
    }

    private static String getFileNameFromUrl(String url) {
        int lastSlashIndex = url.lastIndexOf("/");
        return url.substring(lastSlashIndex + 1);
    }

    private String makeFileName(String fileName, String fileExt) {
//        String fullFileName = fileName.trim();
//        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
//        fullFileName = fullFileName.replaceAll(characterFilter, "");
//        fullFileName = fullFileName.replaceAll("['+.^:,#\"]", "");
//        fullFileName = fullFileName.replace(" ", "_")
//                .replace("!", "")
//                .replace("@", "_")
//                .replace(":", "") + fileExt;
//
//
//        //Remove Emoji
//        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
//        fullFileName = fullFileName.replaceAll(regex, "");
//
//        if (fullFileName.length() > 100) {
//            fullFileName = fullFileName.substring(0, 100) + fileExt;
//        } else {
//            fullFileName = fullFileName + fileExt;
//        }
//        DLog.d(fileName);
//        return fileName;
        return System.currentTimeMillis() + "." + fileExt;
    }
}
