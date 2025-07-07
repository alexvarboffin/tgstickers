package com.walhalla.stickers.fragment

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.FragmentActivity
import com.walhalla.stickers.AppDatabase
import com.walhalla.stickers.R
import com.walhalla.stickers.adapter.AbstractStickerAdapter
import com.walhalla.stickers.adapter.AbstractStickerAdapter.ItemClickListener
import com.walhalla.stickers.adapter.empty.EmptyViewModel
import com.walhalla.stickers.constants.MyIntent
import com.walhalla.stickers.database.LocalDatabaseRepo
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.databinding.RewardDialogLayoutBinding
import com.walhalla.stickers.utils.TelegramUtils
import com.walhalla.stickers.wads.KSUtil
import com.walhalla.ui.DLog.d
import com.walhalla.ui.plugins.Module_U
import com.walhalla.utils.AManagerI.RewardManagerCallback
import com.walhalla.utils.RewardManager
import androidx.core.graphics.drawable.toDrawable

const val UNLOCK_ITEM_IF_FAILED = true


abstract class AbstractStatusListFragment : androidx.fragment.app.Fragment(), FragmentRefreshListener, ItemClickListener{
    @JvmField
    protected var m: KSUtil? = null
    private var rm: RewardManager? = null

    @JvmField
    protected var db: AppDatabase? = null
    @JvmField
    var adapter: AbstractStickerAdapter? = null
    @JvmField
    protected var title: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = LocalDatabaseRepo.getDatabase(activity)
        m = KSUtil.getInstance(activity)
        rm = RewardManager.instance
        val data: MutableSet<Int?> = HashSet<Int?>()
        data.add(4)
        data.add(6)
        data.add(7)
        data.add(9)
        m!!.initialize(data)


        rm!!.loadRewardAd(requireActivity())
        handleInstance()
    }

    private fun handleInstance() {
        val bundle = arguments
        if (arguments != null) {
            this.title = bundle!!.getString(MyIntent.KEY_CAT_NAME)
        }
        if (activity != null) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            if (actionBar != null) {
                if (TextUtils.isEmpty(title)) {
                    actionBar.setSubtitle(R.string.dictionary_all)
                } else {
                    actionBar.subtitle = title //Favorite quotes, word
                }
            }
        }
    }

    override fun onRefresh() {
        val list: MutableList<StickerDb>?
        if (TextUtils.isEmpty(title)) {
            list = db!!.stickerDao().getAllStickers()
        } else {
            list = db!!.stickerDao().getFavorite()
        }
        handleMessage(list)
    }

    private fun handleMessage(message: MutableList<StickerDb>?) {
        if (message == null || message.isEmpty()) {
            this.adapter!!.swap(EmptyViewModel(""))

            //this.adapter.swap(new EmptyViewModel(getString(R.string.emptyFavoriteList)));
        } else {
            this.adapter!!.swap(message)
        }
    }


    private fun showUnlockDialog(activity: FragmentActivity, data: StickerDb?, position: Int) {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Unlock Item")
//                .setMessage("Watch an ad to unlock this item?")
//                .setPositiveButton("OK", (dialog, which) -> showRewardedAd(getActivity(), data, position))
//                .setNegativeButton("Cancel", null)
//                .show();
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val binding = RewardDialogLayoutBinding.inflate(inflater)
        builder.setView(binding.getRoot())
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        binding.dPurchase.setOnClickListener(View.OnClickListener { view: View? ->
            alertDialog.dismiss()
            rm!!.showRewardAdBanner(requireActivity(), position, object : RewardManagerCallback {
                override fun successResult7(position: Int) {
                    d("@@@@@@")
                    // User earned the reward.
                    m!!.unlockItem(position)
                    if (mainCallback != null) {
                        mainCallback!!.rewardExplode()
                    } else {
                        //Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    adapter!!.notifyItemChanged(position)
                    //handleProxy(data);
                }

                override fun errorShowAds(position: Int) {
                    if(UNLOCK_ITEM_IF_FAILED){
                        m?.unlockItem(position)//Unlock if ads not loaded
                        adapter?.notifyItemChanged(position)//Unlock if ads not loaded
                    }
                    Toast.makeText(context, R.string.ad_not_loaded_try_another_time, Toast.LENGTH_SHORT).show()
                }
            })
        })
        binding.dCancel.setOnClickListener(View.OnClickListener { view: View? -> alertDialog.dismiss() })
        alertDialog.show()
    }

    override fun onItemClick(data: StickerDb, adapterPosition: Int) {
        if (m!!.isItemLocked(adapterPosition)) {
            //data.setLock(LessonState.UNLOCK);
            showUnlockDialog(requireActivity(), data, adapterPosition)
        } else {
            handleProxy(data)
        }
    }

    protected abstract fun handleProxy(data: StickerDb)

    override fun shareItem(sticker: StickerDb) {
        Module_U.shareText(requireContext(), TelegramUtils.ADDSTICKERS_WEB + sticker.link, null)
    }



    //callback
    var mainCallback: Callback? = null

    interface Callback {
        //        void showMessage(String s);
        fun rewardExplode()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback) {
            mainCallback = context as Callback
        } else {
            throw RuntimeException("$context must implement Callback")
        }
    }


    override fun onDetach() {
        super.onDetach()
        mainCallback = null
    }
}
