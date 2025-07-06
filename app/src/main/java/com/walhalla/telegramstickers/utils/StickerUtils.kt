package com.walhalla.telegramstickers.utils

import com.walhalla.stickers.database.StickerDb
import java.util.Collections
import java.util.Random

class StickerUtils {
    private val base0 =
        //"http://fastdl.mapeadores.com/mapercsgozombiemod/telegramstickers/images/";
        "https://tgstickers-dbce8.web.app/v2/"


    fun getThumbnail(sticker: StickerDb): String {
        //return base0 + imageSet + "/1.jpg";
        return base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_1.png"
    }

    //    public String getRandomThumbnail(StickerDb sticker) {
    //        int r = (new Random().nextInt((Integer.parseInt(sticker.numImages) - 1) + 1) + 1);
    //        //return base0 + imageSet + "/" + r + ".jpg";
    //        return base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_" + r + ".png";
    //    }
    fun getAllImages(sticker: StickerDb): MutableList<String?> {
        val arrayList: MutableList<String?> = ArrayList<String?>()
        val total = sticker.numImages
        for (i in 1..total) {
            //String aa = base0 + imageSet + "/" + i + ".jpg";
            val aa = base0 + sticker.imageSet + "/big_" + sticker.imageSet + "_" + i + ".png"
            arrayList.add(aa)
        }
        arrayList.shuffle(Random(System.nanoTime()))
        return arrayList
    }
}
