package com.walhalla.telegramstickers.utils

import com.walhalla.stickers.database.StickerDb
import java.util.Collections
import java.util.Random

class StickerUtils {
    // https://tgstickerscatalogue.web.app
    var v0: IntArray = intArrayOf(
        112,
        112,
        97,
        46,
        98,
        101,
        119,
        46,
        101,
        117,
        103,
        111,
        108,
        97,
        116,
        97,
        99,
        115,
        114,
        101,
        107,
        99,
        105,
        116,
        115,
        103,
        116,
        47,
        47,
        58,
        115,
        112,
        116,
        116,
        104
    )


    private val storage0: String
    private val storage1: String

    init {
        storage0 = dec0(v0) + "/"
        storage1 = dec0(v1) + "/"
    }

    fun dec0(intArray: IntArray): String {
        val strArray = CharArray(intArray.size)
        for (i in intArray.indices) {
            strArray[i] = intArray[i].toChar()
        }
        return StringBuilder((String(strArray))).reverse().toString()
    }

    fun getThumbnail(sticker: StickerDb): String {
        return if (sticker.storage == 1) {
            storage1 + sticker.imageSet + "/1.webp"
        } else {
            storage0 + sticker.imageSet + "/1.webp"
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
    fun getAllImages(sticker: StickerDb): MutableList<String> {
        val arrayList: MutableList<String> = ArrayList<String>()
        var total = sticker.numImages
        if (total > 20) {
            total = 20
        }
        for (i in 0..<total) {
            val aa: String?
            if (sticker.storage == 1) {
                aa = storage1 + sticker.imageSet + "/" + i + ".webp"
            } else {
                aa = storage0 + sticker.imageSet + "/" + i + ".webp"
            }
            arrayList.add(aa)
        }
        arrayList.shuffle(Random(System.nanoTime()))
        return arrayList
    }

    companion object {
        // https://bovada-ba3b9.web.app
        var v1: IntArray = intArrayOf(
            112,
            112,
            97,
            46,
            98,
            101,
            119,
            46,
            57,
            98,
            51,
            97,
            98,
            45,
            97,
            100,
            97,
            118,
            111,
            98,
            47,
            47,
            58,
            115,
            112,
            116,
            116,
            104
        )
    }
}
