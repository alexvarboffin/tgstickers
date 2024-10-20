//package com.walhalla.telegramstickers.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.Toolbar;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//import com.walhalla.telegramstickers.R;
//
//
//public class Main extends BaseActivity
//{
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setTheme(R.style.AppTheme_NoActionBar);
//        setContentView(R.layout.activity_main2);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        if (savedInstanceState == null) {
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.@@@@@@@@@@@@@@@@@, MainFragment8888.newInstance())
////                    .commitNow();
//        }
//
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            //LoginManager.getInstance().logOut();
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//}
