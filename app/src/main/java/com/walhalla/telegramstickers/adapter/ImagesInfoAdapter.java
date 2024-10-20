package com.walhalla.telegramstickers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import com.walhalla.abcsharedlib.Share;
import com.walhalla.telegramstickers.R;
import com.walhalla.telegramstickers.databinding.ItemStickerImagesBinding;
import com.walhalla.telegramstickers.utils.CoreUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class ImagesInfoAdapter
        extends RecyclerView.Adapter<RecordHolder1> {


    private final ImAdapterCallback clb;

    public interface ImAdapterCallback {

        void saveImageRequest(String resource);
    }

    private final Context context;
    private final String stickerPackName;

    private List<String> data;
    private PopupMenu popup;


    public ImagesInfoAdapter(Context context2, List<String> arrayList, String stickerPackName, ImAdapterCallback clb) {
        this.context = context2;
        this.data = arrayList;
        this.stickerPackName = stickerPackName;
        this.clb = clb;
    }

    @NonNull
    @Override
    public RecordHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @NonNull ItemStickerImagesBinding view = ItemStickerImagesBinding.inflate(inflater, parent, false);
        return new RecordHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHolder1 holder, int position) {
        String imageUrl = this.data.get(position);
        try {
            Glide.with(this.context)
                    .load(imageUrl)
                    .centerCrop()
                    //.crossFade()
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
                    .into(holder.binding.stickerImage);

//            Glide.with(context)
//                    .asBitmap()
//                    .load(imageUrl)
//                    .error(R.drawable.ic_broken_image)
//                    .listener(new RequestListener<>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, @Nullable @org.jetbrains.annotations.Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
//                            //DLog.d("@@@@" + thumbnail + "@" + sticker.storage);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
//                    .into(new CustomTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
//                            holder.binding.stickerImage.setImageBitmap(resource);
//                            //sharedBitmap = resource;
//                        }
//
//                        @Override
//                        public void onLoadCleared(Drawable placeholder) {
//                            // Not required
//                        }
//                    });

        } catch (Exception ignored) {
        }


        holder.binding.overflowMenu.setOnClickListener(view ->
                showPopupMenu(view, imageUrl, holder.binding.stickerImage));
    }


    private void showPopupMenu(View view, String resource, ImageView stickerImage) {
        popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();
        inflater.inflate(R.menu.item_sticker, menu);
        Object menuHelper;
        Class<?>[] argTypes;
        try {
            @SuppressLint("DiscouragedPrivateApi") Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
        }
        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.menu_share_image) {
                shareImage(context, resource, stickerImage);
            } else if (itemId == R.id.menu_save_image) {
                if (clb != null) {
                    clb.saveImageRequest(resource);
                }
            }
            return false;
        });
        popup.show();
    }


    private void shareImage(Context context, String resource, ImageView parent) {
        if (popup != null) {
            popup.dismiss();
        }
        //showWatermark(tv_quotes_watermark, tools);
        Bitmap bitmap = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        parent.draw(canvas);

        File file = CoreUtil.makeLocalExportFileName(context, fileNamePrefix());
        Uri uri = CoreUtil.getLocalBitmapUri(context, file, bitmap);

        //hideWatermark(tv_quotes_watermark, tools);
        String appName = context.getString(R.string.app_name);

        String extra = "hello";
//        if (QTextUtils.isAuthorNotEmpty(status.getAuthor())) {
//            extra = extra + "\n" + "— " + status.getAuthor() + "\n" + appName;
//        }

        Intent intent = Share.makeImageShare(extra);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, appName);
        //intent.putExtra(Intent.EXTRA_TITLE, appName);

        //BugFix
        //java.lang.SecurityException: Permission Denial: reading androidx.core.content.FileProvider
        Intent chooser = Intent.createChooser(intent, appName);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        context.startActivity(chooser);
        //Toast.makeText(getActivity(), share_as_image, Toast.LENGTH_SHORT).show();
    }

    private String fileNamePrefix() {
        return "sticker_";
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

}
