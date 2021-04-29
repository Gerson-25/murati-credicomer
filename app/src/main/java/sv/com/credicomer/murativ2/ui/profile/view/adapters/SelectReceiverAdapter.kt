package sv.com.credicomer.murativ2.ui.profile.view.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sv.com.credicomer.murativ2.ui.profile.view.fragments.GroupListFragment
import sv.com.credicomer.murativ2.ui.profile.view.fragments.OnItemListClickListener
import sv.com.credicomer.murativ2.ui.profile.view.fragments.UsersListFragment

private const val NUM_PAGES = 2

class SelectReceiverAdapter(activity: Fragment, var listener: OnItemListClickListener): FragmentStateAdapter(activity){
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int) = when (position){
        0 -> UsersListFragment(listener)
        else -> GroupListFragment()
    }

}