package com.walhalla.telegramstickers.activity.main;

import static com.walhalla.core.TypeNavItem.__TAG_CATEGORY_LIST;
import static com.walhalla.stickers.Pagin.DIVIDER;
import static com.walhalla.ui.DLog.handleException;
import static com.walhalla.ui.plugins.DialogAbout.aboutDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import com.google.android.material.snackbar.Snackbar;
import com.walhalla.core.Navigator;
import com.walhalla.core.TypeNavItem;
import com.walhalla.library.activity.GDPR;

import com.walhalla.stickers.NavigationCls;
import com.walhalla.stickers.databinding.ActivityMainBinding;
import com.walhalla.stickers.fragment.AbstractStatusListFragment;
import com.walhalla.stickers.fragment.FragmentRefreshListener;
import com.walhalla.telegramstickers.MainPresenter;
import com.walhalla.telegramstickers.R;
import com.walhalla.telegramstickers.ResourceHelper;

import com.walhalla.telegramstickers.activity.FeatureActivity;

import com.walhalla.telegramstickers.fragment.BaseDBFragment;

import com.walhalla.telegramstickers.fragment.MainFragment;
import com.walhalla.stickers.database.StickerDb;

import com.walhalla.telegramstickers.fragment.RandomFragment;
import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.xml.listeners.OnParticleSystemUpdateListener;

public class MainActivity extends FeatureActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , MainFragment.FCallback, AbstractStatusListFragment.Callback {


    private FragmentRefreshListener fragmentRefreshListener;

    ArrayList<HashMap<String, String>> stickersList;


    private ProgressDialog dialog;

    private boolean doubleBackToExitPressedOnce;
    private MainPresenter presenter;

    private Thread mThread;

    private ResourceHelper utils;

    private Handler mHandler;

    private ActivityMainBinding binding;
    private Shape.DrawableShape drawableShape;


    public void successResult00(List<StickerDb> arrayList) {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();

            if (this.getFragmentRefreshListener() != null) {
                this.getFragmentRefreshListener().onRefresh();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final int itemId = menuItem.getItemId();
        boolean found = false;
        String CURRENT_TAG = String.valueOf(menuItem.getTitle());
        for (Navigator nav : NavigationCls.mNav0) {
            if (itemId == nav.id) {
                CURRENT_TAG = nav.tag;
                found = true;
                break;
            }
        }

//        if (itemId == R.id.action_my_dreams) {
////                    navItemIndex = 0;
////                    CURRENT_TAG = TAG_MY_DREAMS;
//            binding.drawerLayout.closeDrawers();
//            //@@@ startActivity(new Intent(ButtonActivity.this, Diary.class));
//            return true;
//
////                case R.itemId.action_sleep_date:
////                    navItemIndex = 0;
////                    CURRENT_TAG = TAG_SLEEP_DATE;
////                    break;
//
////                case R.itemId.home:
//
//
////                case R.itemId.action_settings:
////                    navItemIndex = 2;
////                    CURRENT_TAG = TAG_SETTINGS;
////                    break;
//
////                case R.itemId.action_about_apps:
////                    // launch new intent instead of loading fragment
////                    //startActivity(new Intent(ButtonActivity.this, AboutUsActivity.class));
////                    mBinding.drawerLayout.closeDrawers();
////                    return true;
////
////                case R.itemId.nav_share:
////                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
////                    shareIntent.setType(Const.MIME_TEXT);
////                    shareIntent.putExtra(Intent.EXTRA_TEXT, Const.SHARE_MESSAGE_TEXT);
////                    startActivity(Intent.createChooser(shareIntent, Const.SHARE_TITLE));
////                    return true;
//
//
////                    case R.itemId.nav_privacy_policy:
////                        // launch new intent instead of loading fragment
////                        startActivity(new Intent(ButtonActivity.this, PrivacyPolicyActivity.class));
////                        mBinding.drawerLayout.closeDrawers();
////                        return true;
//        }


        if (!found) {
            if (utils.isMenuPressed(itemId)) {
//                        runnable = () -> {
//                            RootForecastsFragment fragment = RootForecastsFragment.newInstance(zodiacs.get(itemId - 1));
//                            replaceFragment(fragment);
//                        };
                //navItemIndex = 0;
                CURRENT_TAG = __TAG_CATEGORY_LIST + DIVIDER + utils.toType(itemId);
            }
        }
        //else {navItemIndex = 1;}

        //Checking if the menuItem is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }
        menuItem.setChecked(true);

        getFragmentByTagName(CURRENT_TAG);
//            DrawerLayout drawer = findViewById(R.itemId.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
        if (drawable != null) {
            drawableShape = new Shape.DrawableShape(drawable, true, true);
        }
        Handler handler = new Handler(Looper.getMainLooper());
        presenter = new MainPresenter(this, handler, this);

        GDPR gdpr = new GDPR();
        gdpr.init(this);

//        AdRequest build = new AdRequest.Builder().build();
//        mBind.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                if (mBind.adView.getVisibility() == View.GONE) {
//                    mBind.adView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//        mBind.adView.loadAd(build);
        utils = ResourceHelper.getInstance(this);
        setSupportActionBar(binding.toolbar);

        //__CURRENT_TAG__ = Constants.TAG_DICTIONARY + DIVIDER + Constants.D_ALL;
        //map.put(R.id.action_favorite_statuses, Constants.TAG_SHOW_FAVORITE_STATUSES);
        //map.put(R.id.action_random_statuses, Constants.TAG_SHOW_RANDOM_STATUSES);

        mHandler = new Handler();
        setUpNavigationView();

        //this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        presenter.onCreateEvent();

        if (savedInstanceState != null) {
//            Parcelable parcelable = savedInstanceState.getParcelable(STATE_QUESTION);
//            if (parcelable != null) {
//                mGameState = (GameState) parcelable;
//            }
//            onSetMachine(State.RESUME_GAME);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(binding.frameContainer.getId(), new MainFragment())
                    .commit();
        }


        setupAdAtBottom(binding.bottomButton);
    }


    private void setUpNavigationView() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }

//Or
//                InputMethodManager inputManager = (InputMethodManager) ButtonActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                View v = ButtonActivity.this.getCurrentFocus();
//                if (v != null) {
//                    ButtonActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);//Setting the actionbarToggle to drawer layout
        toggle.syncState();//calling sync state is necessary or else your hamburger icon wont show up

//        final Menu menu = navigationView.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem item = menu.getItem(i);
//            DLog.d("" + item.toString()+" "+item.getGroupId()+" "+item.getItemId()+" "+item.hasSubMenu());
//        }

