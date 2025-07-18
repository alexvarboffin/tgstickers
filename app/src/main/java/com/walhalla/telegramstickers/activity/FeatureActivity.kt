package com.walhalla.telegramstickers.activity

import com.walhalla.domain.interactors.impl.AdvertInteractorImpl
import com.walhalla.domain.repository.AdvertRepository
import com.walhalla.telegramstickers.TApp
import com.walhalla.ui.DLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope

abstract class FeatureActivity : androidx.appcompat.app.AppCompatActivity() {
    private var wakeLock: android.os.PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        val powerManager =
            getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager
        // Создаем WakeLock, чтобы предотвратить выключение экрана
        wakeLock = powerManager.newWakeLock(
            android.os.PowerManager.SCREEN_BRIGHT_WAKE_LOCK or android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "YourApp:WakeLockTag"
        )
        // Устанавливаем флаг FLAG_KEEP_SCREEN_ON в окне активности
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    protected fun loadRepository(): AdvertRepository? {
        return TApp.repository
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.main, menu);
    //        return true;
    //    }
    //    @SuppressLint("NonConstantResourceId")
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        switch (item.getItemId()) {
    //
    /*            case R.string.start_test_again:
                 return false;
    //
    //            case R.id.action_about:
    //                //Module_U.aboutDialog(this);
    /**/                startActivity(new Intent(getApplicationContext(), ActivityAbout.
    class));
    * /                overridePendingTransition(R.anim.open_next, R.anim.close_main); */
    //                return true;
    //
    //            case R.id.action_privacy_policy:
    //                Module_U.openBrowser(this, ...);
    //                return true;
    //
    //            case R.id.action_rate_app:
    //                Module_U.rateUs(this);
    //                return true;
    //
    //            case R.id.action_share_app:
    //                Module_U.shareThisApp(this);
    //                return true;
    //
    /*            case R.id.action_discover_more_app:
                    Module_U.moreApp(this);
                    return true;
    //
                case R.id.action_exit:
                    this.finish();
                    return true; */
    //
    //            case R.id.action_feedback:
    //                Module_U.feedback(this);
    //                return true;
    //
    //            default:
    //                return super.onOptionsItemSelected(item);
    //        }
    //
    //
    //        //action_how_to_use_app
    //        //action_support_developer
    //
    //        //return super.onOptionsItemSelected(item);
    //    }

    @android.annotation.SuppressLint("WakelockTimeout")
    override fun onResume() {
        super.onResume()
        wakeLock!!.acquire() // Включаем WakeLock
    }

    override fun onPause() {
        super.onPause()
        wakeLock!!.release() // Отключаем WakeLock при приостановке активности
    }


    //base
    protected val start_time: kotlin.Long = java.lang.System.currentTimeMillis()
    private val content: android.widget.FrameLayout? = null

    private val callback: com.walhalla.domain.interactors.AdvertInteractor.Callback<android.view.View> =
        object : com.walhalla.domain.interactors.AdvertInteractor.Callback<android.view.View> {
            override fun onMessageRetrieved(id: Int, message: android.view.View) {
                DLog.d(message.javaClass.getName() + " --> " + message.hashCode())

                if (content != null) {
                    DLog.d("@@@@@@@@@@" + content.javaClass.getName())
                    try {
                        //content.removeView(message);
                        if (message.getParent() != null) {
                            (message.getParent() as android.view.ViewGroup).removeView(message)
                        }
                        val params = android.widget.FrameLayout.LayoutParams(
                            android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                            android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER
                        message.setLayoutParams(params)

                        val vto = message.getViewTreeObserver()
                        vto.addOnGlobalLayoutListener(object :
                            android.view.ViewTreeObserver.OnGlobalLayoutListener {
                            @android.annotation.SuppressLint("ObsoleteSdkInt")
                            override fun onGlobalLayout() {
                                if (android.os.Build.VERSION.SDK_INT < 16) {
                                    message.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                                } else {
                                    message.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                                }
                                //int width = message.getMeasuredWidth();
                                //int height = message.getMeasuredHeight();
                                //DLog.i("@@@@" + height + "x" + width);
                                //setSpaceForAd(height);
                            }
                        })
                        content.addView(message)
                    } catch (e: java.lang.Exception) {
                        DLog.handleException(e)
                    }
                }
            }

            override fun onRetrievalFailed(error: kotlin.String) {
                DLog.d("---->" + error)
            }
        }


    protected fun setupAdAtBottom(content: android.widget.FrameLayout) {
        //FrameLayout content = findViewById(android.R.id.content);


//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.BOTTOM;

//        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater()
//                .inflate(R.layout.ad_layout, null);
//        linearLayout.setLayoutParams(params);
//
//        // adding viewtreeobserver to get height of linearLayout layout , so that
//        // android.R.id.content will set margin of that height
//        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @SuppressLint("ObsoleteSdkInt")
//            @Override
//            public void onGlobalLayout() {
//                if (Build.VERSION.SDK_INT < 16) {
//                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                } else {
//                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                int width = linearLayout.getMeasuredWidth();
//                int height = linearLayout.getMeasuredHeight();
//                //DLog.i("@@@@" + height + "x" + width);
//                setSpaceForAd(height);
//            }
//        });
//        addLayoutToContent(linearLayout);


        val interactor = AdvertInteractorImpl(CoroutineScope(Dispatchers.IO), MainScope(), loadRepository()!!)

        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor.selectView(content, callback)
    } //    private void setSpaceForAd(int height) {
    //        DLog.d("@@@@@@@@" + height);
    /*        FrameLayout content = findViewById(android.R.id.content);
           if (content != null)
    {
                    View child0 = content.getChildAt(0);
                    //child0.setPadding(0, 0, 0, 50);

                    FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) child0.getLayoutParams();
                    //lp.bottomMargin = height;
                    child0.setLayoutParams(lp);

    }
    //    }
        AdView adView = new AdView(this);
       adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); */
    //
    //    private void addLayoutToContent(View ad) {
    //        content.addView(ad);
    //        AdView mAdView = ad.findViewById(R.id.adView);
    //        //mAdView.setAdListener(new AdListener(mAdView));
    //        mAdView.loadAd(new AdRequest.Builder().build());
    //    }
}
