package com.christina.tracker.activity

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.christina.tracker.*
import com.google.firebase.auth.FirebaseAuth
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by christina on 2/7/16.
 */

public class SwipeableActivity: FragmentActivity() {
  val navDataSource: BehaviorSubject<List<String>> by lazy { BehaviorSubject.create<List<String>>(listOf("a", "b")) }
  var counter = 0
  val adapter: NavListAdapter by lazy { NavListAdapter(navDataSource.asObservable()) }
  var creationObs: Subscription? = null

  val collectionPagerAdapter: PagerAdapter by lazy { CollectionPagerAdapter(supportFragmentManager) }

  override fun onCreate(savedInstanceBundle: Bundle?) {
    super.onCreate(savedInstanceBundle)

    setContentView(R.layout.activity_swipeable)

    configureNavigationDrawer()
    configureSwipeablePager()

    //test code
    val currVal = navDataSource.value
    val newVal = currVal + "$counter"
    navDataSource.onNext(newVal)
    adapter.notifyDataSetChanged()

    adapter.newExercise.observeOn(AndroidSchedulers.mainThread()).subscribe(
        {
          navDataSource.onNext(navDataSource.value + "new exc")
          adapter.notifyDataSetChanged()
        },
        { e ->
          Timber.e("Error in the add logic $e")
        }
    )
  }

  private fun configureSwipeablePager() {
    val pager = findViewById(R.id.main_view) as ViewPager
    pager.adapter = collectionPagerAdapter
  }

  private fun configureNavigationDrawer() {
    //todo: use data binding
    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
    val drawerRecyclerView: RecyclerView = drawerLayout.findViewById(R.id.nav_drawer_list) as RecyclerView

    drawerRecyclerView.setAdapter(adapter)
    val linearLayourManager = LinearLayoutManager(this)
    drawerRecyclerView.setLayoutManager(linearLayourManager)
  }
}

public class NavListAdapter: RecyclerView.Adapter<BaseNavDrawerViewHolder> {
  private final val NAV_ITEM_TYPE = 0
  private final val NAV_TITLE_TYPE = 1
  private final val NAV_ADD_TYPE = 2

  val newExercise: PublishSubject<String> by lazy { PublishSubject.create<String>() }

  private var listData: List<String> = emptyList()
  private var sub: Subscription? = null

  public constructor(navDataSource: Observable<List<String>>) {
    //todo: should test is this sub leaks
    sub = navDataSource.subscribe(
        { newList: List<String> -> //onNext
          Timber.d("got new data to show for the nav drawer: %@", newList)
          listData = newList
        },
        { e -> //onError
          Timber.e("error in nav data source obs %@", e)
        }
    )
  }

  override fun onBindViewHolder(holder: BaseNavDrawerViewHolder?, position: Int) {
    val data: String = listData[position] //todo: check this int

    when (holder) {
      is NavDrawerItemViewHolder -> {
        holder.getHolderBinding().itemData = data
        holder.getHolderBinding().executePendingBindings()
      }
      is NavDrawerTitleViewHolder -> {
        val pojo = NavDrawerPojo("Christina Lee", "cml446@gmail.com")
        holder.getHolderBinding().setTitleData(pojo)
        holder.getHolderBinding().executePendingBindings()
      }
      is NavDrawerAddViewHolder -> {
        //no op
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseNavDrawerViewHolder? {
    when (viewType) {
      NAV_ITEM_TYPE -> {
        val view = LayoutInflater.from(parent?.getContext()).inflate(R.layout.nav_item_row, parent, false)
        return NavDrawerItemViewHolder(view)
      }
      NAV_TITLE_TYPE -> {
        val view = LayoutInflater.from(parent?.getContext()).inflate(R.layout.nav_item_header, parent, false)
        return NavDrawerTitleViewHolder(view)
      }
      NAV_ADD_TYPE -> {
        val view = LayoutInflater.from(parent?.getContext()).inflate(R.layout.nav_item_add, parent, false)
        return NavDrawerAddViewHolder(view, newExercise)
      }
    }
    throw Error("Could not determine a valid view holder for unsupported type $viewType in onCreateViewHolder")
  }

  override fun getItemCount(): Int {
    return listData.count()
  }

  override fun getItemViewType(position: Int): Int {
    if (position == 0) return NAV_TITLE_TYPE
    else if (position == listData.count() - 1) return NAV_ADD_TYPE

    return NAV_ITEM_TYPE
  }
}
