package com.walhalla.stickers.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.ui.DLog;



public class LocalDatabaseRepo {

    private static AppDatabase database;
    static final Migration MIGRATION_1_2 = new Migration(1, 43) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Поскольку мы не изменяли таблицу, здесь больше ничего не нужно делать.
            DLog.d("===================================");
        }
    };

    //private CategoryDao dao;

    private static final Object LOCK = new Object();

    private static final RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {

//                    @Override
//                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                        super.onCreate(db);
//
//                        Executors.newSingleThreadExecutor().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                getInstance().getDatabase().questionDao().init();
//                            }
//                        });
//
//                    }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public synchronized static AppDatabase getDatabase(Context context) {
        if (database == null) {
            synchronized (LOCK) {
                if (database == null) {
                    database = Room.databaseBuilder(context, AppDatabase.class,
                                    "electroclash2.db" //+ (char) 0x200B
                            )
                            //@@@.createFromAsset("info/info.ttf")
                            .allowMainThreadQueries()
                            //.addMigrations(MIGRATION_1_2)
                            .addCallback(dbCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return database;
    }

}
