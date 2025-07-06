package com.walhalla.telegramstickers

import android.content.Context
import com.walhalla.stickers.constants.Constants

class ResourceHelper(context: Context) {
    val data: IntArray

    init {
        this.data = intArrayOf(
            //                R.string.cat_name_animals,
            //                R.string.cat_name_comics,
            //                R.string.cat_name_faces,
            //                R.string.cat_name_movies,
            //                R.string.cat_name_games,
            //                R.string.cat_name_memes,
            //                R.string.cat_name_messages,
            R.string.c_animals,
            R.string.c_art_and_design,
            R.string.c_auto_and_moto,
            R.string.c_books_and_magazine,
            R.string.c_celebrities,
            R.string.c_cryptocurrencies,
            R.string.c_economics_and_finance,
            R.string.c_entertainment,
            R.string.c_fashion_and_beauty,
            R.string.c_food,
            R.string.c_games_and_apps,
            R.string.c_health,
            R.string.c_languages,
            R.string.c_love,
            R.string.c_news_and_media,

            R.string.c_photo,
            R.string.c_science,
            R.string.c_self_development,
            R.string.c_sports_and_fitness,
            R.string.c_technology,
            R.string.c_telegram,
            R.string.c_travel,
            R.string.c_videos_and_movies,  //R.string.cat_name_others,

            R.string.c_other,  //R.string.cat_privacy_policy
        )
    }

    fun categories(): IntArray {
        return data
    }


    fun isMenuPressed(id: Int): Boolean {
        var result = false
        for (i in data) {
            if (i == id) {
                result = true
                break
            }
        }
        //DLog.d("menu-pressed: " + id + " " + result);
        return result
    }

    fun toType(resourceId: Int): Int {
        var position = Constants.D_ALL
        for (i in data.indices) {
            val aa = data[i]
            if (aa == resourceId) {
                position = i
                break
            }
        }
        //return (position == 0) ? Constants.D_ALL : position;
        return position
    }

    fun titleV(c_id: Int): Int {
        if (c_id == Constants.D_FAVORITE) {
            return R.string.title_favorite_statuses
        } else if (c_id == Constants.D_ALL) {
            return R.string.dictionary_all
        } else if (c_id < 0 || c_id > data.size - 1) {
            return c_id
        }
        return data[c_id]
    }

    companion object {
        private var instance: ResourceHelper? = null

        @JvmStatic
        fun getInstance(context: Context): ResourceHelper {
            if (instance == null) {
                instance = ResourceHelper(context)
            }
            return instance!!
        }
    }
}
