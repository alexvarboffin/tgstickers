package com.walhalla.stickers.adapter.empty;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.stickers.databinding.RowEmptyBinding;


public class EmptyViewHolder extends RecyclerView.ViewHolder {

    private final RowEmptyBinding binding;

    public EmptyViewHolder(RowEmptyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object o) {
        EmptyViewModel error = (EmptyViewModel) o;
        if (error != null) {
            binding.tvErrorMsg.setText(error.error);
        }
    }
}