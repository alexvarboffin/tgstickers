package com.walhalla.stickers.utils;

import android.annotation.SuppressLint;
import android.os.Build;

@SuppressLint("ObsoleteSdkInt")
public class IOUtils {

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
    public static boolean hasLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static boolean hasMarsallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
