package com.walhalla.stickers.adapter;

import android.view.View;

import androidx.recyclerview.widget.DiffUtil;

import com.walhalla.stickers.adapter.empty.EmptyViewModel;
import com.walhalla.stickers.database.StickerDb;

import java.util.List;

public class StickerDiffCallback extends DiffUtil.Callback {

    private final List<ViewModel> oldList;
    private final List<ViewModel> newList;

    public StickerDiffCallback(List<ViewModel> oldList, List<ViewModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        if (oldItem instanceof StickerDb && newItem instanceof StickerDb) {
            return ((StickerDb) oldItem).getId() == (((StickerDb) newItem).getId());
        } else return oldItem instanceof EmptyViewModel && newItem instanceof EmptyViewModel;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        if (oldItem instanceof StickerDb && newItem instanceof StickerDb) {
            return oldItem.equals(newItem);
        } else return oldItem instanceof EmptyViewModel && newItem instanceof EmptyViewModel;
    }
}
