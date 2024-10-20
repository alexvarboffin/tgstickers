package com.walhalla.telegramstickers.utils;

import com.walhalla.stickers.database.StickerDb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StickerUtils {

    private final String base0 =
            //"http://fastdl.mapeadores.com/mapercsgozombiemod/telegramstickers/images/";
            "https://tgstickers-dbce8.web.app/v2/";


    public String getThumbnail(StickerDb sticker) {
        //return base0 + imageSet + "/1.jpg";
        return base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_1.png";
    }

//    public String getRandomThumbnail(StickerDb sticker) {
//        int r = (new Random().nextInt((Integer.parseInt(sticker.numImages) - 1) + 1) + 1);
//        //return base0 + imageSet + "/" + r + ".jpg";
//        return base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_" + r + ".png";
//    }

    public List<String> getAllImages(StickerDb sticker) {
        List<String> arrayList = new ArrayList<>();
        int total = sticker.numImages;
        for (int i = 1; i <= total; i++) {
            //String aa = base0 + imageSet + "/" + i + ".jpg";
            String aa = base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_" + i + ".png";
            arrayList.add(aa);
        }
        Collections.shuffle(arrayList, new Random(System.nanoTime()));
        return arrayList;
    }
}
