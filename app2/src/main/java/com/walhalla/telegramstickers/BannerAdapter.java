//package com.walhalla.telegramstickers;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//
//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
//import com.walhalla.ui.Module_U;
//
//public class BannerAdapter {
//
//    private static final String TAG = "@@@";
//    private static final String KEY_DISM_COUNT = "key_dism";
//
//    private final String BANNER_ID;
//    private final Context context;
//    private final String URLTelegram;
//    private final SharedPreferences mSharedPreferences;
//    private final long dism;
//
//    private InterstitialAd mInterstitialAd;
//    boolean onlyOnce = false;
//
//    public BannerAdapter(Context context) {
//        this.context = context;
//        this.BANNER_ID = context.getString(R.string.i1);
//        this.URLTelegram = context.getString(R.string.url_telegram);
//
//
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        dism = mSharedPreferences.getLong(KEY_DISM_COUNT, 0);
//    }
//
//    private final FullScreenContentCallback callback = new FullScreenContentCallback() {
//        @Override
//        public void onAdClicked() {
//            // Called when a click is recorded for an ad.
//            Log.d(TAG, "Ad was clicked.");
//        }
//
//
//        // Called when ad is dismissed. {Вызывается при закрытии объявления.}
//        //Вернулись в приложение после просмотра рекламы
//        @Override
//        public void onAdDismissedFullScreenContent() {
//            //@mSharedPreferences.edit().putLong(KEY_DISM_COUNT, dism).apply();
//
//
//            // Set the ad reference to null so you don't show the ad a second time.
//            Log.d(TAG, "Ad dismissed fullscreen content.");
//            mInterstitialAd = null;
//            loadNewBanner(context);
//        }
//
//        @Override
//        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//            // Called when ad fails to show.
//            Log.e(TAG, "Ad failed to show fullscreen content.");
//            mInterstitialAd = null;
//        }
//
//        @Override
//        public void onAdImpression() {
//            // Called when an impression is recorded for an ad.
//            Log.d(TAG, "Ad recorded an impression.");
//        }
//
//        @Override
//        public void onAdShowedFullScreenContent() {
//            // Called when ad is shown.
//            Log.d(TAG, "Ad showed fullscreen content.");
//        }
//    };
//
//
//    public void loadNewBanner(Context context) {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(context, BANNER_ID,
//                adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        mInterstitialAd = interstitialAd;
//                        mInterstitialAd.setFullScreenContentCallback(callback);
//                        Log.i(TAG, "onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        //loadAdError.getCode() - 3 error_code_no_fill
//                        Log.d(TAG, "" + loadAdError.getCode());//3
//                        Log.d(TAG, "" + loadAdError.getMessage());//No ad config.
//                        mInterstitialAd = null;
//
//                    }
//                });
//    }
//
//    public void showBanner(Activity mainActivity, String link) {
//
//        if (
//            //Math.random() * 100.0d >= 100.0d ||
//                mInterstitialAd == null || this.onlyOnce) {
//            Log.d(TAG, "The interstitial ad wasn't ready yet.");
//            //boolean success = launch(mainActivity, URLTelegram + link);
//        } else {
//            mInterstitialAd.show(mainActivity);
//            onlyOnce = true;
//        }
//
////        mInterstitialAd.setAdListener(new AdListener() {
////
////            @Override
////            public void onAdClosed() {
////                launch();
////            }
////        });
//    }
//
//}
