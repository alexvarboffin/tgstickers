package com.walhalla.stickers.presenter;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

//import com.walhalla.permissionresolver.permission.PermissionResolver;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.walhalla.stickers.R;
import com.walhalla.stickers.downloader.DownloadFile;
import com.walhalla.ui.DLog;

public class StickerInfoPresenter {


    private final PermissionResolver resolver;
    private final Activity activity;


    public StickerInfoPresenter(Activity activity) {
        this.activity = activity;
        this.resolver = new PermissionResolver(this.activity);
    }


    public void x(Activity activity, ActivityResultLauncher<String> var0) {
        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
            String message = activity.getString(R.string.this_permission_is_needed);
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.alert_perm_title)
                    .setMessage(message + "\n" + perm)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            {
                                var0.launch(perm);//aaa();
                            }
                    )
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            var0.launch(perm);//aaa();
        }
    }

    public void saveImageRequest(String fileUrl, String link) {
        if (resolver.isNeedGrantPermission()) {
            DLog.d("# wait permission");
        } else {
            DownloadFile.newInstance().makeImageLoad(activity, fileUrl, link);
            //Tracker.log(getContext(), url);
        }
    }

    public static class PermissionResolver {
        private final Activity activity;

        public PermissionResolver(Activity activity) {
            this.activity = activity;
        }

        public static boolean checkExternalStoragePermission(Context context) {
            int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isNeedGrantPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Начиная с Android 10 (API 29) можно сохранять без WRITE_EXTERNAL_STORAGE
                return false;
            } else {
                // До Android 10 — требуется разрешение
                return !checkExternalStoragePermission(activity);
            }
        }
    }
}
