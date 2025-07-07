package com.walhalla.stickers.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import com.walhalla.abcsharedlib.Share
import com.walhalla.stickers.R
import com.walhalla.stickers.utils.CoreUtil


fun shareImage1(context: Context, resource: String?, parent: ImageView, fileNamePrefix: String) {

    //showWatermark(tv_quotes_watermark, tools);
    val bitmap = createBitmap(parent.width, parent.height)
    val canvas = Canvas(bitmap)
    parent.draw(canvas)

    val file = CoreUtil.makeLocalExportFileName(context, fileNamePrefix)
    val uri = CoreUtil.getLocalBitmapUri(context, file, bitmap)

    //hideWatermark(tv_quotes_watermark, tools);
    val appName = context.getString(R.string.app_name)

    //val extra = "hello"
    val extra = appName + ",  " + resource
//        if (QTextUtils.isAuthorNotEmpty(status.getAuthor())) {
//            extra = extra + "\n" + "— " + status.getAuthor() + "\n" + appName;
//        }
    val intent = Share.makeImageShare(extra)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.putExtra(Intent.EXTRA_SUBJECT, appName)

    //intent.putExtra(Intent.EXTRA_TITLE, appName);

    //BugFix
    //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider
    val chooser = Intent.createChooser(intent, appName)
    val resInfoList = context.getPackageManager()
        .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        context.grantUriPermission(
            packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    context.startActivity(chooser)
    //Toast.makeText(getActivity(), share_as_image, Toast.LENGTH_SHORT).show();
}