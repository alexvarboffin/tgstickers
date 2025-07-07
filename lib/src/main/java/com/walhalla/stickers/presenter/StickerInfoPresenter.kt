package com.walhalla.stickers.presenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.walhalla.stickers.R
import com.walhalla.stickers.downloader.DownloadFile
import com.walhalla.ui.DLog.d



class StickerInfoPresenter(private val activity: Activity, private val var0: ActivityResultLauncher<String?>?) {
    private val resolver: PermissionResolver


    init {
        this.resolver = PermissionResolver(this.activity)
    }


    fun x(activity: Activity) {
        val perm = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
            val message = activity.getString(R.string.this_permission_is_needed)
            AlertDialog.Builder(activity)
                .setTitle(R.string.alert_perm_title)
                .setMessage(message + "\n" + perm)
                .setPositiveButton(
                    android.R.string.ok,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                        var0?.launch(perm) //aaa();
                    }
                )
                .setNegativeButton(
                    android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int -> dialog!!.dismiss() })
                .create().show()
        } else {
            var0?.launch(perm) //aaa();
        }
    }

    fun saveImageRequest(fileUrl: String, link: String?) {
        if (resolver.isNeedGrantPermission) {
            x(activity)
        } else {
            DownloadFile.newInstance().makeImageLoad(activity, fileUrl, link)
            //Tracker.log(getContext(), url);
        }
    }
}

class PermissionResolver(private val activity: Activity) {
    val isNeedGrantPermission: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Начиная с Android 10 (API 29) можно сохранять без WRITE_EXTERNAL_STORAGE
                return false
            } else {
                // До Android 10 — требуется разрешение
                return !checkExternalStoragePermission(activity)
            }
        }

    companion object {
        @JvmStatic
        fun checkExternalStoragePermission(context: Context): Boolean {
            val result = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                return false
            }
        }
    }
}