package com.walhalla.stickers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.stickers.R;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

    private Context context;
    private String[] letters;
    private ButtonClickListener buttonClickListener;

    public ButtonAdapter(Context context, String[] letters, ButtonClickListener buttonClickListener) {
        this.context = context;
        this.letters = letters;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_button, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        holder.bind(letters[position]);
    }

    @Override
    public int getItemCount() {
        return letters.length;
    }

    class ButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            button.setOnClickListener(this);
        }

        public void bind(String letter) {
            button.setText(letter);
        }

        @Override
        public void onClick(View v) {
            buttonClickListener.onButtonClick(button.getText().toString());
        }
    }

    public interface ButtonClickListener {
        void onButtonClick(String letter);
    }
}

