package com.walhalla.telegramstickers.utils;

import com.walhalla.stickers.database.StickerDb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StickerUtils {

    // https://tgstickerscatalogue.web.app
    public int[] v0 = new int[]{112, 112, 97, 46, 98, 101, 119, 46, 101, 117, 103, 111, 108, 97, 116, 97, 99, 115, 114, 101, 107, 99, 105, 116, 115, 103, 116, 47, 47, 58, 115, 112, 116, 116, 104};


    // https://bovada-ba3b9.web.app
    public static int[] v1 = new int[]{112, 112, 97, 46, 98, 101, 119, 46, 57, 98, 51, 97, 98, 45, 97, 100, 97, 118, 111, 98, 47, 47, 58, 115, 112, 116, 116, 104};

    private final String storage0;
    private final String storage1;

    public StickerUtils() {
        storage0 = dec0(v0) + "/";
        storage1 = dec0(v1) + "/";

    }

    public String dec0(int[] intArray) {
        char[] strArray = new char[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            strArray[i] = (char) intArray[i];
        }
        return new StringBuilder((String.valueOf(strArray))).reverse().toString();
    }

    public String getThumbnail(StickerDb sticker) {
        if (sticker.storage == 1) {
            return storage1 + sticker.imageSet + "/1.webp";
        } else {
            return storage0 + sticker.imageSet + "/1.webp";
        }
    }

//    public static String getRandomThumbnail(StickerDb sticker) {
//        int total = Integer.parseInt(sticker.numImages);
//        if (total > 20) {
//            total = 20;
//        }
//        int rand = (new Random().nextInt((total - 1) + 1) + 1);
//        //return base0 + imageSet + "/" + rand + ".jpg";
//        return base0 + sticker.imageSet + "/" + rand + ".webp";
//    }

    public List<String> getAllImages(StickerDb sticker) {
        List<String> arrayList = new ArrayList<>();
        int total = sticker.numImages;
        if (total > 20) {
            total = 20;
        }
        for (int i = 0; i < total; i++) {
            String aa;
            if (sticker.storage == 1) {
                aa = storage1 + sticker.imageSet + "/" + i + ".webp";
            } else {
                aa = storage0 + sticker.imageSet + "/" + i + ".webp";
            }
            arrayList.add(aa);
        }
        Collections.shuffle(arrayList, new Random(System.nanoTime()));
        return arrayList;
    }
}
