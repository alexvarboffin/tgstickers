package com.walhalla.stickers.utils

import com.walhalla.stickers.compat.ComV19



object Utils {
    //private InterstitialAd interstitialAd;
    fun isSameDomain(url: String, url1: String): Boolean {
        return getRootDomainUrl(url.lowercase(java.util.Locale.getDefault())) == com.walhalla.stickers.utils.Utils.getRootDomainUrl(
            url1.lowercase(java.util.Locale.getDefault())
        )
    }

    private fun getRootDomainUrl(url: String): String {
        val domainKeys: kotlin.Array<kotlin.String?> =
            url.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[2].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val length = domainKeys.size
        val dummy = if (domainKeys[0] == "www") 1 else 0
        if (length - dummy == 2) return domainKeys[length - 2] + "." + domainKeys[length - 1]
        else {
            if (domainKeys[length - 1]!!.length == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1]
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1]
            }
        }
    }

    fun tintMenuIcon(
        context: android.content.Context,
        item: android.view.MenuItem,
        color: kotlin.Int
    ) {
        val drawable = item.icon
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate()
            drawable.setColorFilter(
                androidx.core.content.ContextCompat.getColor(context, color),
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    //    public static void bookmarkUrl(Context context, String url) {
    //        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0); // 0 - for private mode
    //        SharedPreferences.Editor editor = pref.edit();
    //
    //        // if url is already bookmarked, unbookmark it
    //        if (pref.getBoolean(url, false)) {
    //            editor.remove(url).apply();
    //        } else {
    //            editor.putBoolean(url, true);
    //        }
    //        editor.commit();
    //    }
    //    public static boolean isBookmarked(Context context, String url) {
    //        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0);
    //        return pref.getBoolean(url, false);
    //    }
//    fun ShowErrorToast0(context: android.content.Context?, err: kotlin.Int) {
//        val comv19: ComV19 = ComV19()
//        es.dmoral.toasty.Toasty.custom(
//            context,
//            err,
//            comv19.getDrawable(context, com.walhalla.stickers.R.drawable.ic_cancel),
//            com.walhalla.stickers.R.color.error,
//            android.R.color.white, Toasty.LENGTH_SHORT, true, true
//        ).show()
//    }

    @JvmStatic
    fun ShowToast0(context: android.content.Context, str: kotlin.String?) {
//        val comv19: ComV19 = ComV19()
//        Toasty.custom(
//            context, str,
//            comv19.getDrawable(context, com.walhalla.stickers.R.drawable.ic_info),
//            androidx.core.content.ContextCompat.getColor(
//                context,
//                com.walhalla.stickers.R.color.colorPrimaryDark
//            ),
//            androidx.core.content.ContextCompat.getColor(context, android.R.color.white),
//            Toasty.LENGTH_SHORT, true, true
//        ).show()
    }

    fun ShowToast0(context: android.content.Context?, res: kotlin.Int) {
//        val comv19: ComV19 = ComV19()
//        Toasty.custom(
//            context, res,
//            comv19.getDrawable(context, com.walhalla.stickers.R.drawable.ic_info),
//            com.walhalla.stickers.R.color.colorPrimaryDark,
//            android.R.color.white,
//            Toasty.LENGTH_SHORT, true, true
//        ).show()
    }

    fun isValidUrl(input: kotlin.CharSequence?): kotlin.Boolean {
        if (android.text.TextUtils.isEmpty(input)) {
            return false
        }
        val URL_PATTERN = android.util.Patterns.WEB_URL
        var matches = URL_PATTERN.matcher(input).matches()
        if (!matches) {
            val urlString = input.toString() + ""
            if (android.webkit.URLUtil.isNetworkUrl(urlString)) {
                try {
                    java.net.URL(urlString)
                    matches = true
                } catch (e: java.lang.Exception) {
                    com.walhalla.ui.DLog.handleException(e)
                }
            }
        }
        return matches
    }


    //   public static void GetSessionID(final Context cntx){
    //       final String[] ID = new String[1];
    //
    //       AsyncTask.execute(new Runnable() {
    //           @Override
    //           public void run() {
    //
    //               try {
    //                   Document doc = Jsoup.connect(API_URL2).post();
    //
    //                   Elements scriptElements = doc.getElementsByTag("script");
    //                   for (Element element : scriptElements) {
    //                       if (element.data().contains("sid")) {
    //                            // find the line which contains 'infosite.token = <...>;'
    //                           Pattern pattern = Pattern.compile("(?is)sid=\'(.+?)\'");
    //                           Matcher matcher = pattern.matcher(element.data());
    //                           // we only expect a single match here so there's no need to loop through the matcher's groups
    //                           if (matcher.find()) {
    //                               //System.out.println(matcher.group());
    //                               //System.out.println(matcher.group(1));
    //                               ID[0] = matcher.group(1).toString();
    //                           } else {
    //                               System.err.println("No match found!");
    //                           }
    //                           break;
    //                       }
    //                   }
    //               } catch (IOException e) {
    //                   DLog.handleException(e);
    //               }
    //
    //
    //               Session session;
    //               session = new Session(cntx);
    //               session.setSid(ID[0]);
    //                   }
    //
    //
    //       });
    //
    //
    //
    //
    //    //    return ID[0];
    //   }
    fun getRootDirPath(context: android.content.Context): kotlin.String {
        if (android.os.Environment.MEDIA_MOUNTED == android.os.Environment.getExternalStorageState()) {
            val file = androidx.core.content.ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            return file.absolutePath
        } else {
            return context.applicationContext.filesDir.absolutePath
        }
    }

    fun getProgressDisplayLine(currentBytes: kotlin.Long, totalBytes: kotlin.Long): kotlin.String {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(
            totalBytes
        )
    }

    private fun getBytesToMBString(bytes: kotlin.Long): kotlin.String {
        return String.format(java.util.Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }
}
