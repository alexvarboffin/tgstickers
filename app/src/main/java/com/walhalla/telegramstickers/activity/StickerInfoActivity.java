package com.walhalla.telegramstickers.activity;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.walhalla.common.KonfettiPresenter;
import com.walhalla.permissionresolver.permission.PermissionResolver;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;

import com.walhalla.stickers.databinding.ActivityStickerInfoBinding;
import com.walhalla.stickers.fragment.QRCodeDialog;
import com.walhalla.stickers.presenter.StickerInfoPresenter;
import com.walhalla.stickers.utils.TelegramUtils;
import com.walhalla.telegramstickers.adapter.ImagesInfoAdapter;
import com.walhalla.telegramstickers.R;

import com.walhalla.stickers.downloader.DownloadFile;
import com.walhalla.telegramstickers.utils.StickerUtils;
import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Module_U;

import java.util.ArrayList;
import java.util.List;

@Keep
public class StickerInfoActivity extends FeatureActivity implements ImagesInfoAdapter.ImAdapterCallback {

    private static final String KEY_ARG_ID = StickerInfoActivity.class.getSimpleName();

    protected AppDatabase db;
    List<String> list = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;
    String name = "";
    String link = "";

    private ActivityStickerInfoBinding binding;
    private StickerUtils m;
    private StickerInfoPresenter presenter;
    private QRCodeDialog qrCodeDialog;
    private KonfettiPresenter k;


    //private BannerAdapter adapter;

    public static Intent newIntent(Context context, long id) {
        Intent intent = new Intent(context, StickerInfoActivity.class);
        intent.putExtra(KEY_ARG_ID, id);
        return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new StickerInfoPresenter(this);

        binding = ActivityStickerInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        k = new KonfettiPresenter(binding.konfettiView);
        m = new StickerUtils();
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        adapter = new BannerAdapter(this);
//        adapter.loadNewBanner(this);

//@        AdRequest build = new AdRequest.Builder().build();
//@        mBind.adView.setAdListener(new AdListener() {
//@            @Override
//@            public void onAdLoaded() {
//@                if (mBind.adView.getVisibility() == View.GONE) {
//@                    mBind.adView.setVisibility(View.VISIBLE);
//@                }
//@            }
//@        });
//@        mBind.adView.loadAd(build);

        db = LocalDatabaseRepo.getDatabase(this);

        long pos = 1;
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_ARG_ID, 0) > 0) {
            // Восстанавливаем id из сохраненного состояния, если оно не пустое и id положительный
            pos = savedInstanceState.getLong(KEY_ARG_ID);
            DLog.d("@@R@@" + (getIntent() != null) + ", " + pos);
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(KEY_ARG_ID)) {
                // Если интент не пустой и содержит id, извлекаем его из интента
                pos = intent.getLongExtra(KEY_ARG_ID, 0);
            }
            DLog.d("@@N@@" + (getIntent() != null) + ", " + pos);
        }

        StickerDb sticker = db.stickerDao().getSticker(pos);
        String author = "";


        if (sticker != null) {
            name = sticker.name;
            link = sticker.link;
            ActionBar r = getSupportActionBar();
            if (r != null) {
                r.setSubtitle(name);
            }
            if (!isEmptyField(sticker.author)) {
                author = sticker.author;
                binding.author.setText(getString(R.string.author_placeholder, author));
            } else {
                author = getString(R.string.author_unknown);
                binding.author.setText(getString(R.string.author_placeholder, author));
            }
            try {
                Glide.with(getApplicationContext())
                        .load(m.getThumbnail(sticker))
                        .centerCrop()
                        //.crossFade()
                        .dontTransform()
                        //.crossFade()
                        //fitCenter()
                        //.error(R.drawable.error)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                        .listener(new RequestListener<>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, @Nullable @org.jetbrains.annotations.Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                //DLog.d("@@@@" + thumbnail + "@" + sticker.storage);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(binding.thumb);
            } catch (Exception ignored) {
            }


            Bundle bundle2 = new Bundle();
            bundle2.putString("value1", "StickerDb Pack Opened");
            bundle2.putString("value2", link);
            this.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle2);
        }


        binding.name.setText("\"" + name + "\"  Telegram Stickers");

        this.list = m.getAllImages(sticker);


        ImagesInfoAdapter aa = new ImagesInfoAdapter(this, this.list, link, this);
        int numberOfColumns = 3;
        binding.imagesSticker.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
