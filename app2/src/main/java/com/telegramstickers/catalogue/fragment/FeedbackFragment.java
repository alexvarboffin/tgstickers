package com.telegramstickers.catalogue.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.net.MailTo;
import androidx.fragment.app.FragmentActivity;

import com.telegramstickers.catalogue.R;

public class FeedbackFragment extends FragmentActivity {
    Button bug;
    Button idea;
    View.OnClickListener onClickButton = new View.OnClickListener() {
        public void onClick(View view) {
            try {
//                switch (view.getId()) {
//                    case R.id.feedback_bug:
//                        FeedbackFragment feedbackFragment = FeedbackFragment.this;
//                        feedbackFragment.startActivity(FeedbackFragment.getSendFeedbackIntent(feedbackFragment.getApplicationContext(), "bug"));
//                        return;
//                    case R.id.feedback_idea:
//                        FeedbackFragment feedbackFragment2 = FeedbackFragment.this;
//                        feedbackFragment2.startActivity(FeedbackFragment.getSendFeedbackIntent(feedbackFragment2.getApplicationContext(), "idea"));
//                        return;
//                    case R.id.feedback_question:
//                        FeedbackFragment feedbackFragment3 = FeedbackFragment.this;
//                        feedbackFragment3.startActivity(FeedbackFragment.getSendFeedbackIntent(feedbackFragment3.getApplicationContext(), "question"));
//                        return;
//                    default:
//                        return;
//                }
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(FeedbackFragment.this.getApplicationContext(), "There are no email applications installed.", 0).show();
            }
        }
    };
    Button question;

    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.feedback);
//        this.bug = findViewById(R.id.feedback_bug);
//        this.idea = findViewById(R.id.feedback_idea);
//        this.question = findViewById(R.id.feedback_question);
//        this.bug.setOnClickListener(this.onClickButton);
//        this.idea.setOnClickListener(this.onClickButton);
//        this.question.setOnClickListener(this.onClickButton);
    }

    public static Intent getSendFeedbackIntent(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        StringBuilder sb = new StringBuilder();
        sb.append(MailTo.MAILTO_SCHEME);
        sb.append(context.getString(R.string.publisher_feedback_email));
        sb.append("?subject=");
        String string = context.getString(R.string.send_feedback_optional_subject);
        if (TextUtils.isEmpty(string)) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1165870106:
                    if (str.equals("question")) {
                        c = 0;
                        break;
                    }
                    break;
                case 97908:
                    if (str.equals("bug")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3227383:
                    if (str.equals("idea")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    sb.append(Uri.encode(String.format(context.getString(R.string.feedback_subject_question), getApplicationName(context))));
                    break;
                case 1:
                    sb.append(Uri.encode(String.format(context.getString(R.string.feedback_subject_bug), getApplicationName(context))));
                    break;
                case 2:
                    sb.append(Uri.encode(String.format(context.getString(R.string.feedback_subject_idea), getApplicationName(context))));
                    break;
            }
        } else {
            sb.append(Uri.encode(string));
        }
        sb.append("&body=");
        String string2 = context.getString(R.string.send_feedback_optional_body);
        if (TextUtils.isEmpty(string2)) {
            sb.append(Uri.encode("\n\n"));
            sb.append(Uri.encode(getFullVersionString(context)));
            sb.append(Uri.encode("\n"));
            sb.append(Uri.encode(getDeviceName()));
            sb.append(Uri.encode("\n"));
            sb.append(Uri.encode(getOsVersion()));
        } else {
            sb.append(Uri.encode(string2));
        }
        intent.setData(Uri.parse(sb.toString()));
        return intent;
    }

    public static String getFullVersionString(Context context) {
        return getVersionInfo(context, R.string.full_version_text_dynamic);
    }

    private static String getVersionInfo(Context context, int i) {
        int i2;
        String str = "";
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            str = packageManager.getPackageInfo(packageName, 0).versionName;
            i2 = packageManager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            i2 = 0;
        }
        return String.format(context.getResources().getString(i), str, Integer.valueOf(i2));
    }

    public static String getDeviceName() {
        String str = Build.MANUFACTURER;
        String str2 = Build.MODEL;
        if (str2.startsWith(str)) {
            return capitalize(str2);
        }
        return capitalize(str) + " " + str2;
    }

    private static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        char charAt = str.charAt(0);
        if (Character.isUpperCase(charAt)) {
            return str;
        }
        return Character.toUpperCase(charAt) + str.substring(1);
    }

    public static String getOsVersion() {
        return "Android " + Build.VERSION.RELEASE + " API Level " + Build.VERSION.SDK_INT;
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo;
        CharSequence charSequence;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            charSequence = packageManager.getApplicationLabel(applicationInfo);
        } else {
            charSequence = context.getString(R.string.this_application);
        }
        return (String) charSequence;
    }
}
