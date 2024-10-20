package com.walhalla.stickers.presenter;


import android.app.Activity;

import com.walhalla.permissionresolver.permission.PermissionResolver;
import com.walhalla.stickers.downloader.DownloadFile;
import com.walhalla.ui.DLog;

public class StickerInfoPresenter {


    private final PermissionResolver resolver;
    private final Activity activity;


    public StickerInfoPresenter(Activity activity) {
        this.activity = activity;
        this.resolver = new PermissionResolver(this.activity);
    }

    public void saveImageRequest(String fileUrl, String link) {
        if (resolver.isNeedGrantPermission()) {
            DLog.d("# wait permission");
        } else {
            DownloadFile.newInstance().makeImageLoad(activity, fileUrl, link);
            //Tracker.log(getContext(), url);
        }
    }
}
