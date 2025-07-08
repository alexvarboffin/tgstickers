package com.telegramstickers.catalogue.activity.main

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.telegramstickers.catalogue.R
import com.telegramstickers.catalogue.activity.FeatureActivity
import com.telegramstickers.catalogue.fragment.BaseDBFragment
import com.walhalla.core.TypeNavItem
import com.walhalla.library.activity.GDPR
import com.walhalla.stickers.NavigationCls
import com.walhalla.stickers.Pagin
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.databinding.ActivityMainBinding
import com.walhalla.stickers.fragment.AbstractStatusListFragment
import com.walhalla.stickers.fragment.FragmentRefreshListener
import com.walhalla.telegramstickers.MainPresenter
import com.walhalla.telegramstickers.ResourceHelper
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.getAppVersion
import com.walhalla.ui.DLog.handleException
import com.walhalla.ui.plugins.DialogAbout.aboutDialog
import com.walhalla.ui.plugins.Launcher.openBrowser
import com.walhalla.ui.plugins.Launcher.rateUs
import com.walhalla.ui.plugins.Module_U
import com.walhalla.ui.plugins.Module_U.feedback
import com.walhalla.ui.plugins.Module_U.moreApp
import com.walhalla.ui.plugins.Module_U.shareThisApp
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartyFactory
import nl.dionsegijn.konfetti.core.Position.Relative
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Shape.Circle
import nl.dionsegijn.konfetti.core.models.Shape.DrawableShape
import nl.dionsegijn.konfetti.core.models.Shape.Square
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.xml.listeners.OnParticleSystemUpdateListener
import java.util.Arrays
import java.util.concurrent.TimeUnit
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import com.google.android.gms.common.util.CollectionUtils.listOf

class MainActivity : FeatureActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainFragment.FCallback, AbstractStatusListFragment.Callback {
    private var fragmentRefreshListener: FragmentRefreshListener? = null

    var stickersList: ArrayList<HashMap<String, String>>? = null


    private var dialog: ProgressDialog? = null

    private var doubleBackToExitPressedOnce = false
    private var presenter: MainPresenter? = null

    private var mThread: Thread? = null

    private var utils: ResourceHelper? = null

    private var mHandler: Handler? = null

    private var binding: ActivityMainBinding? = null
    private var drawableShape: DrawableShape? = null


