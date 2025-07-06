package com.telegramstickers.catalogue

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.walhalla.domain.repository.from_internet.AdvertAdmobRepository
import com.walhalla.domain.repository.from_internet.AdvertConfig
import com.walhalla.library.BuildConfig
import com.walhalla.ui.DLog.d
import com.walhalla.utils.AdmobAdsIds
import com.walhalla.utils.RewardManager
import com.walhalla.wads.AppOpenAdManager
import com.walhalla.wads.OnShowAdCompleteListener

class TApp : Application() {
    private var appOpenAdManager: AppOpenAdManager? = null
    private var currentActivity: Activity? = null

    private var intersticialCounter = 0


    fun hasToDisplayIntersticiel(): Boolean {
        return this.intersticialCounter >= 3
    }

    fun incIntersticialCounter() {
        d("Counter " + this.intersticialCounter)
        this.intersticialCounter++
    }

    fun resetIntersticialCounter() {
        this.intersticialCounter = 0
    }

    val secondsBeforeIntersticial: Int
        get() = 5000


    //    @Override
    //    protected void attachBaseContext(Context base) {
    //        super.attachBaseContext(base);
    //        MultiDex.install(this);
    //    }
    override fun onCreate() {
        super.onCreate()
        sInstance = this


        //SharedPrefUtil.init(getApplicationContext());
        //DatabaseUtil.init(getApplicationContext());
        this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            /**
             * ActivityLifecycleCallback methods.
             */
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                if (activity is AdActivity) {
                } else {
                    //###activity.SplRunActivity
                    //###activity.ButtonActivity
                    //###activity.StickerInfoActivity
                    //###com.google.android.gms.ads.AdActivity
                }

                if (BuildConfig.DEBUG) {
                    Toast.makeText(
                        activity,
                        "###" + activity.getLocalClassName(),
                        Toast.LENGTH_SHORT
                    ).show()
                    d("###" + activity.getLocalClassName())
                }

                // An ad activity is started when an ad is showing, which could be AdActivity class from Google
                // SDK or another activity class implemented by a third party mediation partner. Updating the
                // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
                // one that shows the ad.
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                if (activity is AdActivity) {
                } else {
                    currentActivity = activity
                    if (BuildConfig.DEBUG) {
                        //java.lang.Exception: Toast callstack! strTip=
                        // @@@Toast.makeText(myApplication, "@@@" + activity.getLocalClassName(), Toast.LENGTH_SHORT).show();
                        d("@@@" + activity.getLocalClassName())
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })


        val list: MutableList<String?> = ArrayList<String?>()

        run {
            list.add(AdRequest.DEVICE_ID_EMULATOR)
            list.add("A8A2F7804653E219880030864C1F32E4")
            list.add("5D5A89BC6372A49242D138B9AC352894")
            list.add("A2A86E2966898F258CB671EE038C2703")
            list.add("E20E841EDD87D721EE54510499AE2605")
            list.add("47968D9B88E1887C32E066D476736990")
        }
        val requestConfiguration = RequestConfiguration.Builder()
            .setTestDeviceIds(list)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)
        MobileAds.initialize(
            this,
            OnInitializationCompleteListener { initializationStatus: InitializationStatus? -> })

        val config: AdvertConfig? = AdvertConfig.newBuilder()
            .setAppId(getString(R.string.app_id))
            .setBannerId(getString(R.string.b1))
            .build()

        repository = AdvertAdmobRepository.getInstance(config!!)


        //Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getInstance(getApplicationContext()));
        //mRequestQueue = Volley.newRequestQueue(this);
        //Fabric.with(this, new Crashlytics());

        // Obtain the FirebaseAnalytics instance.
        //FirebaseAnalytics.getInstance(this);
        //getKeyHash("SHA");
        //getKeyHash("MD5");
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            /**
             * LifecycleObserver method that shows the app open ad when the app moves to foreground.
             */
            //    @OnLifecycleEvent(Lifecycle.Event.ON_START)
            //    protected void onMoveToForeground() {
            //        // Show the ad (if available) when the app moves to foreground.
            //        appOpenAdManager.showAdIfAvailable(currentActivity);
            //    }
            override fun onStart(owner: LifecycleOwner) {
                if (ACTIVITY_MOVES_TO_FOREGROUND_HANDLE) { // Show the ad (if available) when the app moves to foreground.
                    if (!appOpenAdManager!!.isShowingAd) {
                        appOpenAdManager!!.showAdIfAvailable(currentActivity)
                    }
                    d("{}{}{}{}" + currentActivity)
                }
            }
        })
        appOpenAdManager = AppOpenAdManager(getString(R.string.r1))


        val m0 = AdmobAdsIds(
            null,  //getString(R.string.admob_native_id),
            null,  //getString(R.string.admob_inter_id),
            getString(R.string.rewardedId)
        )

        val w: RewardManager? = RewardManager.instance
        w?.init(m0)
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
    fun log(id: String?, name: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        FirebaseAnalytics.getInstance(this)
            .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }


    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager!!.showAdIfAvailable(activity, onShowAdCompleteListener)
    }


    companion object {
        private const val ACTIVITY_MOVES_TO_FOREGROUND_HANDLE = true

        const val NB_VIDEOS_BEFORE_AD: Int = 3
        const val SECONDS_TO_COUNT_VIDEOS: Int = 5

        //private static final String ONESIGNAL_APP_ID = "8fcf9ed0-0064-49ee-9d27-aca9bb3bd972";
        private var sInstance: TApp? = null

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
        var repository: AdvertAdmobRepository? = null

        val context: Context
            get() = sInstance!!.applicationContext
    }
}