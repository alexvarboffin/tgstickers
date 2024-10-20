package com.walhalla.stickers.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.walhalla.stickers.AppDatabase;
import com.walhalla.stickers.database.LocalDatabaseRepo;
import com.walhalla.stickers.database.StickerDb;
import com.walhalla.ui.DLog;

import java.util.List;

public abstract class StickersPresenter {

    protected static final String KEY_VALID_DATA = "up_success";
    protected final SharedPreferences sharedPreferences;
    private final int timeToRefresh0 = 60;//60 min in hour
    protected final long ONE_HOUR = timeToRefresh0 * 60;
    protected static final String KEY_DATE = "daterefresh0";

    public StickersPresenter(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    protected boolean isDelayTimeout() {

        boolean dataSetSuccess = sharedPreferences.getBoolean(KEY_VALID_DATA, false);
        if (dataSetSuccess) {
            return false;
        }

        long currentTime = System.currentTimeMillis() / 1000;//current time in sec
        long newTimeWithDelay = currentTime + (ONE_HOUR);//+1 hour
        if (!sharedPreferences.contains(KEY_DATE)) {
            sharedPreferences.edit().putLong(KEY_DATE, newTimeWithDelay).apply();
        }
        newTimeWithDelay = sharedPreferences.getLong(KEY_DATE, newTimeWithDelay);//new time

//        DLog.d("{@@@}" + (newTimeWithDelay < currentTime) + " ");
//        DLog.d("{@@@}" + newTimeWithDelay + ", " + currentTime + " ");
//        DLog.d("{@@@}" + ((newTimeWithDelay - currentTime)) / 60 + " ");


        //return false;// Disable Timeout
        //Enabled ==>
        return newTimeWithDelay < currentTime;
    }

}
