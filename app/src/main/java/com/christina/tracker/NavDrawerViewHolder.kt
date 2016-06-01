package com.christina.tracker

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.christina.tracker.databinding.NavItemHeaderBinding
import com.christina.tracker.databinding.NavItemRowBinding

/**
 * Created by christina on 5/30/16.
 */

abstract  class BaseNavDrawerViewHolder: RecyclerView.ViewHolder {
  constructor(itemView: View?): super(itemView) {}
  //todo: make abstract getHolderBinding()
  abstract fun getHolderBinding(): ViewDataBinding
}

class NavDrawerItemViewHolder : BaseNavDrawerViewHolder {
  private val binding: NavItemRowBinding
//  val textView: TextView?

  constructor(itemView: View?): super(itemView) {
//    textView = itemView?.findViewById(R.id.navItemRowText) as TextView?
    binding = DataBindingUtil.bind(itemView)
  }

  override  fun getHolderBinding(): NavItemRowBinding {
    return binding
  }
}

class NavDrawerTitleViewHolder : BaseNavDrawerViewHolder {
  private val binding: NavItemHeaderBinding
//  val textView1: TextView?
//  val textView2: TextView?

  constructor(itemView: View?): super(itemView) {
//    textView1 = itemView?.findViewById(R.id.navItemHeaderText1) as TextView?
//    textView2 = itemView?.findViewById(R.id.navItemHeaderText2) as TextView?
    binding = DataBindingUtil.bind(itemView)
  }

  override fun getHolderBinding(): NavItemHeaderBinding {
    return binding
  }
}

//class NavDrawerAddViewHolder : BaseNavDrawerViewHolder {
//
//  constructor(itemView: View?): super(itemView) { }
//}
