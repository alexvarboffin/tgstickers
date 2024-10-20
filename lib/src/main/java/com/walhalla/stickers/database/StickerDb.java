package com.walhalla.stickers.database;


import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.walhalla.stickers.adapter.ViewModel;

import java.util.Objects;

@Keep
@Entity(tableName = "stickers")
public class StickerDb implements ViewModel {


    public long get_id() {
        return _id;
    }

    @PrimaryKey
    public long _id;
    public String name;
    public String author;
    public String category;
    public String dateAdded;
    public int numImages;
    public int storage;

    public long getId() {
        return _id;
    }


    public String imageSet; //image location
    public String link; //packset url

    @ColumnInfo(name = "liked")
    public int liked = 0;


    public StickerDb() {
    }

    public StickerDb(int _id, String name, String author, String imageSet,
                     int numImages, String category, String link, String createAt, int storage) {
        this._id = _id;
        this.name = name;
        this.author = author;
        this.imageSet = imageSet;
        this.numImages = numImages;
        this.category = category;
        this.link = link;
        this.dateAdded = createAt;
        this.storage = storage;
    }

    public StickerDb(int _id, String name, String author, String imageSet,
                     int numImages, String category, String link, String createAt) {
        this(_id, name, author, imageSet, numImages, category, link, createAt, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StickerDb stickerDb = (StickerDb) o;
        return Objects.equals(imageSet, stickerDb.imageSet) && Objects.equals(link, stickerDb.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageSet, link);
    }

    @Override
    public int getItemType() {
        return 134;
    }



    public void setLiked() {
        liked = 1;
    }


    public void setDisLiked() {
        liked = 0;
    }


    public boolean isLiked() {
        return liked == 1;
    }
}
