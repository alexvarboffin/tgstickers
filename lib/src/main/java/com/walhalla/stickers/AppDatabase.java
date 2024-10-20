package com.walhalla.stickers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.walhalla.stickers.database.StickerDao;
import com.walhalla.stickers.database.StickerDb;

@Database(entities = {StickerDb.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StickerDao stickerDao();
}