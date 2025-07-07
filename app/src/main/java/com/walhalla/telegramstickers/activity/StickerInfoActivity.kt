package com.walhalla.telegramstickers.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.walhalla.common.KonfettiPresenter
import com.walhalla.stickers.AppDatabase
import com.walhalla.stickers.database.LocalDatabaseRepo
import com.walhalla.stickers.databinding.ActivityStickerInfoBinding
import com.walhalla.stickers.fragment.QRCodeDialog
import com.walhalla.stickers.presenter.StickerInfoPresenter
import com.walhalla.stickers.utils.TelegramUtils
import com.walhalla.telegramstickers.R
import com.walhalla.telegramstickers.adapter.ImagesInfoAdapter
import com.walhalla.telegramstickers.utils.StickerUtils
import com.walhalla.ui.DLog.d
import com.walhalla.ui.plugins.Module_U.shareText
import androidx.core.net.toUri

@Keep
class StickerInfoActivity : FeatureActivity(), ImagesInfoAdapter.ImAdapterCallback {
    protected var db: AppDatabase? = null
    var list: MutableList<String?> = ArrayList<String?>()
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    var name: String? = ""
    var link: String = ""

    private var binding: ActivityStickerInfoBinding? = null
    private var m: StickerUtils? = null
    private var presenter: StickerInfoPresenter? = null
    private var qrCodeDialog: QRCodeDialog? = null
    private var k: KonfettiPresenter? = null


    // Объявляем launcher как поле класса
    private var requestPermissionLauncher: ActivityResultLauncher<String?>? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Разрешение получено
                setLayout()
            } else {
                // Разрешение не получено
                showNoStoragePermissionSnackbar()
            }
        }
        presenter = StickerInfoPresenter(this, requestPermissionLauncher)
        binding = ActivityStickerInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        k = KonfettiPresenter(binding!!.konfettiView)
        m = StickerUtils()
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

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
        db = LocalDatabaseRepo.getDatabase(this)

        var pos: Long = 1
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_ARG_ID, 0) > 0) {
            // Восстанавливаем id из сохраненного состояния, если оно не пустое и id положительный
            pos = savedInstanceState.getLong(KEY_ARG_ID)
            d("@@R@@" + (intent != null) + ", " + pos)
        } else {
            val intent = getIntent()
            if (intent != null && intent.hasExtra(KEY_ARG_ID)) {
                // Если интент не пустой и содержит id, извлекаем его из интента
                pos = intent.getLongExtra(KEY_ARG_ID, 0)
            }
            d("@@N@@" + (getIntent() != null) + ", " + pos)
        }

        val sticker = db!!.stickerDao().getSticker(pos)
        var author: String? = ""


        if (sticker != null) {
            name = sticker.name
            link = sticker.link
            val r = getSupportActionBar()
            if (r != null) {
                r.setSubtitle(name)
            }
            if (!isEmptyField(sticker.author)) {
                author = sticker.author
                binding!!.author.setText(getString(R.string.author_placeholder, author))
            } else {
                author = getString(R.string.author_unknown)
                binding!!.author.setText(getString(R.string.author_placeholder, author))
            }
            try {
                Glide.with(getApplicationContext())
                    .load(m!!.getThumbnail(sticker))
                    .centerCrop() //.crossFade()
                    .dontTransform() //.crossFade()
                    //fitCenter()
                    //.error(R.drawable.error)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
                            //DLog.d("@@@@" + thumbnail + "@" + sticker.storage);
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable?>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }
                    })
                    .into(binding!!.thumb)
            } catch (ignored: Exception) {
            }


            val bundle2 = Bundle()
            bundle2.putString("value1", "StickerDb Pack Opened")
            bundle2.putString("value2", link)
            this.mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SHARE, bundle2)
        }


        binding!!.name.setText("\"" + name + "\"  Telegram Stickers")

        this.list = m!!.getAllImages(sticker)


        val aa = ImagesInfoAdapter(this, this.list, link, this)
        val numberOfColumns = 3
        binding!!.imagesSticker.setLayoutManager(GridLayoutManager(this, numberOfColumns))
        //        aa.setOnItemClickListener((view1, position) -> {
