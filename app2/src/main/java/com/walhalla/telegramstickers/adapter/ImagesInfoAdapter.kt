package com.walhalla.telegramstickers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.telegramstickers.catalogue.R
import com.telegramstickers.catalogue.databinding.ItemStickerImagesBinding
import com.walhalla.abcsharedlib.Share.makeImageShare
import com.walhalla.stickers.utils.CoreUtil
import androidx.core.graphics.createBitmap
import com.walhalla.stickers.adapter.shareImage1

class ImagesInfoAdapter
    (
    private val context: Context,
    private val data: MutableList<String>,
    private val stickerPackName: String?,
    private val clb: ImAdapterCallback?
) : RecyclerView.Adapter<RecordHolder1?>() {
    interface ImAdapterCallback {
        fun saveImageRequest(resource: String)
    }

    private var popup: PopupMenu? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder1 {
        val inflater = LayoutInflater.from(context)
        val view = ItemStickerImagesBinding.inflate(inflater, parent, false)
        return RecordHolder1(view)
    }

    override fun onBindViewHolder(holder: RecordHolder1, position: Int) {
        val imageUrl = this.data.get(position)
        try {
            Glide.with(this.context)
                .load(imageUrl)
                .centerCrop() //.crossFade()
                .dontTransform() //.crossFade()
                //fitCenter()
                //.error(R.drawable.error)
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        //DLog.d("@@@@" + thumbnail + "@" + sticker.storage);
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(holder.binding.stickerImage)

            //            Glide.with(context)
//                    .asBitmap()
//                    .load(imageUrl)
//                    .placeholder(R.drawable.loading_animation)
//                                        .error(R.drawable.ic_broken_image)
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
        } catch (ignored: Exception) {
        }


        holder.binding.overflowMenu.setOnClickListener(View.OnClickListener { view: View? ->
            showPopupMenu(
                view!!, imageUrl, holder.binding.stickerImage
            )
        })
    }


    private fun showPopupMenu(view: View, resource: String?, stickerImage: ImageView) {
        popup = PopupMenu(view.context, view)
        val inflater = popup!!.menuInflater
        val menu = popup!!.menu
        inflater.inflate(R.menu.item_sticker, menu)
        try {
            @SuppressLint("DiscouragedPrivateApi")
            val fMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
            fMenuHelper.isAccessible = true
            val menuHelper = fMenuHelper.get(popup)
            val argTypes = arrayOf(Boolean::class.javaPrimitiveType)
            menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", *argTypes)
                .invoke(menuHelper, true)
        } catch (e: Exception) {
            // обработка ошибки (можно залогировать или проигнорировать)
        }
        popup!!.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem: MenuItem? ->
            val itemId = menuItem!!.itemId
            if (itemId == R.id.menu_share_image) {
                if (popup != null) {
                    popup!!.dismiss()
                }
                shareImage1(context, resource, stickerImage, fileNamePrefix())
            } else
                if (itemId == R.id.menu_save_image) {
                    if (clb != null) {
                        clb.saveImageRequest(resource?:"")
                    }
                }
            false
        })
        popup!!.show()
    }


    private fun fileNamePrefix(): String {
        return "sticker_"
    }


    override fun getItemCount(): Int {
        return data.size
    }
}