//        aa.setOnItemClickListener((view1, position) -> {
//            Intent intent = new Intent(view1.getContext(), StickerInfoActivity.class);
//            intent.putExtra(KEY_STICKERS, MainFragment.this.gridArray.get(position).id);
//            MainFragment.this.startActivity(intent);
//        });
        binding.imagesSticker.setAdapter(aa);


        binding.install.setOnClickListener(view0 -> {
            //adapter.showBanner(StickerInfoActivity.this, link);
            boolean isLaunched = TelegramUtils.launchTelegram(this, TelegramUtils.ADDSTICKERS + link);
            if (!isLaunched) {
                //Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
            }else{
                k.rain();
            }
//            Bundle bundle1 = new Bundle();
//            bundle1.putString("value1", "Installed StickerPack");
//            bundle1.putString("value2", mBind.infoName.getText().toString());
//            bundle1.putString("value3", "InterstitialAd " + this.onlyOnce);
//            bundle1.putString("value4", "Telegram Installed " + zArr[0]);
//            StickerInfoActivity.this.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle1);
        });

        binding.reportSticker.setOnClickListener(view0 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(StickerInfoActivity.this);
            builder.setMessage(R.string.report_message).setPositiveButton(
                    android.R.string.yes, (dialogInterface, i) -> {
                        Bundle bundle12 = new Bundle();
                        bundle12.putString("value", "Reported " + name);
                        StickerInfoActivity.this.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle12);
                        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
            });
            builder.create();
            builder.show();
        });
        setupAdAtBottom(binding.bottomButton);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(binding.konfettiView.isActive()){
            binding.konfettiView.reset();
        }
    }

    private boolean isEmptyField(String author) {
        return author == null || TextUtils.isEmpty(author) || author.equalsIgnoreCase("null");
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mBind.adView.resume();
//    }

//    @Override
//    public void onPause() {
//        mBind.adView.pause();
//        super.onPause();
//    }

//    @Override
//    public void onDestroy() {
//        mBind.adView.destroy();
//        super.onDestroy();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == PermissionResolver.REQUEST_PERMISSION_CODE) {
                if (grantResults != null && grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setlayout();
                } else {
                    //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
                    showNoStoragePermissionSnackbar();
                }
            }
        } catch (Exception e) {
            DLog.handleException(e);
            //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
            showNoStoragePermissionSnackbar();
        }
    }

    public void showNoStoragePermissionSnackbar() {
        //makeToaster(R.string.info_permission_denied);
        if (binding.coordinator != null) {
            Snackbar.make(binding.coordinator, getString(R.string.label_no_storage_permission), Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(android.R.color.white))
                    .setAction(getString(R.string.action_settings), v -> {
                        openApplicationSettings();
                        Toast t = Toast.makeText(this, getString(R.string.label_grant_storage_permission), Toast.LENGTH_LONG);
                        t.show();
                    }).show();
        }
    }

    private static final int APPLICATION_DETAILS_SETTINGS = 10001;

    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
        startActivityForResult(appSettingsIntent, APPLICATION_DETAILS_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DLog.d("==> " + requestCode + "\t" + resultCode);
        if (requestCode == APPLICATION_DETAILS_SETTINGS) {
            setlayout();
        } else {
            //@@ mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setlayout() {
    }

    @Override
    public void saveImageRequest(String fileUrl) {
        presenter.saveImageRequest(fileUrl, link);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stickerinfo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.actionShare) {
            Module_U.shareText(this, TelegramUtils.ADDSTICKERS_WEB + link, null);
            return true;
        } else if (itemId == R.id.actionQr) {
            String qr = TelegramUtils.ADDSTICKERS_WEB + link;
            qrCodeDialog = QRCodeDialog.newInstance(qr, name);
            if (getFragmentManager() != null) {
                qrCodeDialog.show(getSupportFragmentManager(), "dlg1");
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}