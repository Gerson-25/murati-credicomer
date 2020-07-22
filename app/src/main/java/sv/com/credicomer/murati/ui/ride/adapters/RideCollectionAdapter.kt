package sv.com.credicomer.murati.ui.ride.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sv.com.credicomer.murati.ui.ride.fragments.MapFragment
import sv.com.credicomer.murati.ui.ride.fragments.ReservationFSFragment


class RideCollectionAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instancDemoObjectFragmente in createFragment(int)

        return if (position==0){
            ReservationFSFragment()
        }else{
            MapFragment()
        }
    }
}