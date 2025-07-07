package com.walhalla.stickers.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        //return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).getActiveNetworkInfo() != null
        return true
    }
}