    fun successResult00(arrayList: MutableList<StickerDb>) {
        if (this.dialog != null && this.dialog!!.isShowing) {
            this.dialog!!.dismiss()

            if (this.getFragmentRefreshListener() != null) {
                this.getFragmentRefreshListener()!!.onRefresh()
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val itemId = menuItem.itemId
        var found = false
        var CURRENT_TAG = menuItem.title.toString()
        for (nav in NavigationCls.mNav0) {
            if (itemId == nav.id) {
                CURRENT_TAG = nav.tag
                found = true
                break
            }
        }


        //        if (itemId == R.id.action_my_dreams) {
        /*                    navItemIndex = 0;
        * /                    CURRENT_TAG = TAG_MY_DREAMS; */
//            binding.drawerLayout.closeDrawers();
//            //@@@ startActivity(new Intent(ButtonActivity.this, Diary.class));
//            return true;
//
        /*                case R.itemId.action_sleep_date:
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_SLEEP_DATE;
                    break;

                case R.itemId.home:


                case R.itemId.action_settings:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_SETTINGS;
                    break;

                case R.itemId.action_about_apps:
                    // launch new intent instead of loading fragment
                    //startActivity(new Intent(ButtonActivity.this, AboutUsActivity.class));
                    mBinding.drawerLayout.closeDrawers();
                    return true;

                case R.itemId.nav_share:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType(Const.MIME_TEXT);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, Const.SHARE_MESSAGE_TEXT);
                    startActivity(Intent.createChooser(shareIntent, Const.SHARE_TITLE));
                    return true;


                    case R.itemId.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(ButtonActivity.this, PrivacyPolicyActivity.class));
                        mBinding.drawerLayout.closeDrawers();
                        return true;*/
//        }
        if (!found) {
            if (utils!!.isMenuPressed(itemId)) {
//                        runnable = () -> {
//                            RootForecastsFragment fragment = RootForecastsFragment.newInstance(zodiacs.get(itemId - 1));
//                            replaceFragment(fragment);
//                        };
                //navItemIndex = 0;
                CURRENT_TAG =
                    TypeNavItem.__TAG_CATEGORY_LIST + Pagin.DIVIDER + utils!!.toType(itemId)
            }
        }

        //else {navItemIndex = 1;}

        //Checking if the menuItem is in checked state or not, if not make it in checked state
        if (menuItem.isChecked) {
            menuItem.isChecked = false
        } else {
            menuItem.isChecked = true
        }
        menuItem.isChecked = true

        getFragmentByTagName(CURRENT_TAG)
//            DrawerLayout drawer = findViewById(R.itemId.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
        return true
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_heart)
        if (drawable != null) {
            drawableShape = DrawableShape(drawable, true, true)
        }
        val handler = Handler(Looper.getMainLooper())
        presenter = MainPresenter(this, handler, this)

        val gdpr = GDPR()
        gdpr.init(this)

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
        utils = ResourceHelper.getInstance(this)
        setSupportActionBar(binding!!.toolbar)
        setTitle(R.string.app_title)

        //__CURRENT_TAG__ = Constants.TAG_DICTIONARY + DIVIDER + Constants.D_ALL;
        //map.put(R.id.action_favorite_statuses, Constants.TAG_SHOW_FAVORITE_STATUSES);
        //map.put(R.id.action_random_statuses, Constants.TAG_SHOW_RANDOM_STATUSES);
        mHandler = Handler()
        setUpNavigationView()

        //this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        presenter!!.onCreateEvent()

        if (savedInstanceState != null) {
//            Parcelable parcelable = savedInstanceState.getParcelable(STATE_QUESTION);
//            if (parcelable != null) {
//                mGameState = (GameState) parcelable;
//            }
//            onSetMachine(State.RESUME_GAME);
        } else {
            supportFragmentManager.beginTransaction()
                .add(binding!!.frameContainer.id, MainFragment())
                .commit()
        }


        setupAdAtBottom(binding!!.bottomButton)
    }


    private fun setUpNavigationView() {
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding!!.drawerLayout,
            binding!!.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView!!)
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                if (getCurrentFocus() != null) {
                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(
                            currentFocus!!.windowToken,
                            0
                        )
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

            override fun onDrawerOpened(drawerView: View) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView!!)
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                if (currentFocus != null) {
                    inputMethodManager?.hideSoftInputFromWindow(
                        currentFocus!!.windowToken,
                        0
                    )
                }
            }
        }
        binding!!.drawerLayout.addDrawerListener(toggle) //Setting the actionbarToggle to drawer layout
        toggle.syncState() //calling sync state is necessary or else your hamburger icon wont show up

//        final Menu menu = navigationView.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem item = menu.getItem(i);
//            DLog.d("" + item.toString()+" "+item.getGroupId()+" "+item.getItemId()+" "+item.hasSubMenu());
//        }
        binding!!.navView.setNavigationItemSelectedListener(this)
        var menu: Menu? = binding!!.navView.menu
        val mainMenu = binding!!.navView.menu //inject in root
        if (mainMenu.isNotEmpty()) {
            //inject in submenu
            val menuItem = mainMenu[0]
            if (menuItem.hasSubMenu()) {
                menu = menuItem.subMenu
            }
        }

        val header = binding!!.navView.getHeaderView(0)
        (header.findViewById<View?>(R.id.textView) as TextView).setText(getAppVersion(this))

        //TypedArray typedArray = getResources().obtainTypedArray(R.array.dictionaryList);
        //CURRENT_TAG = Constants.__TAG_CATEGORY_LIST + DIVIDER + Constants.D_ALL;
        if (menu != null) {
            for (res in utils!!.categories()) {
                menu.add(1, res, Menu.FIRST, res).setIcon(R.drawable.ic_star)
                //.setIconTintList(ContextCompat.getColorStateList(this, R.color.t1));
                //subMenu.add(0, sign.getId(), Menu.FIRST, sign.getName()).setIcon(sign.getSignIcon());
                //subMenu.add(1, R.string.sign_02, Menu.FIRST, getString(R.string.sign_01)).setIcon(R.drawable.ic_aries);
            }
        }
        //-- menu.add(1, R.id.action_settings, Menu.FIRST + 1, R.string.action_settings);
