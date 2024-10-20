package com.walhalla.stickers.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StickerDao {
    @Insert
    void insertSticker(StickerDb sticker);

    @Update
    int update(StickerDb question);

    @Delete
    void deleteSticker(StickerDb sticker);

    @Query("SELECT * FROM stickers WHERE _id = :id")
    StickerDb getSticker(long id);

    @Query("SELECT * FROM stickers WHERE name = :name")
    StickerDb getSticker(String name);

    //    @Query("SELECT * FROM stickers")
//    List<StickerDb> getAllStickers();
    @Query("SELECT * FROM stickers ORDER BY _id ASC")
    List<StickerDb> getAllStickers();

    @Query("SELECT * FROM stickers WHERE category = :category")
    List<StickerDb> getCategoryStickers(String category);


    //delete all rows in a table without deleting the table
    @Query("DELETE FROM stickers")
    void deleteAllStickers();

    @Query("SELECT COUNT(*) FROM stickers")
    int countStickers();


    @Query("SELECT * FROM stickers WHERE liked>0")
    List<StickerDb> getFavorite();
}
