package com.walhalla.stickers.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.walhalla.stickers.databinding.QrLayoutBinding;

public class QRCodeDialog extends DialogFragment {


    private static final String ARG_URL = "arg_qr_code_text";
    private static final String ARG_TITLE = "arg_qr_code_title";

    private String url;
    private Bitmap bitmap;
    private QrLayoutBinding binding;
    private String title;


    public QRCodeDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.url = getArguments().getString(ARG_URL);
            this.title = getArguments().getString(ARG_TITLE);
        }
    }

    public static QRCodeDialog newInstance(String code, String title) {
        QRCodeDialog fragment = new QRCodeDialog();
        Bundle arg = new Bundle();
        arg.putString(ARG_URL, code);
        arg.putString(ARG_TITLE, title);
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = QrLayoutBinding.inflate(inflater, container, false);
        bitmap = net.glxn.qrgen.android.QRCode.from(url)
                //.withColor(0xFFFF0000, 0xFFFFFFAA)
                .bitmap();
        binding.imageView.setImageBitmap(bitmap);
        binding.imageView.setOnClickListener(v -> this.dismiss());
        binding.qrTitle.setText(title);
        return binding.getRoot();
    }


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Dialog mm = super.onCreateDialog(savedInstanceState);
//        mm.setTitle("@@@@");
//        return mm;
//    }
//     .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel())

}
