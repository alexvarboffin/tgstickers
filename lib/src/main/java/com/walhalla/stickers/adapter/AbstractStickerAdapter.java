package com.walhalla.stickers.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.stickers.constants.Constants;
import com.walhalla.stickers.R;
import com.walhalla.stickers.adapter.empty.EmptyViewModel;
import com.walhalla.stickers.database.StickerDao;
import com.walhalla.stickers.database.StickerDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractStickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_STICKER = 134;
    public static final int ITEM_TYPE_EMPTY = 112;


    private final SharedPreferences preferences;
    private final Context context;
    private final StickerDao dao;
    private final boolean isFavorite;
    protected final Set<Integer> unlockedItems;


    protected List<ViewModel> data = new ArrayList<>();
    protected ItemClickListener mClickListener;


    public AbstractStickerAdapter(Context context, Set<Integer> unlockedItems, StickerDao dao, boolean isFavorite) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        this.unlockedItems = unlockedItems;
        this.dao = dao;
        this.isFavorite = isFavorite;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    protected void onFavoriteClickRequest(StickerPackViewHolder holder, StickerDb sticker, int position) {
        String key_name;
        if (sticker.isLiked()) {
            key_name = Constants.KEY_ADD_FAVORITE;
        } else {
            key_name = Constants.KEY_REMOVE_FAVORITE;
        }
        boolean key = preferences.getBoolean(key_name, false);
        if (!key) {
            makeDialog(context, sticker, position, holder);
            preferences.edit().putBoolean(key_name, true).apply();
        } else {
            processFavorite(sticker, position, holder);
        }
    }

    private void processFavorite(StickerDb sticker, int position, StickerPackViewHolder holder) {
        if (sticker.isLiked()) {
            sticker.setDisLiked();
        } else {
            sticker.setLiked();
        }
        holder.updateFavorite(sticker);

        int result = dao.update(sticker);
        if (!sticker.isLiked() && isFavorite) {
            data.remove(position);
            if (data == null || data.isEmpty()) {
                swap(new EmptyViewModel(context.getString(R.string.emptyFavoriteList)));
            }
            notifyDataSetChanged();
        }
//        if (callback != null) {
//            if (result > 0) {
//                mMainThread.post(() -> callback.onMessageRetrieved(result));
//            } else {
//                mMainThread.post(() -> callback.onRetrievalFailed("Database is empty, reinstall the application"));
//            }
//        }
    }

    public void swap(EmptyViewModel emptyViewModel) {
        List<ViewModel> newList = new ArrayList<>();
        newList.add(emptyViewModel);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new StickerDiffCallback(data, newList));
        this.data.clear();
        this.data.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    private void makeDialog(Context context, StickerDb sticker, int position, StickerPackViewHolder holder) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout;
        if (inflater != null) {
            alertLayout = inflater.inflate(R.layout.text_dialog, null);
            final TextView textView = alertLayout.findViewById(R.id.messageDelete);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            if (sticker.isLiked()) {
                textView.setText(R.string.remove_from_favorites_question);
                alert.setTitle(R.string.remove_from_favorites);
            } else {
                textView.setText(R.string.add_it_to_favorites_question);
                alert.setTitle(R.string.add_to_favorite_stickers);
            }
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            alert.setCancelable(false);
            alert.setNegativeButton(android.R.string.no, (dialog, which) -> {
            });

            alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                processFavorite(sticker, position, holder);
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }

    public void swap(List<StickerDb> newData) {
        if (newData == null) {
            return;
        }
        List<ViewModel> newList = new ArrayList<>(newData);
        StickerDiffCallback diffCallback = new StickerDiffCallback(data, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        if (!this.data.isEmpty()) {
            this.data.clear();
        }
        this.data.addAll(newList);

        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        ViewModel model = data.get(position);
        if (model instanceof StickerDb) {
            return ITEM_TYPE_STICKER;
        } else if (data.get(position) instanceof EmptyViewModel) {
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_EMPTY;
    }

    public GridLayoutManager getGridLayoutManager(Context context, int defaultSpanCount) {
        GridLayoutManager lm = new GridLayoutManager(context, defaultSpanCount);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = getItemViewType(position);
                int spanCount;
                if (viewType == ITEM_TYPE_STICKER) {
                    spanCount = 1; // Количество столбцов для элемента типа 1
                } else if (viewType == ITEM_TYPE_EMPTY) {
                    spanCount = 3; // Количество столбцов для элемента типа 2
                } else {
                    spanCount = defaultSpanCount; // Если тип элемента не определен, используйте количество столбцов по умолчанию
                }
                return spanCount;
            }
        });
        return lm;
    }

    public boolean isItemUnlocked(int position) {
        return unlockedItems.contains(position);
    }

    public interface ItemClickListener {
        void onItemClick(StickerDb stickerDb, int position);

        void shareItem(StickerDb sticker);

        //void onFavoriteClickRequest(StickerDb sticker);
    }
}
