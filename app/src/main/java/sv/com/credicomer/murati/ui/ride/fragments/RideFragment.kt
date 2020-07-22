package sv.com.credicomer.murati.ui.ride.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentRideBinding
import sv.com.credicomer.murati.ui.ride.adapters.RideCollectionAdapter
import sv.com.credicomer.murati.ui.ride.viewModels.RideViewModel

class RideFragment : Fragment() {

    private lateinit var binding:FragmentRideBinding
    private lateinit var rideViewModel: RideViewModel
    private lateinit var rideCollectionAdapter: RideCollectionAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_ride, container, false)
        rideViewModel =
            ViewModelProvider(this).get(RideViewModel::class.java)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       tabLayout = binding.tabLayout

        rideCollectionAdapter = RideCollectionAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = rideCollectionAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            if(position==0){
                tab.text="Horarios"
            }else{
                tab.text="Mapa"
            }

    
        }.attach()

    }
}