        binding.navView.setNavigationItemSelectedListener(this);
        Menu menu = binding.navView.getMenu();
        final Menu mainMenu = binding.navView.getMenu();//inject in root
        if (mainMenu.size() > 0) {
            //inject in submenu
            MenuItem menuItem = mainMenu.getItem(0);
            if (menuItem.hasSubMenu()) {
                menu = menuItem.getSubMenu();
            }
        }

        View header = binding.navView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.textView)).setText(DLog.getAppVersion(this));

        //TypedArray typedArray = getResources().obtainTypedArray(R.array.dictionaryList);
        //__CURRENT_TAG__ = Constants.__TAG_CATEGORY_LIST + DIVIDER + Constants.D_ALL;

        if (menu != null) {
            for (int res : utils.categories()) {
                menu.add(1, res, Menu.FIRST, res).setIcon(R.drawable.ic_star);
                //.setIconTintList(ContextCompat.getColorStateList(this, R.color.t1));
                //subMenu.add(0, sign.getId(), Menu.FIRST, sign.getName()).setIcon(sign.getSignIcon());
                //subMenu.add(1, R.string.sign_02, Menu.FIRST, getString(R.string.sign_01)).setIcon(R.drawable.ic_aries);
            }
        }
//-- menu.add(1, R.id.action_settings, Menu.FIRST + 1, R.string.action_settings);
//        menu.add(1, R.id.action_about, Menu.FIRST + 1, R.string.action_about);
//        menu.add(1, R.id.action_exit, Menu.FIRST + 1, R.string.action_exit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        menu.add("x").setOnMenuItemClickListener(v->{
//            throw new RuntimeException("Test Crash"); // Force a crash
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            aboutDialog(this);
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
            return true;
        } else if (itemId == R.id.action_rate_app) {
            Launcher.rateUs(this);
            return true;
        } else if (itemId == R.id.action_share_app) {
            Module_U.shareThisApp(this);
            return true;
        } else if (itemId == R.id.action_discover_more_app) {
            Module_U.moreApp(this);
            return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;
        }
        return super.onOptionsItemSelected(item);


//            case R.id.action_exit:
//                this.finish();
//                return true;
//            case R.id.action_more_app_01:
//                Module_U.moreApp(this, "com.walhalla.ttloader");
//                return true;
//
//            case R.id.action_more_app_02:
//                Module_U.moreApp(this, "com.walhalla.vibro");
//                return true;

    }


    public void refreshDB() {
        this.stickersList = new ArrayList<>();
        showDialog();
        presenter.execute();
    }

    private void showDialog() {
        this.dialog = new ProgressDialog(MainActivity.this);
        this.dialog.setMessage(MainActivity.this.getString(R.string.loading_stickers));
        this.dialog.setIndeterminate(false);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }


