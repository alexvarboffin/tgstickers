package com.walhalla.alphabetick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.walhalla.stickers.R;
import com.walhalla.stickers.databinding.ItemLetterBinding;

import java.util.Arrays;

/**
 * Created by ricardo.montesinos on 6/22/17.
 */

public class Alphabetik extends RecyclerView {

    //Default Alphabet
    private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private float fontSize;
    private int selectedFontSize;
    private static int itemsColor;
    private int selectedItemColor;
    private int selectedItemBackground;

    //Adapter & Manager
    private SectionIndexAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public Alphabetik(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        //this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        getAttributes(context, attrs);
        initRecyclerView();
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Alphabetik);

        //Custom sizes
        //Attributes
        int width = ta.getDimensionPixelSize(R.styleable.Alphabetik_width, 15);

        int defaultSize = (int) spToPixel(context, 12);
        int attFontSizeValue = ta.getDimensionPixelSize(R.styleable.Alphabetik_fontSize, defaultSize);
        fontSize = pixelsToSp(context, attFontSizeValue);

        //Custom colors
        //Items Color
        int aItemsColor = R.styleable.Alphabetik_itemsColor;
        if (ta.hasValue(R.styleable.Alphabetik_itemsColor)) {
            itemsColor = getColor(ta.getResourceId(aItemsColor, 0));
        }

        //TODO
        //Selected Item Color
        /*int aSelectedItemColor = R.styleable.Alphabetik_selectedItemColor;
        if (ta.hasValue(aSelectedItemColor)) {
            selectedItemColor = ta.getResourceId(aSelectedItemColor, 0);
        }

        //Selected Item Background
        int aSelectedItemBackground = R.styleable.Alphabetik_selectedItemBackground;
        if (ta.hasValue(aSelectedItemBackground)) {
            selectedItemBackground = ta.getResourceId(aSelectedItemBackground, 0);
        }*/

        //Recycle
        ta.recycle();
    }

    private int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    private float pixelsToSp(Context context, int px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    private float spToPixel(Context context, int sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    private void initRecyclerView() {
        adapter = new SectionIndexAdapter(alphabet, getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), VERTICAL, false);
        this.setHasFixedSize(true);
        this.setAdapter(adapter);
        this.setLayoutManager(linearLayoutManager);
    }

    /**
     * Setter method. Set a custom alphabet, this method sort it automatically.
     *
     * @param {String} array of characters, e.g. "A", "B", "C"...
     * @method setAlphabet
     */
    public void setAlphabet(String[] alphabet) {
        Arrays.sort(alphabet);
        this.alphabet = alphabet;
        initRecyclerView();
    }

    //LISTENER
    public void onSectionIndexClickListener(SectionIndexClickListener sectionIndexClickListener) {
        adapter.onSectionIndexClickListener(sectionIndexClickListener);
    }

    //Fast Alphabet generation From A-Z
    private String[] generateAlphabet() {
        String[] alphabetTemp = new String[27];
        int index = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            alphabetTemp[index] = "" + c;
            index++;
        }
        return alphabetTemp;
    }

    /**
     * Set letter to bold
     *
     * @param {String} "letter"
     * @method setLetterToBold
     */
    public void setLetterToBold(String letter) {
        int index = Arrays.asList(alphabet).indexOf(letter);
        String regex = "[0-9]+";
        if (letter.matches(regex)) {
            index = alphabet.length - 1;
        }
        adapter.setBoldPosition(index);
        this.linearLayoutManager.scrollToPositionWithOffset(index, 0);
        if (this.getAdapter() != null) {
            this.getAdapter().notifyDataSetChanged();
        }
    }

    //ADAPTER
    class SectionIndexAdapter extends Adapter<SectionIndexAdapter.ViewHolder> {

        private int boldPosition = 0;
        private final String[] alphabet;
        private final LayoutInflater mInflater;
        private SectionIndexClickListener sectionIndexClickListener;

        SectionIndexAdapter(String[] alphabet, Context context) {
            this.alphabet = alphabet;
            this.mInflater = LayoutInflater.from(context);
        }

        //LISTENER
        public void onSectionIndexClickListener(SectionIndexClickListener sectionIndexClickListener) {
            this.sectionIndexClickListener = sectionIndexClickListener;
        }

        public void setBoldPosition(int position) {
            this.boldPosition = position;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            @NonNull ItemLetterBinding binding = ItemLetterBinding.inflate(mInflater, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String letter = alphabet[position];
            holder.binding.tvLetter.setText(letter);
            //Set current position to bold
            Typeface normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            holder.binding.tvLetter.setTypeface(position == boldPosition ? boldTypeface : normalTypeface);

            //Custom Font size
            holder.binding.tvLetter.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

            //Custom color
            if (itemsColor != 0) {
                holder.binding.tvLetter.setTextColor(itemsColor);
            }
        }

        @Override
        public int getItemCount() {
            return alphabet.length;
        }

        //VIEW HOLDER
        class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

            private final ItemLetterBinding binding;

            public ViewHolder(ItemLetterBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
                binding.tvLetter.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (sectionIndexClickListener != null) {
                    String character = "" + binding.tvLetter.getText().toString();
                    sectionIndexClickListener.onItemClick(view, this.getPosition(), character);
                    setLetterToBold(character);
                }
            }
        }
    }

    //INTERFACES
    public interface SectionIndexClickListener {
        void onItemClick(View view, int position, String character);
    }
}
