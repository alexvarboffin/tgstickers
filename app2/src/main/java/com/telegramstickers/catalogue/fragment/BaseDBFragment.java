package com.telegramstickers.catalogue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.telegramstickers.catalogue.databinding.FragmentMainBinding;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.stickers.adapter.empty.EmptyViewModel;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.utils.TelegramUtils;
import com.walhalla.stickers.wads.KSUtil;
import com.walhalla.telegramstickers.adapter.StickerAdapter;

import com.telegramstickers.catalogue.activity.StickerInfoActivity;
import com.walhalla.ui.plugins.Module_U;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BaseDBFragment extends Fragment implements StickerAdapter.ItemClickListener {

    private static final String KEY_BUNDLE_0 = "value_0";

    protected AppDatabase db;
    List<StickerDb> list = new ArrayList<>();


    private String val0 = "";
    private StickerAdapter adapter;
    private KSUtil m;


    public static BaseDBFragment newInstance(String category) {
        BaseDBFragment f = new BaseDBFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BUNDLE_0, category);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = KSUtil.getInstance(getActivity());
        if (getArguments() != null) {
            val0 = getArguments().getString(KEY_BUNDLE_0);
            AppCompatActivity a = (AppCompatActivity) getActivity();
            if (a != null) {
                ActionBar r = a.getSupportActionBar();
                if (r != null) {
                    r.setSubtitle(val0);
                }
            }
        }
        db = LocalDatabaseRepo.getDatabase(getActivity());
        adapter = new StickerAdapter(getContext(), m.getBlockedItems(), db.stickerDao(), false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        @NonNull FragmentMainBinding binding = FragmentMainBinding.inflate(layoutInflater, viewGroup, false);
        db = LocalDatabaseRepo.getDatabase(getActivity());
        int defaultSpanCount = 3;
        GridLayoutManager lm = adapter.getGridLayoutManager(getContext(), defaultSpanCount);
        binding.recyclerView.setLayoutManager(lm);
        adapter.setOnItemClickListener(this);
        binding.recyclerView.setAdapter(adapter);
        refreshCategory(binding, val0);
        return binding.getRoot();
    }

    private void refreshCategory(FragmentMainBinding binding, String category) {
        String categoryLowerCase = category.toLowerCase();
        this.list = db.stickerDao().getCategoryStickers(categoryLowerCase);
        if (this.list == null||list.isEmpty()) {
            adapter.swap(new EmptyViewModel(""));
        }else {
            Collections.shuffle(this.list, new Random(System.nanoTime()));
            adapter.swap(list);
        }
    }

    @Override
    public void onItemClick(StickerDb stickerDb, int position) {
        Intent intent = StickerInfoActivity.newIntent(getContext(), stickerDb._id);
        BaseDBFragment.this.startActivity(intent);
    }

    @Override
    public void shareItem(StickerDb sticker) {
        Module_U.shareText(getContext(), TelegramUtils.ADDSTICKERS_WEB + sticker.link, null);
    }
}
