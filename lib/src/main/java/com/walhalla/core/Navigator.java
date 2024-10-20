package com.walhalla.core;

import androidx.annotation.Keep;

//import com.walhalla.view.adapter.ViewModel;

import java.io.Serializable;

@Keep
public class Navigator implements Serializable//, ViewModel
{

    public final Integer icon;
    public int id;
    public String tag;

    public Navigator(int id, String tag, Integer icon) {
        this.id = id;
        this.tag = tag;
        this.icon = icon;
    }

//    @Override
//    public int getItemType() {
//        return 348;
//    }

    //public Nav() {}
}
