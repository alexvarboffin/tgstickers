package com.walhalla.stickers.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import com.walhalla.stickers.R;
import com.walhalla.ui.plugins.Launcher;


public class TelegramUtils {


    public static String ADDSTICKERS = "tg://addstickers?set=";
    public static final String ADDSTICKERS_WEB = "https://t.me/addstickers/";


    public static boolean launchTelegram(Activity activity, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            return true;
        } catch (Exception unused) {
            showTelegramAdvert(activity, R.string.telegramnotfound_title);
        }
        return false;
    }

    public static void showTelegramAdvert(Activity activity, int title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.telegramnotfound_message);
        builder.setPositiveButton(R.string.INSTALL, (dialogInterface, i) -> {
            Launcher.openMarketApp(activity, "org.telegram.messenger");
        });
        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }
}
