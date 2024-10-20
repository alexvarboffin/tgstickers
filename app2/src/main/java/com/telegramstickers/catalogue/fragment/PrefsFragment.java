package com.telegramstickers.catalogue.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PrefsFragment extends PreferenceFragment {
    private Context myContext;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Activity activity = getActivity();
        this.myContext = activity;
        //activity.setTheme(R.style.MyPreferenceTheme);
        //addPreferencesFromResource(R.xml.preferences);
    }
}
