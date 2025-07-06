package com.walhalla.telegramstickers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.walhalla.stickers.AppDatabase
import com.walhalla.stickers.adapter.AbstractStickerAdapter.ItemClickListener
import com.walhalla.stickers.adapter.empty.EmptyViewModel
import com.walhalla.stickers.database.LocalDatabaseRepo
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.utils.TelegramUtils
import com.walhalla.stickers.wads.KSUtil
import com.walhalla.telegramstickers.activity.StickerInfoActivity.Companion.newIntent
import com.walhalla.telegramstickers.adapter.StickerAdapter
import com.walhalla.telegramstickers.databinding.FragmentMainBinding
import com.walhalla.ui.plugins.Module_U
import java.util.Collections
import java.util.Locale
import java.util.Random

class BaseDBFragment : Fragment(), ItemClickListener {
    protected var db: AppDatabase? = null
    var list: MutableList<StickerDb?>? = ArrayList<StickerDb?>()


    private var val0: String? = ""
    private var adapter: StickerAdapter? = null
    private var m: KSUtil? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m = KSUtil.getInstance(getActivity())
        if (getArguments() != null) {
            val0 = requireArguments().getString(KEY_BUNDLE_0)
            val a = getActivity() as AppCompatActivity?
            if (a != null) {
                val r = a.getSupportActionBar()
                if (r != null) {
                    r.setSubtitle(val0)
                }
            }
        }
        db = LocalDatabaseRepo.getDatabase(getActivity())
        adapter = StickerAdapter(getContext(), m!!.getBlockedItems(), db!!.stickerDao(), false)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(layoutInflater, viewGroup, false)

        val defaultSpanCount = 3
        val lm = adapter!!.getGridLayoutManager(getContext(), defaultSpanCount)
        binding.recyclerView.setLayoutManager(lm)
        adapter!!.setOnItemClickListener(this)
        binding.recyclerView.setAdapter(adapter)
        refreshCategory(binding, val0!!)
        return binding.getRoot()
    }

    private fun refreshCategory(binding: FragmentMainBinding?, category: String) {
        val categoryLowerCase = category.lowercase(Locale.getDefault())
        this.list = db!!.stickerDao().getCategoryStickers(categoryLowerCase)
        if (this.list == null || list!!.isEmpty()) {
            adapter!!.swap(EmptyViewModel(""))
        } else {
            Collections.shuffle(this.list, Random(System.nanoTime()))
            adapter!!.swap(list)
        }
    }


    override fun onItemClick(stickerDb: StickerDb, position: Int) {
        val intent = newIntent(getContext(), stickerDb._id)
        this@BaseDBFragment.startActivity(intent)
    }

    override fun shareItem(sticker: StickerDb) {
        Module_U.shareText(requireContext(), TelegramUtils.ADDSTICKERS_WEB + sticker.link, null)
    }

    companion object {
        private const val KEY_BUNDLE_0 = "value_0"

        @JvmStatic
        fun newInstance(category: String?): BaseDBFragment {
            val f = BaseDBFragment()
            val bundle = Bundle()
            bundle.putString(KEY_BUNDLE_0, category)
            f.setArguments(bundle)
            return f
        }
    }
}
