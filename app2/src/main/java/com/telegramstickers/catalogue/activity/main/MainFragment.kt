package com.telegramstickers.catalogue.activity.main

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.telegramstickers.catalogue.activity.StickerInfoActivity.Companion.newIntent
import com.telegramstickers.catalogue.databinding.FragmentHomeBinding
import com.walhalla.stickers.constants.MyIntent
import com.walhalla.stickers.database.StickerDb
import com.walhalla.stickers.fragment.AbstractStatusListFragment
import com.walhalla.stickers.fragment.FragmentRefreshListener
import com.walhalla.telegramstickers.adapter.StickerAdapter

class MainFragment : AbstractStatusListFragment() {
    private var mCallback: FCallback? = null


    private var binding: FragmentHomeBinding? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFavorite = !TextUtils.isEmpty(title)
        adapter = StickerAdapter(context, m!!.blockedItems, db!!.stickerDao(), isFavorite)
    }


    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (useAlphaSectionRes()) {
//            final String[] customAlphabet = getResources().getStringArray(R.array.alphabet);
//            binding.alphSectionIndex.setAlphabet(customAlphabet);
//            binding.alphSectionIndex.onSectionIndexClickListener((view1, position, character) -> {
//
//                int newValue = adapter.getPositionFromData(character);
//                DLog.d("[scroll]" + newValue);
//
//                //String info = " Position = " + position + " Char = " + character + "\t" + getPositionFromData(character);
//                //Log.i("View: ", view1 + "," + info);
//                //Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
//                //recyclerView.smoothScrollToPosition(getPositionFromData(character));
//                binding.recyclerView.scrollToPosition(newValue);
//            });
//        } else {
//            binding.alphSectionIndex.setVisibility(View.GONE);
//        }
        //floatingActionButton.attachToListView(this.gridView);
//        mBind.addNewStickers.setVisibility(View.GONE);
//        binding.addNewStickers.setOnClickListener(view0 ->
//                startActivityForResult(new Intent(MainFragment.this.getActivity(), AddStickerActivity.class), 1));
        mCallback!!.setFragmentRefreshListener(this)


        val numberOfColumns = 3
        val lm = adapter!!.getGridLayoutManager(context, numberOfColumns)
        binding!!.recyclerView.setLayoutManager(lm)
        adapter!!.setOnItemClickListener(this)
        binding!!.recyclerView.setAdapter(adapter)
    }

    public override fun handleProxy(data: StickerDb) {
        val intent = newIntent(context, data._id)
        this@MainFragment.startActivity(intent)
    }

    private fun useAlphaSectionRes(): Boolean {
        return false
    }

    public override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FCallback) {
            mCallback = context as FCallback
        } else {
            throw RuntimeException("$context must implement Callback")
        }
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    public override fun onDetach() {
        super.onDetach()
        mCallback = null
    }


    interface FCallback {
        fun setFragmentRefreshListener(listener: FragmentRefreshListener?)
    }

    companion object {
        fun newInstance(title: String?): Fragment {
            val fragment: Fragment = MainFragment()
            val bundle = Bundle()
            bundle.putString(MyIntent.KEY_CAT_NAME, title)
            fragment.setArguments(bundle)
            return fragment
        }
    }
}