//            Intent intent = new Intent(view1.getContext(), StickerInfoActivity.class);
//            intent.putExtra(KEY_STICKERS, MainFragment.this.gridArray.get(position).id);
//            MainFragment.this.startActivity(intent);
//        });
        binding!!.imagesSticker.setAdapter(aa)


        binding!!.install.setOnClickListener(View.OnClickListener { view0: View? ->
            //adapter.showBanner(StickerInfoActivity.this, link);
            val isLaunched = TelegramUtils.launchTelegram(this, TelegramUtils.ADDSTICKERS + link)
            if (!isLaunched) {
                //Toast.makeText(this, "No", Toast.LENGTH_SHORT).show();
            } else {
                k!!.rain()
            }
        })

        binding!!.reportSticker.setOnClickListener(View.OnClickListener { view0: View? ->
            val builder = AlertDialog.Builder(this@StickerInfoActivity)
            builder.setMessage(R.string.report_message).setPositiveButton(
                android.R.string.yes,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                    val bundle12 = Bundle()
                    bundle12.putString("value", "Reported $name")
                    this@StickerInfoActivity.mFirebaseAnalytics!!.logEvent(
                        FirebaseAnalytics.Event.SHARE,
                        bundle12
                    )
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                }).setNegativeButton(
                android.R.string.cancel,
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int -> })
            builder.create()
            builder.show()
        })
        setupAdAtBottom(binding!!.bottomButton)
    }

    public override fun onResume() {
        super.onResume()
        if (binding!!.konfettiView.isActive()) {
            binding!!.konfettiView.reset()
        }
    }

    private fun isEmptyField(author: String?): Boolean {
        return author == null || TextUtils.isEmpty(author) || author.equals(
            "null",
            ignoreCase = true
        )
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
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        try {
    //            if (requestCode == PermissionResolver.REQUEST_PERMISSION_CODE) {
    //                if (grantResults != null && grantResults.length > 0
    //                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //                    setlayout();
    //                } else {
    //                    //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
    //                    showNoStoragePermissionSnackbar();
    //                }
    //            }
    //        } catch (Exception e) {
    //            DLog.handleException(e);
    //            //iUtils.ShowToast(this, getString(R.string.info_permission_denied));
    //            showNoStoragePermissionSnackbar();
    //        }
    //    }
    fun showNoStoragePermissionSnackbar() {
        //makeToaster(R.string.info_permission_denied);
        if (binding!!.coordinator != null) {
            Snackbar.make(
                binding!!.coordinator,
                getString(R.string.label_no_storage_permission),
                Snackbar.LENGTH_LONG
            )
                .setActionTextColor(getResources().getColor(android.R.color.white))
                .setAction(getString(R.string.action_settings), View.OnClickListener { v: View? ->
                    openApplicationSettings()
                    val t = Toast.makeText(
                        this,
                        getString(R.string.label_grant_storage_permission),
                        Toast.LENGTH_LONG
                    )
                    t.show()
                }).show()
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            ("package:" + this.packageName).toUri()
        )
        startActivityForResult(appSettingsIntent, APPLICATION_DETAILS_SETTINGS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        d("==> " + requestCode + "\t" + resultCode)
        if (requestCode == APPLICATION_DETAILS_SETTINGS) {
            setLayout()
        } else {
            //@@ mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun setLayout() {
    }

    override fun saveImageRequest(fileUrl: String) {
        presenter!!.saveImageRequest(fileUrl, link)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // close this activity as oppose to navigating up
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_stickerinfo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.getItemId()
        if (itemId == R.id.actionShare) {
            shareText(this, TelegramUtils.ADDSTICKERS_WEB + link, null)
            return true
        } else if (itemId == R.id.actionQr) {
            val qr = TelegramUtils.ADDSTICKERS_WEB + link
            qrCodeDialog = QRCodeDialog.newInstance(qr, name)
            if (getFragmentManager() != null) {
                qrCodeDialog!!.show(supportFragmentManager, "dlg1")
            }
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val KEY_ARG_ID: String = StickerInfoActivity::class.java.simpleName

        //private BannerAdapter adapter;
        @JvmStatic
        fun newIntent(context: Context?, id: Long): Intent {
            val intent = Intent(context, StickerInfoActivity::class.java)
            intent.putExtra(KEY_ARG_ID, id)
            return intent
        }

        private const val APPLICATION_DETAILS_SETTINGS = 10001
    }
}