package com.walhalla.stickers.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.webkit.URLUtil;

import androidx.core.content.ContextCompat;


import com.walhalla.stickers.R;
import com.walhalla.stickers.compat.ComV19;
import com.walhalla.ui.DLog;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Utils {
    //private InterstitialAd interstitialAd;

    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
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

    public static void ShowErrorToast0(Context context, int err) {
        ComV19 comv19 = new ComV19();
        Toasty.custom(context,
                err,
                comv19.getDrawable(context, R.drawable.ic_cancel),
                R.color.error,
                android.R.color.white, Toasty.LENGTH_SHORT, true, true).show();
    }

    public static void ShowToast0(Context context, String str) {
        ComV19 comv19 = new ComV19();
        Toasty.custom(context, str,
                comv19.getDrawable(context, R.drawable.ic_info),
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, android.R.color.white),
                Toasty.LENGTH_SHORT, true, true).show();
    }

    public static void ShowToast0(Context context, int res) {
        ComV19 comv19 = new ComV19();
        Toasty.custom(context, res,
                comv19.getDrawable(context, R.drawable.ic_info),
                R.color.colorPrimaryDark,
                android.R.color.white,
                Toasty.LENGTH_SHORT, true, true).show();
    }

    public static boolean isValidUrl(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean matches = URL_PATTERN.matcher(input).matches();
        if (!matches) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    matches = true;
                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        }
        return matches;
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

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }


}
