package com.walhalla.stickers.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.stickers.R;
import com.walhalla.stickers.adapter.AbstractStickerAdapter;
import com.walhalla.stickers.adapter.empty.EmptyViewModel;
import com.walhalla.stickers.constants.MyIntent;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.databinding.RewardDialogLayoutBinding;
import com.walhalla.stickers.utils.TelegramUtils;
import com.walhalla.stickers.wads.KSUtil;
import com.walhalla.ui.DLog;
import com.walhalla.ui.plugins.Module_U;
import com.walhalla.utils.AManagerI;
import com.walhalla.utils.RewardManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractStatusListFragment extends Fragment
        implements
        FragmentRefreshListener
        , AbstractStickerAdapter.ItemClickListener
        , AManagerI.RewardManagerCallback {

    protected KSUtil m;
    private RewardManager rm;

    protected AppDatabase db;
    public AbstractStickerAdapter adapter;
    protected String title;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = LocalDatabaseRepo.getDatabase(getActivity());
        m = KSUtil.getInstance(getActivity());
        rm = RewardManager.getInstance();
        Set<Integer> data = new HashSet<>();
        data.add(4);
        data.add(6);
        data.add(7);
        data.add(9);
        m.initialize(data);


        rm.loadRewardAd(getActivity());
        handleInstance();
    }

    private void handleInstance() {
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            this.title = bundle.getString(MyIntent.KEY_CAT_NAME);
        }
        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                if (TextUtils.isEmpty(title)) {
                    actionBar.setSubtitle(R.string.dictionary_all);
                } else {
                    actionBar.setSubtitle(title);//Favorite quotes, word
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        List<StickerDb> list;
        if (TextUtils.isEmpty(title)) {
            list = db.stickerDao().getAllStickers();
        } else {
            list = db.stickerDao().getFavorite();
        }
        handleMessage(list);
    }

    private void handleMessage(List<StickerDb> message) {
        if (message == null || message.isEmpty()) {
            this.adapter.swap(new EmptyViewModel(""));
            //this.adapter.swap(new EmptyViewModel(getString(R.string.emptyFavoriteList)));

        } else {
            this.adapter.swap(message);
        }
    }


    private void showUnlockDialog(FragmentActivity activity, StickerDb data, int position) {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Unlock Item")
//                .setMessage("Watch an ad to unlock this item?")
//                .setPositiveButton("OK", (dialog, which) -> showRewardedAd(getActivity(), data, position))
//                .setNegativeButton("Cancel", null)
//                .show();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        RewardDialogLayoutBinding binding = RewardDialogLayoutBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.dPurchase.setOnClickListener(view -> {
            alertDialog.dismiss();
            rm.showRewardAdBanner(getActivity(), position, this);
        });
        binding.dCancel.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void onItemClick(StickerDb data, int adapterPosition) {
        if (m.isItemLocked(adapterPosition)) {
            //data.setLock(LessonState.UNLOCK);
            showUnlockDialog(getActivity(), data, adapterPosition);
        } else {
            handleProxy(data);
        }
    }

    protected abstract void handleProxy(StickerDb data);

    @Override
    public void shareItem(StickerDb sticker) {
        Module_U.shareText(getContext(), TelegramUtils.ADDSTICKERS_WEB + sticker.link, null);
    }

    @Override
    public void successResult7(int position) {
        DLog.d("@@@@@@");
        // User earned the reward.
        m.unlockItem(position);
        if (this.mainCallback != null) {
            this.mainCallback.rewardExplode();
        } else {
            //Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
        }
        adapter.notifyItemChanged(position);
        //handleProxy(data);
    }

    @Override
    public void errorShowAds() {
        Toast.makeText(getContext(), R.string.ad_not_loaded_try_another_time, Toast.LENGTH_SHORT).show();
    }


    //callback
    Callback mainCallback;

    public interface Callback {

//        void showMessage(String s);

        void rewardExplode();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mainCallback = (Callback) context;
        } else {
            throw new RuntimeException(context + " must implement Callback");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mainCallback = null;
    }
}
