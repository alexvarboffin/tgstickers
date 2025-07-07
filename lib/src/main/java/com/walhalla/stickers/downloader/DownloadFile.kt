package com.walhalla.stickers.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.walhalla.stickers.presenter.PermissionResolver.Companion.checkExternalStoragePermission
import com.walhalla.stickers.utils.IOUtils.hasMarsallow
import com.walhalla.stickers.utils.Utils.ShowToast0
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException


class DownloadFile private constructor() {
    private val longHashSet = HashSet<Long?>()


    fun makeImageLoad(context: Context, url: String, stickerPackName: String?) {
        if (hasMarsallow()) {
            if (checkExternalStoragePermission(context)) {
            } else {
                d("__________________________________")
                return
            }
        }

        try {
            val fileName: String = getFileNameFromUrl(url)
            val fileExt: String = getFileExtensionFromUrl(url)
            val fullFileName = makeFileName(fileName, fileExt)
            println("[+][+] DownloadFileName: " + fullFileName + "\t\t" + url)


            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(fileName)
            request.setDescription("Downloading")


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner()
            }

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

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
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fullFileName)


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                request.allowScanningByMediaScanner()
                request.setVisibleInDownloadsUi(true)
            }

            if (manager != null) {
                longHashSet.add(manager.enqueue(request))
            }


            //DLog.d("[+] " + dir + " :: " + mBaseFolderPath);
            ShowToast0(context, "Downloading Start!")
        } catch (e: Exception) {
            println("@@@ $e")
            handleException(e)
        }
    }

    private fun makeFileName(fileName: String?, fileExt: String?): String {
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
        return System.currentTimeMillis().toString() + "." + fileExt
    }

    companion object {
        private var instance: DownloadFile? = null
        fun newInstance(): DownloadFile {
            if (instance == null) {
                instance = DownloadFile()
            }
            return instance!!
        }


        private fun getFileExtensionFromUrl(url: String): String {
            val lastDotIndex = url.lastIndexOf(".")
            return url.substring(lastDotIndex + 1)
        }

        private fun getFileNameFromUrl(url: String): String {
            val lastSlashIndex = url.lastIndexOf("/")
            return url.substring(lastSlashIndex + 1)
        }
    }
}
