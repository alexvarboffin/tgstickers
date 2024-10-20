package com.telegramstickers.catalogue.activity;

import android.preference.PreferenceActivity;
import com.telegramstickers.catalogue.fragment.PrefsFragment;
import java.util.List;

public class PrefsActivity extends PreferenceActivity {
    @Override // android.preference.PreferenceActivity
    public void onBuildHeaders(List<Header> list) {
        super.onBuildHeaders(list);
        getFragmentManager().beginTransaction().replace(16908290, new PrefsFragment()).commit();
    }
}
