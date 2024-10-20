package com.telegramstickers.catalogue.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.telegramstickers.catalogue.R;
import com.telegramstickers.catalogue.databinding.FragmentHomeBinding;

import com.walhalla.stickers.constants.MyIntent;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.fragment.AbstractStatusListFragment;
import com.walhalla.stickers.fragment.FragmentRefreshListener;
import com.walhalla.stickers.utils.TelegramUtils;
import com.walhalla.telegramstickers.adapter.StickerAdapter;

import com.telegramstickers.catalogue.activity.StickerInfoActivity;

import java.util.List;


public class MainFragment extends AbstractStatusListFragment {

    private FCallback mCallback;


    private FragmentHomeBinding binding;


    public static Fragment newInstance(String title) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MyIntent.KEY_CAT_NAME, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFavorite = !TextUtils.isEmpty(title);
        adapter = new StickerAdapter(getContext(), m.getBlockedItems(), db.stickerDao(), isFavorite);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (useAlphaSectionRes()) {
//            final String[] customAlphabet = getResources().getStringArray(R.array.alphabet);
//            binding.alphSectionIndex.setAlphabet(customAlphabet);
//            binding.alphSectionIndex.onSectionIndexClickListener((view1, position, character) -> {
//
//                int newValue = adapter.getPositionFromData(character);
//                DLog.d("[scroll]" + newValue);
//
//                //String info = " Position = " + position + " Char = " + character + "\t" + getPositionFromData(character);
//                //Log.i("View: ", view1 + "," + info);
//                //Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
//                //recyclerView.smoothScrollToPosition(getPositionFromData(character));
//                binding.recyclerView.scrollToPosition(newValue);
//            });
//        } else {
//            binding.alphSectionIndex.setVisibility(View.GONE);
//        }
        //floatingActionButton.attachToListView(this.gridView);
//        mBind.addNewStickers.setVisibility(View.GONE);
//        binding.addNewStickers.setOnClickListener(view0 ->
//                startActivityForResult(new Intent(MainFragment.this.getActivity(), AddStickerActivity.class), 1));

        mCallback.setFragmentRefreshListener(this);


        int numberOfColumns = 3;
        GridLayoutManager lm = adapter.getGridLayoutManager(getContext(), numberOfColumns);
        binding.recyclerView.setLayoutManager(lm);
        adapter.setOnItemClickListener(this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void handleProxy(StickerDb data) {
        Intent intent = StickerInfoActivity.newIntent(getContext(), data._id);
        MainFragment.this.startActivity(intent);
    }

    private boolean useAlphaSectionRes() {
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FCallback) {
            mCallback = (FCallback) context;
        } else {
            throw new RuntimeException(context + " must implement Callback");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }




    public interface FCallback {
        void setFragmentRefreshListener(FragmentRefreshListener listener);
    }
}