//    public ActionBarHandler getActionBarHandler() {
//        return new ActionBarSearchHandler(this, str -> {
//            db.open();
//            StickerDb sticker = db.getSticker(str);
//            //db--close();
//            if (sticker != null) {
//                Intent intent = new Intent(ButtonActivity.this.getApplicationContext(), StickerInfoActivity.class);
//                intent.putExtra(KEY_STICKERS, sticker.id);
//                ButtonActivity.this.startActivity(intent);
//            }
//        })
//                .enableAutoCompletion().setAutoCompletionMode(ToolbarSearch.AutoCompletionMode.CONTAINS)
//                .enableAutoCompletionDynamic((str, onSearchingListener) -> {
//                    db.open();
//                    ArrayList<StickerDb> allStickers = db.getAllStickers();
//                    //db--close();
//                    List<String> arrayList = new ArrayList<>();
//                    for (int i = 0; i < allStickers.size(); i++) {
//                        if (allStickers.get(i).name.toLowerCase().contains(str.toLowerCase())) {
//                            arrayList.add(allStickers.get(i).name);
//                        }
//                    }
//                    if (arrayList.size() > 0) {
//                        onSearchingListener.onGettingResults(arrayList);
//                    }
//                });
//    }


//    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
//        return new NavigationDrawerTopHandler(getApplicationContext())
//                .addItem(getString(R.string.latest_stickers),
//                        R.drawable.ic_star,
//                        new MainFragment())
//                        .addSection(getString(R.string.categories))
//                .addItem(getString(categories()[0]),
//                        R.drawable.ic_star,
//                        BaseDBFragment.newInstance(Clazz.animals)
//                ).addItem(getString(categories()[1]),
//                        R.drawable.ic_star,
//                        BaseDBFragment.newInstance(Clazz.cartoon)
//                )
//                .addItem(getString(categories()[2]),
//                        BaseDBFragment.newInstance(Clazz.faces)
//                )
//                .addItem(getString(categories()[3]),
//                        BaseDBFragment.newInstance(Clazz.movies)
//                )
//                .addItem(getString(categories()[4]),
//                        BaseDBFragment.newInstance(Clazz.games)
//                )
//                .addItem(getString(categories()[5]),
//                        BaseDBFragment.newInstance(Clazz.memes)
//                )
//                .addItem(getString(categories()[6]),
//                        BaseDBFragment.newInstance(Clazz.messages)
//                )
//                .addItem(getString(categories()[7]),
//                        BaseDBFragment.newInstance(Clazz.others)
//                )
//                .addItem(getString(categories()[8]),
//                        R.drawable.ic_star, new PrivacyFragment());
//    }
//
//    @Override
//    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
//        return new NavigationDrawerBottomHandler(getApplicationContext())
//                .addHelpAndFeedback(view -> ButtonActivity.this.startActivity(new Intent(ButtonActivity.this.getApplicationContext(), FeedbackFragment.class)));
//    }


    public FragmentRefreshListener getFragmentRefreshListener() {
        return this.fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener listener) {
        this.fragmentRefreshListener = listener;
    }


    @Override
    public void onStart() {
        super.onStart();
        //RateThisApp.initialize(this, null);
        //RateThisApp.getInstance(this).showRateDialogIfNeeded(false);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //Pressed back => return to home screen
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(count > 0);
            }
            if (count > 0) {
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(fm.getBackStackEntryAt(0).getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {//count == 0


//                Dialog
//                new AlertDialog.Builder(this)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setTitle("Leaving this App?")
//                        .setMessage("Are you sure you want to close this application?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                finish();
//                            }
//
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
                //super.onBackPressed();


                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    //moveTaskToBack(true);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                backPressedToast();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
            }
        }
    }

    private void backPressedToast() {
        //View view = findViewById(R.id.cLayout);
        //View view = findViewById(android.R.id.content);
        Snackbar.make(binding.coordinatorLayout, R.string.press_again_to_exit, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

//    public void backButtonHandler() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.leave_title);
//        builder.setMessage(R.string.leave_message);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> ButtonActivity.this.finish());
//        builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.cancel());
//        builder.show();
//    }

    private void dismissProgressDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    public void getFragmentByTagName(String currentTag) {

        // selecting appropriate nav menu item
        //selectNavMenu();

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(currentTag) != null) {
            binding.drawerLayout.closeDrawers();
            // show or hide the fab button
            //toggleFab();
            return;
        }
        //Toast.makeText(this, "@@" + __CURRENT_TAG__, Toast.LENGTH_SHORT).show();

        // show or hide the fab button
        //toggleFab();
        //Closing drawer on item click
        binding.drawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
//        Runnable mPendingRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    TimeUnit.MILLISECONDS.sleep(7000);
//                    // update the main content by replacing fragments
//                    Fragment fragment = getHomeFragment();
//                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                            android.R.anim.fade_out);
//                    fragmentTransaction.replace(R.id.frame_container, fragment, CURRENT_TAG);
//                    fragmentTransaction.commitAllowingStateLoss();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        // If mPendingRunnable is not null, then add to the message queue
//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }


        mThread = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(400);
                mHandler.post(() -> {
                    replaceFragmentWithPopBackStack(currentTag);
                });
                //mThread.interrupt();
            } catch (InterruptedException e) {
//                Toast.makeText(this, e.getLocalizedMessage()
//                        + " - " + mThread.getName(), Toast.LENGTH_SHORT).show();
            }
        }, "my-threader");


        if (!mThread.isAlive()) {
            try {
                mThread.start();
            } catch (Exception r) {
                handleException(r);
            }
        }
    }

    public void replaceFragmentWithPopBackStack(String fragmentTag) {
        //Clear back stack
        //final int count = fm.getBackStackEntryCount();
        FragmentManager fm = getSupportFragmentManager();
        if (!fm.isStateSaved()) {
            if (fragmentTag.contains(TypeNavItem.TAG_SHOW_AUTHOR)) {
                //not popup + AppNavigator.TAG_DIVIDER
            } else {
                try {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (IllegalStateException e) {
                    DLog.d("{e}{1}");
                }
            }

            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment(fragmentTag);
            FragmentTransaction ft = fm.beginTransaction();
            //ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);


            if (fragmentTag.contains(TypeNavItem.TAG_SHOW_AUTHOR)) {
                ft.addToBackStack(fragmentTag);
                //ft.replace(R.id.container, fragment);
                ft.replace(binding.frameContainer.getId(), fragment, fragmentTag);//set this fragment in stack
                //@@ft.replace(R.id.container, fragment, null);
            } else {
                ft.addToBackStack(null);
                ft.replace(binding.frameContainer.getId(), fragment, null);
            }

//                    ft.commitAllowingStateLoss();


//                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    ft.replace(R.@@@@@@@@@@@@@@@@@, fragment);
//                    ft.addToBackStack(null);
            try {
                ft.commit();
            } catch (java.lang.IllegalStateException e) {
                DLog.d("{e}{2}");
            }
        }
    }

    private Fragment getHomeFragment(String currentTag) {

//            case TAG_SETTINGS:
//                // settings fragment
//                return KeywordListFragment.newInstance(R.string.app_name);

//            case Constants.TAG_SHOW_FAVORITE_STATUSES:
//                return DescriptionListFragment.newFavorite(); //@ StickerInfoActivity

//            case Constants.TAG_SHOW_RANDOM_STATUSES:
//                return new RandomStatusFragment();

//            case TAG_SLEEP_DATE:
//                // settings fragment
//                return SleepDateFragment.newInstance(-1);

//            case Constants.TAG_WORD_DESCRIPTION:
//                return DescriptionListFragment.newInstance();//@ StickerInfoActivity

//            case 4:
//                // settings fragment
//                settingsFragment = new UserFragment();
//                return settingsFragment;

        if (TypeNavItem.TAG_SHOW_FAVORITE_STATUSES.equals(currentTag)) {
            return MainFragment.newInstance(getString(R.string.menu_favorite_stickers));

        } else if (TypeNavItem.TAG_SHOW_RANDOM_STATUSES.equals(currentTag)) {
            return new RandomFragment();
        } else if (currentTag.startsWith(TypeNavItem.__TAG_CATEGORY_LIST)) {
            String o = currentTag.split(DIVIDER)[1];
            try {
                int num = Integer.parseInt(o);
                return BaseDBFragment.newInstance(
                        //num,
                        getString(utils.titleV(num)));
            } catch (Exception e) {
                handleException(e);
                return new MainFragment();
            }
        }

        //R.id.action_all

        else {
            return new MainFragment();
        }
//                return BaseDBFragment.newInstance(
//                        //Constants.D_ALL,
//                        getString(R.string.dictionary_all));
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

    @Override
    public void onDestroy() {
//        mBind.adView.destroy();
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void rewardExplode() {
        explode();
    }


    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS)
                .max(100);
        binding.konfettiView.setOnParticleSystemUpdateListener(new OnParticleSystemUpdateListener() {
            @Override
            public void onParticleSystemStarted(@NonNull KonfettiView konfettiView, @NonNull Party party, int i) {

            }

            @Override
            public void onParticleSystemEnded(@NonNull KonfettiView konfettiView, @NonNull Party party, int i) {

            }
        });
        binding.konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build());
    }
}
