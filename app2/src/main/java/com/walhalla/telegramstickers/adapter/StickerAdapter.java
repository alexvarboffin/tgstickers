package com.walhalla.telegramstickers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.telegramstickers.catalogue.R;
import com.walhalla.stickers.adapter.AbstractStickerAdapter;
import com.walhalla.stickers.adapter.StickerPackViewHolder;
import com.walhalla.stickers.adapter.ViewModel;
import com.walhalla.stickers.adapter.empty.EmptyViewHolder;
import com.walhalla.stickers.database.StickerDao;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.databinding.RowEmptyBinding;
import com.walhalla.stickers.databinding.StickerItemBinding;
import com.walhalla.telegramstickers.utils.StickerUtils;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StickerAdapter extends AbstractStickerAdapter {


    private final Context context;
    private final StickerUtils m;

    public StickerAdapter(Context context, Set<Integer> unlockedItems, StickerDao dao, List<ViewModel> list, boolean isFavorite) {
        super(context, unlockedItems, dao, isFavorite);
        this.context = context;
        this.data = list;
        m = new StickerUtils();
    }

    public StickerAdapter(Context context, Set<Integer> unlockedItems, StickerDao dao, boolean isFavorite) {
        super(context, unlockedItems, dao, isFavorite);
        this.context = context;
        this.data = new ArrayList<>();
        m = new StickerUtils();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_STICKER) {
            StickerItemBinding binding = StickerItemBinding.inflate(inflater, parent, false);
            return new StickerPackViewHolder(binding, mClickListener, unlockedItems);
        }
        //case VIEW_TYPE_EMPTY:
        @NonNull RowEmptyBinding view1 = RowEmptyBinding.inflate(inflater, parent, false);
        return new EmptyViewHolder(view1);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DLog.d("@@@" + position);

        if (holder.getItemViewType() == ITEM_TYPE_STICKER) {
            StickerDb sticker = (StickerDb) this.data.get(position);
            StickerPackViewHolder h0 = ((StickerPackViewHolder) holder);
            h0.bindItem(sticker, m.getThumbnail(sticker), this);
            //holder.binding.stickerName.setText(sticker.name+"@"+sticker.category);
            h0.itemView.setOnClickListener(v9 -> {
                if (mClickListener != null) mClickListener.onItemClick(sticker, position);
            });
        } else {
            EmptyViewHolder vh2 = (EmptyViewHolder) holder;
            vh2.bind(data.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public int getPositionFromData(String character) {
        int position = 0;
        for (ViewModel viewModel : data) {
            if (viewModel instanceof StickerDb) {
                String letter = "" + ((StickerDb) viewModel).link.charAt(0);
                if (letter.equalsIgnoreCase("" + character)) {
                    return position;
                }
                position++;
            }
        }
        return 0;
    }


}
