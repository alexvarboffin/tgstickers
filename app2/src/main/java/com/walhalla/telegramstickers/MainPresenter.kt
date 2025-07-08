package com.walhalla.telegramstickers

import android.content.Context
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.telegramstickers.catalogue.R
import com.telegramstickers.catalogue.activity.main.MainActivity
import com.walhalla.stickers.FirebaseKeys
import com.walhalla.stickers.constants.Constants
import com.walhalla.stickers.database.LocalDatabaseRepo
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.network.JSONParser
import com.walhalla.stickers.presenter.StickersPresenter
import com.walhalla.stickers.utils.NetworkUtils.isNetworkConnected
import com.walhalla.ui.DLog.d
import com.walhalla.ui.DLog.handleException
import org.json.JSONObject
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.content.edit

class MainPresenter(private val context: Context, handler: Handler, activity: MainActivity?) :
    StickersPresenter(
        context
    ) {
    private fun dec0(v: CharArray?): String {
        return StringBuilder((String(v!!))).reverse().toString()
    }

    val url_all_stickers: String
    private val mView: MainActivity?
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val handler: Handler


    init {
        this.url_all_stickers = "https://islamicwallpapers-1c667.web.app/json.json"//dec0(v0)
        this.mView = activity
        this.handler = handler
    }

    val isRunning: Boolean
        get() = !executor.isShutdown // Проверяем, работает ли ExecutorService

    fun stop() {
        executor.shutdown() // Останавливаем ExecutorService
    }

    fun execute() {
        if (!this.isRunning) {
        }
        d("@@@@" + this.isRunning)

        executor.execute(Runnable {
            var totalStickersLength = 0
            val stickersArray: MutableList<StickerDb> = ArrayList()
            val jParser = JSONParser()
            val makeHttpRequest =
                jParser.makeHttpRequest(url_all_stickers, "GET", HashMap<Any?, Any?>())
            if (makeHttpRequest == null) {
                //DLog.d("All Stickers: @@@@@@");
                onPostExecute(stickersArray, totalStickersLength)
                return@Runnable
            }
            try {
                if (isValid(makeHttpRequest)) {
                    val stickers = makeHttpRequest.getJSONArray("stickers")
                    totalStickersLength = stickers.length()
                    for (i in 0..<totalStickersLength) {
                        val `object` = stickers.getJSONObject(i)

                        var author = ""
                        var imageset = ""
                        var link = ""
                        var created_at = ""
                        try {
                            author = `object`.getString("author")
                        } catch (ignored: Exception) {
                        }

                        try {
                            link = `object`.getString("link")
                        } catch (ignored: Exception) {
                        }

                        var count = 0
                        if (`object`.has("count")) {
                            count = `object`.getInt("count")
                        }
                        var storage = 0
                        if (`object`.has("storage")) {
                            storage = `object`.getInt("storage")
                        }
                        var categoryName = ""
                        val tmp = dec0(FirebaseKeys.KEY_CATEGORY)
                        if (`object`.has(tmp)) {
                            categoryName = `object`.getString(tmp).lowercase(Locale.getDefault())
                        }
                        try {
                            if (`object`.has(Constants.KEY_IMAGESET)) {
                                imageset = `object`.getString(Constants.KEY_IMAGESET)
                            } else {
                                imageset = link
                            }
                        } catch (ignored: Exception) {
                            imageset = link
                        }
                        try {
                            created_at = `object`.getString(Constants.TAG_CREATED_AT)
                        } catch (ignored: Exception) {
                        }
                        stickersArray.add(
                            StickerDb(
                                i + 1,  //object.getInt(Constants.TAG_PID),
                                `object`.getString("name"),
                                author,
                                imageset,
                                count,
                                categoryName,
                                link,
                                created_at, storage
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
            onPostExecute(stickersArray, totalStickersLength)
        })
    }


    private fun onPostExecute(arrayList: MutableList<StickerDb>, totalStickersLength: Int) {
        sharedPreferences.edit {
            putLong(KEY_DATE, (System.currentTimeMillis() / 1000) + ONE_HOUR)
        }
        if (mView != null) {
            handler.post {
                if (!arrayList.isEmpty()) {
                    val db = LocalDatabaseRepo.getDatabase(context)
                    db.stickerDao().deleteAllStickers()
                    val total = arrayList.size
                    for (i in 0..<total) {
                        val item = arrayList[i]
                        //if (!db.checkSticker(item.name)) {
                        db.stickerDao().insertSticker(item)
                        //}
                    }
                    //db--close();
                    //DLog.d("[@@ isValidData @@]" + (total == totalStickersLength));
                    if (total == totalStickersLength && totalStickersLength > 0) {
                        sharedPreferences.edit { putBoolean(KEY_VALID_DATA, true) }
                    }
                }
                mView.successResult00(arrayList)
            }
        }
    }

    @Throws(Exception::class)
    private fun isValid(makeHttpRequest: JSONObject?): Boolean {
        //return makeHttpRequest.getInt("success") == 1;
        return true
    }

    fun onCreateEvent() {
        val db = LocalDatabaseRepo.getDatabase(context)
        val tmp = db.stickerDao().getAllStickers()
        if (isDelayTimeout() || (tmp != null && tmp.isEmpty())) {
            //db--close();
            if (isNetworkConnected(context)) {
                mView!!.refreshDB()
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(context.getString(R.string.no_internet))
                builder.show()
            }
        } else {
            //db--close();
        }
    }

    fun dec0(intArray: IntArray): String {
        val strArray = CharArray(intArray.size)
        for (i in intArray.indices) {
            strArray[i] = intArray[i].toChar()
        }
        return StringBuilder((String(strArray))).reverse().toString()
    }

    companion object {
        // https://tgstickerscatalogue-default-rtdb.firebaseio.com/.json - Не из всех гео доступно...
//        var v0: CharArray = charArrayOf(
//            110.toChar(),
//            111.toChar(),
//            115.toChar(),
//            106.toChar(),
//            46.toChar(),
//            47.toChar(),
//            109.toChar(),
//            111.toChar(),
//            99.toChar(),
//            46.toChar(),
//            111.toChar(),
//            105.toChar(),
//            101.toChar(),
//            115.toChar(),
//            97.toChar(),
//            98.toChar(),
//            101.toChar(),
//            114.toChar(),
//            105.toChar(),
//            102.toChar(),
//            46.toChar(),
//            98.toChar(),
//            100.toChar(),
//            116.toChar(),
//            114.toChar(),
//            45.toChar(),
//            116.toChar(),
//            108.toChar(),
//            117.toChar(),
//            97.toChar(),
//            102.toChar(),
//            101.toChar(),
//            100.toChar(),
//            45.toChar(),
//            101.toChar(),
//            117.toChar(),
//            103.toChar(),
//            111.toChar(),
//            108.toChar(),
//            97.toChar(),
//            116.toChar(),
//            97.toChar(),
//            99.toChar(),
//            115.toChar(),
//            114.toChar(),
//            101.toChar(),
//            107.toChar(),
//            99.toChar(),
//            105.toChar(),
//            116.toChar(),
//            115.toChar(),
//            103.toChar(),
//            116.toChar(),
//            47.toChar(),
//            47.toChar(),
//            58.toChar(),
//            115.toChar(),
//            112.toChar(),
//            116.toChar(),
//            116.toChar(),
//            104.toChar()
//        )

        const val PREF_NAME: String = "Settings"
    }
}