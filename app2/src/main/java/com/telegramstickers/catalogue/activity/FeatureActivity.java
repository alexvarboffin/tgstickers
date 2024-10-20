package com.telegramstickers.catalogue.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.walhalla.boilerplate.domain.executor.impl.ThreadExecutor;
import com.walhalla.boilerplate.threading.MainThreadImpl;
import com.walhalla.domain.interactors.AdvertInteractor;
import com.walhalla.domain.interactors.impl.AdvertInteractorImpl;
import com.walhalla.domain.repository.AdvertRepository;

import com.telegramstickers.catalogue.TApp;
import com.walhalla.stickers.fragment.AbstractStatusListFragment;
import com.walhalla.ui.DLog;

public abstract class FeatureActivity extends AppCompatActivity {

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // Создаем WakeLock, чтобы предотвратить выключение экрана
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "YourApp:WakeLockTag");
        // Устанавливаем флаг FLAG_KEEP_SCREEN_ON в окне активности
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DLog.d("");
    }


    protected AdvertRepository loadRepository() {
        return TApp.repository;
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
////            case R.string.start_test_again:
////                return false;
//
//            case R.id.action_about:
//                //Module_U.aboutDialog(this);
////                startActivity(new Intent(getApplicationContext(), ActivityAbout.class));
////                overridePendingTransition(R.anim.open_next, R.anim.close_main);
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
////            case R.id.action_discover_more_app:
////                Module_U.moreApp(this);
////                return true;
//
////            case R.id.action_exit:
////                this.finish();
////                return true;
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

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();// Включаем WakeLock
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();// Отключаем WakeLock при приостановке активности
    }


    //base
    protected final long start_time = System.currentTimeMillis();
    private FrameLayout content;

    private final AdvertInteractor.Callback<View> callback = new AdvertInteractor.Callback<>() {
        @Override
        public void onMessageRetrieved(int id, View message) {
            DLog.d(message.getClass().getName() + " --> " + message.hashCode());

            if (content != null) {
                DLog.d("@@@@@@@@@@" + content.getClass().getName());
                try {
                    //content.removeView(message);
                    if (message.getParent() != null) {
                        ((ViewGroup) message.getParent()).removeView(message);
                    }
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.BOTTOM | Gravity.CENTER;
                    message.setLayoutParams(params);

                    ViewTreeObserver vto = message.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("ObsoleteSdkInt")
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < 16) {
                                message.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                message.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            //int width = message.getMeasuredWidth();
                            //int height = message.getMeasuredHeight();
                            //DLog.i("@@@@" + height + "x" + width);
                            //setSpaceForAd(height);
                        }
                    });
                    content.addView(message);

                } catch (Exception e) {
                    DLog.handleException(e);
                }
            }
        }

        @Override
        public void onRetrievalFailed(String error) {
            DLog.d("---->" + error);
        }
    };




    protected void setupAdAtBottom(FrameLayout content) {

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


        AdvertInteractorImpl interactor = new AdvertInteractorImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(), loadRepository());
        //aa.attach(this);
        //DLog.d("---->" + aa.hashCode());
        interactor.selectView(content, callback);
    }



//    private void setSpaceForAd(int height) {
//        DLog.d("@@@@@@@@" + height);
////        FrameLayout content = findViewById(android.R.id.content);
////        if (content != null) {
////            View child0 = content.getChildAt(0);
////            //child0.setPadding(0, 0, 0, 50);
////
////            FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) child0.getLayoutParams();
////            //lp.bottomMargin = height;
////            child0.setLayoutParams(lp);
////        }
//    }

////    AdView adView = new AdView(this);
////    adView.setAdSize(AdSize.BANNER);
////    adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//
//    private void addLayoutToContent(View ad) {
//        content.addView(ad);
//        AdView mAdView = ad.findViewById(R.id.adView);
//        //mAdView.setAdListener(new AdListener(mAdView));
//        mAdView.loadAd(new AdRequest.Builder().build());
//    }
}
