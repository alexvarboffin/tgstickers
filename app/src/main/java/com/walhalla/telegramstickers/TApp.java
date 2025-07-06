package com.walhalla.telegramstickers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository;
import com.walhalla.domain.repository.from_internet.AdvertConfig;
import com.walhalla.library.BuildConfig;
import com.walhalla.ui.DLog;
import com.walhalla.utils.AdmobAdsIds;
import com.walhalla.utils.RewardManager;
import com.walhalla.wads.AppOpenAdManager;
import com.walhalla.wads.OnShowAdCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class TApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static final boolean ACTIVITY_MOVES_TO_FOREGROUND_HANDLE = true;

    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    public static final int NB_VIDEOS_BEFORE_AD = 3;
    public static final int SECONDS_TO_COUNT_VIDEOS = 5;
    //private static final String ONESIGNAL_APP_ID = "8fcf9ed0-0064-49ee-9d27-aca9bb3bd972";

    private static TApp sInstance;
    private int intersticialCounter = 0;


    public boolean hasToDisplayIntersticiel() {
        return this.intersticialCounter >= 3;
    }

    public void incIntersticialCounter() {
        DLog.d("Counter " + this.intersticialCounter);
        this.intersticialCounter++;
    }

    public void resetIntersticialCounter() {
        this.intersticialCounter = 0;
    }

    public int getSecondsBeforeIntersticial() {
        return 5000;
    }


//    public static synchronized RunApp getInstance() {
//        RunApp aaaaa;
//        synchronized (RunApp.class) {
//            aaaaa = sInstance;
//        }
//        return aaaaa;
//    }


//rts
//    public void setConnectivityListener(NetworkStateReceiver.NetworkStateReceiverListener listener)
//    {
//        NetworkStateReceiver.networkStateReceiverListener = listener;
//    }

//Valley
//    private RequestQueue mRequestQueue;
//
//    public ImageLoader getmImageLoader() {
//        return mImageLoader;
//    }

//    private final ImageLoader mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
//
//        private final LruCache<String, Bitmap> cache = new LruCache<>(20);
//
//        @Override
//        public Bitmap getBitmap(String url) {
//            return this.cache.get(url);
//        }
//
//        @Override
//        public void putBitmap(String url, Bitmap bitmap) {
//            this.cache.put(url, bitmap);
//        }
//    });


//    public RequestQueue getRequestQueue() {
//        return mRequestQueue;
//    }


    public static AdvertAdmobRepository repository;

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //SharedPrefUtil.init(getApplicationContext());
        //DatabaseUtil.init(getApplicationContext());


        this.registerActivityLifecycleCallbacks(this);
        final List<String> list = new ArrayList<>();

        {
            list.add(AdRequest.DEVICE_ID_EMULATOR);
            list.add("A8A2F7804653E219880030864C1F32E4");
            list.add("5D5A89BC6372A49242D138B9AC352894");
            list.add("A2A86E2966898F258CB671EE038C2703");
            list.add("E20E841EDD87D721EE54510499AE2605");
            list.add("47968D9B88E1887C32E066D476736990");
        }
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(list)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this, initializationStatus -> {
            //getString(R.string.app_id)
        });

        AdvertConfig config = AdvertConfig.newBuilder()
                .setAppId(getString(R.string.app_id))
                .setBannerId(getString(R.string.b1))
                .build();

        repository = AdvertAdmobRepository.getInstance(config);


        //Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getInstance(getApplicationContext()));
        //mRequestQueue = Volley.newRequestQueue(this);
        //Fabric.with(this, new Crashlytics());

        // Obtain the FirebaseAnalytics instance.
        //FirebaseAnalytics.getInstance(this);
        //getKeyHash("SHA");
        //getKeyHash("MD5");

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            /**
             * LifecycleObserver method that shows the app open ad when the app moves to foreground.
             */
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    protected void onMoveToForeground() {
//        // Show the ad (if available) when the app moves to foreground.
//        appOpenAdManager.showAdIfAvailable(currentActivity);
//    }
            @Override
            public void onStart(@NonNull LifecycleOwner owner) {

                if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE) {// Show the ad (if available) when the app moves to foreground.
                    if (!appOpenAdManager.isShowingAd) {
                        appOpenAdManager.showAdIfAvailable(currentActivity);
                    }
                    DLog.d("{}{}{}{}" + currentActivity);
                }
            }
        });
        appOpenAdManager = new AppOpenAdManager(getString(R.string.r1));

        AdmobAdsIds m0 = new AdmobAdsIds(
                null,//getString(R.string.admob_native_id),
                null,  //getString(R.string.admob_inter_id),
                getString(R.string.rewardedId)
        );

        RewardManager w = RewardManager.getInstance();
        w.init(m0);
    }


//    @SuppressLint("PackageManagerGetSignatures")
//    private void getKeyHash(String hashStretagy) {
//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance(hashStretagy);
//                md.update(signature.toByteArray());
//
//
//                final byte[] digest = md.digest();
//                final StringBuilder toRet = new StringBuilder();
//                for (int i = 0; i < digest.length; i++) {
//                    if (i != 0) {
//                        //toRet.append(":");
//                    }
//                    int b = digest[i] & 0xff;
//                    String hex = Integer.toHexString(b);
//                    if (hex.length() == 1) toRet.append("0");
//                    toRet.append(hex);
//                }
//
//                DLog.d(getPackageName() + " || " + toRet);
//                String something = new String(Base64.encode(md.digest(), 0));
//                DLog.d("KeyHash  -->>>>>>>>>>>>" + something);
//
//                // Notification.registerGCM(this);
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e1) {
//            DLog.handleException(e1);
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }
//    }

    void log(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        FirebaseAnalytics.getInstance(this)
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (activity instanceof com.google.android.gms.ads.AdActivity) {

        } else {
            //###activity.SplRunActivity
            //###activity.ButtonActivity
            //###activity.StickerInfoActivity
            //###com.google.android.gms.ads.AdActivity
        }

        if (BuildConfig.DEBUG) {
            Toast.makeText(activity, "###" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
            DLog.d("###" + activity.getLocalClassName());
        }
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.

        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}