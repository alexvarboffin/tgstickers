package com.walhalla.stickers.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.walhalla.stickers.R;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.stickers.databinding.StickerItemBinding;

import java.util.Set;


public class StickerPackViewHolder extends RecyclerView.ViewHolder {

    private final AbstractStickerAdapter.ItemClickListener mClickListener;

    public final StickerItemBinding binding;
    private final Set<Integer> unlockedItems;

    public StickerPackViewHolder(@NonNull StickerItemBinding binding, AbstractStickerAdapter.ItemClickListener mClickListener1, Set<Integer> unlockedItems) {
        super(binding.getRoot());
        this.binding = binding;
        this.mClickListener = mClickListener1;
        this.unlockedItems = unlockedItems;
    }


    public void bindItem(StickerDb sticker, String thumbnail, AbstractStickerAdapter adapter) {
        binding.stickerName.setText(sticker.name);
        try {
            Glide.with(binding.getRoot().getContext())
                    .load(thumbnail)//StickerUtils.getRandomThumbnail(sticker)
                    //@@@       .centerCrop()
                    .dontTransform()
                    //.crossFade()
                    //fitCenter()
                    //.error(R.drawable.error)
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .listener(new RequestListener<>() {
                        @Override
                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, @Nullable @org.jetbrains.annotations.Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            //DLog.d("@@@@" + thumbnail + "@" + sticker.storage);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })

                    .into(binding.thumbnail);
        }
//        catch (com.bumptech.glide.load.HttpException ignored) {
//        }
        catch (Exception ignored) {
        }

        updateFavorite(sticker);

//        if (isItemUnlocked(getAdapterPosition())) {
//            binding.ivLock.setImageResource(R.drawable.ic_lock);
//            binding.ivLock.setVisibility(View.VISIBLE);
//            binding.ivLock.setOnClickListener(v ->
//                    mClickListener.handleProxyIntent(getAdapterPosition(), data));
//        } else {
//            binding.ivLock.setVisibility(View.GONE);
//        }

        final int position = getAdapterPosition();
        if (isItemUnlocked(position)) {
            binding.favBtn.setVisibility(View.GONE);
            binding.shareBtn.setVisibility(View.GONE);

            binding.ivLock.setImageResource(R.drawable.ic_lock);
            binding.ivLock.setVisibility(View.VISIBLE);
            binding.ivLock.setOnClickListener(v ->
            {
                if (mClickListener != null) mClickListener.onItemClick(sticker, position);
            });
        } else {
            binding.favBtn.setVisibility(View.VISIBLE);
            binding.shareBtn.setVisibility(View.VISIBLE);

            binding.ivLock.setVisibility(View.GONE);
            binding.favBtn.setOnClickListener(v -> {
                //if (mClickListener != null) {
                adapter.onFavoriteClickRequest(this, sticker, position);
                //}
            });
            binding.shareBtn.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.shareItem(sticker);
                }
            });
        }
    }

    public boolean isItemUnlocked(int position) {
        return unlockedItems.contains(position);
    }

    public void updateFavorite(StickerDb sticker) {
        try {
            ImageView imageViewFavorite = binding.favBtn;
            if (sticker.isLiked()) {
                imageViewFavorite.setImageDrawable(ResourcesCompat.getDrawable(imageViewFavorite.getResources(), R.drawable.ic_favorite_true, null));
            } else {
                imageViewFavorite.setImageDrawable(ResourcesCompat.getDrawable(imageViewFavorite.getResources(), R.drawable.ic_favorite_false, null));
            }
        } catch (Exception ignored) {
        }
    }
}