package com.walhalla.telegramstickers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.walhalla.stickers.adapter.AbstractStickerAdapter
import com.walhalla.stickers.adapter.StickerPackViewHolder
import com.walhalla.stickers.adapter.ViewModel
import com.walhalla.stickers.adapter.empty.EmptyViewHolder
import com.walhalla.stickers.database.StickerDao
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.databinding.RowEmptyBinding
import com.walhalla.stickers.databinding.StickerItemBinding
import com.walhalla.telegramstickers.utils.StickerUtils
import com.walhalla.ui.DLog.d

class StickerAdapter : AbstractStickerAdapter {
    private val context: Context?
    private val m: StickerUtils

    constructor(
        context: Context?,
        unlockedItems: MutableSet<Int?>?,
        dao: StickerDao?,
        list: MutableList<ViewModel?>?,
        isFavorite: Boolean
    ) : super(context, unlockedItems, dao, isFavorite) {
        this.context = context
        this.data = list
        m = StickerUtils()
    }

    constructor(
        context: Context?,
        unlockedItems: MutableSet<Int?>?,
        dao: StickerDao?,
        isFavorite: Boolean
    ) : super(context, unlockedItems, dao, isFavorite) {
        this.context = context
        this.data = ArrayList<ViewModel?>()
        m = StickerUtils()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == ITEM_TYPE_STICKER) {
            val binding = StickerItemBinding.inflate(inflater, parent, false)
            return StickerPackViewHolder(binding, mClickListener, unlockedItems)
        }
        //case VIEW_TYPE_EMPTY:
        val view1 = RowEmptyBinding.inflate(inflater, parent, false)
        return EmptyViewHolder(view1)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        d("@@@" + position)

        if (holder.getItemViewType() == ITEM_TYPE_STICKER) {
            val sticker = this.data.get(position) as StickerDb
            val h0 = (holder as StickerPackViewHolder)
            h0.bindItem(sticker, m.getThumbnail(sticker), this)
            //holder.binding.stickerName.setText(sticker.name+"@"+sticker.category);
            h0.itemView.setOnClickListener(View.OnClickListener { v9: View? ->
                if (mClickListener != null) mClickListener.onItemClick(sticker, position)
            })
        } else {
            val vh2 = holder as EmptyViewHolder
            vh2.bind(data[position])
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    fun getPositionFromData(character: String?): Int {
        var position = 0
        for (viewModel in data) {
            if (viewModel is StickerDb) {
                val letter = "" + viewModel.link[0]
                if (letter.equals("" + character, ignoreCase = true)) {
                    return position
                }
                position++
            }
        }
        return 0
    }
}
