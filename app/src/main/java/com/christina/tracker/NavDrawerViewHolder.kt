package com.christina.tracker

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.christina.tracker.databinding.NavItemHeaderBinding
import com.christina.tracker.databinding.NavItemRowBinding
import rx.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by christina on 5/30/16.
 */

abstract  class BaseNavDrawerViewHolder: RecyclerView.ViewHolder {
  constructor(itemView: View?): super(itemView) {}
  //todo: make abstract getHolderBinding()
  abstract fun getHolderBinding(): ViewDataBinding?
}

class NavDrawerItemViewHolder : BaseNavDrawerViewHolder {
  private val binding: NavItemRowBinding

  constructor(itemView: View?): super(itemView) {
    binding = DataBindingUtil.bind(itemView)
  }

  override  fun getHolderBinding(): NavItemRowBinding {
    return binding
  }
}

class NavDrawerTitleViewHolder : BaseNavDrawerViewHolder {
  private val binding: NavItemHeaderBinding

  constructor(itemView: View?): super(itemView) {
    binding = DataBindingUtil.bind(itemView)
  }

  override fun getHolderBinding(): NavItemHeaderBinding {
    return binding
  }
}

class NavDrawerAddViewHolder : BaseNavDrawerViewHolder {
  val newExercize: PublishSubject<String>

  constructor(itemView: View?, newExercize: PublishSubject<String>): super(itemView) {
    this.newExercize = newExercize

    itemView?.isClickable = true
    itemView?.setOnClickListener {
      Timber.i("add was clicked")
      newExercize.onNext("")
    }
  }

  override fun getHolderBinding(): ViewDataBinding? {
    return null
  }
}
