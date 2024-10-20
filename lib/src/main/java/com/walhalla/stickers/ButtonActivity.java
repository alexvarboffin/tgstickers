package com.walhalla.stickers;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.walhalla.stickers.adapter.ButtonAdapter;
import com.walhalla.stickers.adapter.ColumnAdapter;
import com.walhalla.stickers.adapter.ContentItem;
import com.walhalla.stickers.adapter.HeaderItem;
import com.walhalla.stickers.adapter.Item;

import java.util.ArrayList;
import java.util.List;

public class ButtonActivity extends AppCompatActivity
        implements ButtonAdapter.ButtonClickListener {

    private RecyclerView recyclerView;
    private ButtonAdapter adapter;
    private final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private final String[] headers = {"A", "B", "C", "D"};

    private String[][] content = {
            {"Naruto", "One Piece", "Attack on Titan"},
            {"aaa", "aaaa", "sxxx"},
            {"Lion", "Elephant", "Tiger"},{"Lion", "Elephant", "Tiger"}
    };
    private ColumnAdapter columnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new ButtonAdapter(this, letters, this);
        recyclerView.setAdapter(adapter);


        RecyclerView secondRecyclerView = findViewById(R.id.secondRecyclerView);
        secondRecyclerView.setLayoutManager(new GridLayoutManager(this, headers.length));
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < headers.length; i++) {
            items.add(new HeaderItem(headers[i]));
            for (String item : content[i]) {
                items.add(new ContentItem(item));
            }
        }
        columnAdapter = new ColumnAdapter(this, items);
        secondRecyclerView.setAdapter(columnAdapter);
    }

    @Override
    public void onButtonClick(String letter) {
        Toast.makeText(this, "Clicked: " + letter, Toast.LENGTH_SHORT).show();
        // Добавьте здесь вашу логику для обработки нажатия на кнопку
    }
}

