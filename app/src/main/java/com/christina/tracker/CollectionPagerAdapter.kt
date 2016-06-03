package com.christina.tracker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by christina on 6/2/16.
 */

class CollectionPagerAdapter: FragmentStatePagerAdapter {
    constructor (fm: FragmentManager): super(fm) { }

    override fun getItem(position: Int): Fragment? {
        val fragment = CollectionFragment()
        var args = Bundle()
        when(position){
            0 -> args.putString("string", "Screen 1")
            1 -> args.putString("string", "Screen 2")
            2 -> args.putString("string", "Screen 3")
        }
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}

class CollectionFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                                     container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.stats_fragment, container, false)
        val textView = view.findViewById(R.id.statsText) as TextView
        textView.text = arguments.getString("string")
        return view
    }
}