//        menu.add(1, R.id.action_about, Menu.FIRST + 1, R.string.action_about);
//        menu.add(1, R.id.action_exit, Menu.FIRST + 1, R.string.action_exit);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
//        menu.add("x").setOnMenuItemClickListener(v->{
//            throw new RuntimeException("Test Crash"); // Force a crash
//        });
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.getItemId()
        if (itemId == R.id.action_about) {
            aboutDialog(this)
            return true
        } else if (itemId == R.id.action_privacy_policy) {
            openBrowser(this, getString(R.string.url_privacy_policy))
            return true
        } else if (itemId == R.id.action_rate_app) {
            rateUs(this)
            return true
        } else if (itemId == R.id.action_share_app) {
            shareThisApp(this)
            return true
        } else if (itemId == R.id.action_discover_more_app) {
            moreApp(this)
            return true
        } else if (itemId == R.id.action_feedback) {
            feedback(this)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
//            case R.id.action_exit:
//                this.finish();
//                return true;//case R.id.action_more_app_01:
//                Module_U.moreApp(this, "com.walhalla.ttloader");
//                return true;
//
//            case R.id.action_more_app_02:
//                Module_U.moreApp(this, "com.walhalla.vibro");
//                return true;
    }


    fun refreshDB() {
        this.stickersList = ArrayList()
        showDialog()
        presenter!!.execute()
    }

    private fun showDialog() {
        this.dialog = ProgressDialog(this@MainActivity)
        this.dialog!!.setMessage(this@MainActivity.getString(R.string.loading_stickers))
        this.dialog!!.setIndeterminate(false)
        this.dialog!!.setCancelable(false)
        this.dialog!!.show()
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
    fun getFragmentRefreshListener(): FragmentRefreshListener? {
        return this.fragmentRefreshListener
    }

    override fun setFragmentRefreshListener(listener: FragmentRefreshListener?) {
        this.fragmentRefreshListener = listener
    }


    public override fun onStart() {
        super.onStart()
        //RateThisApp.initialize(this, null);
        //RateThisApp.getInstance(this).showRateDialogIfNeeded(false);
    }

    override fun onBackPressed() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            //Pressed back => return to home screen
            val count = supportFragmentManager.backStackEntryCount
            if (supportActionBar != null) {
                supportActionBar!!.setHomeButtonEnabled(count > 0)
            }
            if (count > 0) {
                val fm = supportFragmentManager
                fm.popBackStack(
                    fm.getBackStackEntryAt(0).id,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            } else { //count == 0


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
                    super.onBackPressed()
                    //moveTaskToBack(true);
                    return
                }

                this.doubleBackToExitPressedOnce = true
                backPressedToast()
                Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 1500)
            }
        }
    }

    private fun backPressedToast() {
        //View view = findViewById(R.id.cLayout);
        //View view = findViewById(android.R.id.content);
        Snackbar.make(
            binding!!.coordinatorLayout,
            R.string.press_again_to_exit,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
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
    private fun dismissProgressDialog() {
        if (this.dialog != null && this.dialog!!.isShowing) {
            this.dialog!!.dismiss()
        }
    }

    fun getFragmentByTagName(currentTag: String) {
        // selecting appropriate nav menu item
        //selectNavMenu();

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer

        if (supportFragmentManager.findFragmentByTag(currentTag) != null) {
            binding!!.drawerLayout.closeDrawers()
            // show or hide the fab button
            //toggleFab();
            return
        }

        //Toast.makeText(this, "@@" + __CURRENT_TAG__, Toast.LENGTH_SHORT).show();

        // show or hide the fab button
        //toggleFab();
        //Closing drawer on item click
        binding!!.drawerLayout.closeDrawers()

        // refresh toolbar menu
        invalidateOptionsMenu()


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
        mThread = Thread(Runnable {
            try {
                TimeUnit.MILLISECONDS.sleep(400)
                mHandler!!.post(Runnable {
                    replaceFragmentWithPopBackStack(currentTag)
                })
                //mThread.interrupt();
            } catch (e: InterruptedException) {
//                Toast.makeText(this, e.getLocalizedMessage()
//                        + " - " + mThread.getName(), Toast.LENGTH_SHORT).show();
            }
        }, "my-threader")


        if (!mThread!!.isAlive()) {
            try {
                mThread!!.start()
            } catch (r: Exception) {
                handleException(r)
            }
        }
    }

    fun replaceFragmentWithPopBackStack(fragmentTag: String) {
        //Clear back stack
        //final int count = fm.getBackStackEntryCount();
        val fm = supportFragmentManager
        if (!fm.isStateSaved) {
            if (fragmentTag.contains(TypeNavItem.TAG_SHOW_AUTHOR)) {
                //not popup + AppNavigator.TAG_DIVIDER
            } else {
                try {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                } catch (e: IllegalStateException) {
                    d("{e}{1}")
                }
            }

            // update the main content by replacing fragments
            val fragment = getHomeFragment(fragmentTag)
            val ft = fm.beginTransaction()


            //ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            if (fragmentTag.contains(TypeNavItem.TAG_SHOW_AUTHOR)) {
                ft.addToBackStack(fragmentTag)
                //ft.replace(R.id.container, fragment);
                ft.replace(
                    binding!!.frameContainer.id,
                    fragment,
                    fragmentTag
                ) //set this fragment in stack
                //@@ft.replace(R.id.container, fragment, null);
            } else {
                ft.addToBackStack(null)
                ft.replace(binding!!.frameContainer.id, fragment, null)
            }


            //                    ft.commitAllowingStateLoss();


//                    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//                    ft.replace(R.@@@@@@@@@@@@@@@@@, fragment);
//                    ft.addToBackStack(null);
            try {
                ft.commit()
            } catch (e: IllegalStateException) {
                d("{e}{2}")
            }
        }
    }

    private fun getHomeFragment(currentTag: String): Fragment {
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

        if (TypeNavItem.TAG_SHOW_FAVORITE_STATUSES == currentTag) {
            return MainFragment.newInstance(getString(R.string.menu_favorite_stickers))
        } else if (TypeNavItem.TAG_SHOW_RANDOM_STATUSES == currentTag) {
            return RandomFragment()
        } else if (currentTag.startsWith(TypeNavItem.__TAG_CATEGORY_LIST)) {
            val o = currentTag.split(Pagin.DIVIDER.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]
            try {
                val num = o.toInt()
                return BaseDBFragment.newInstance( //num,
                    getString(utils!!.titleV(num))
                )
            } catch (e: Exception) {
                handleException(e)
                return MainFragment()
            }
        } else {
            return MainFragment()
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
    public override fun onDestroy() {
//        mBind.adView.destroy();
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun rewardExplode() {
        explode()
    }


    fun explode() {
        val emitterConfig = Emitter(100L, TimeUnit.MILLISECONDS)
            .max(100)
        binding!!.konfettiView.onParticleSystemUpdateListener =
            object : OnParticleSystemUpdateListener {
                override fun onParticleSystemStarted(
                    konfettiView: KonfettiView,
                    party: Party,
                    i: Int
                ) {
                }

                override fun onParticleSystemEnded(
                    konfettiView: KonfettiView,
                    party: Party,
                    i: Int
                ) {
                }
            }
        binding!!.konfettiView.start(
            PartyFactory(emitterConfig)
                .spread(360)
                .shapes(listOf<Shape>(Square, Circle, drawableShape))
                .colors(mutableListOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                .setSpeedBetween(0f, 30f)
                .position(Relative(0.5, 0.3))
                .build()
        )
    }
}
