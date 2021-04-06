package sv.com.credicomer.murativ2.ui.ride.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentReservationFsBinding
import sv.com.credicomer.murativ2.ui.ride.TYPE_ITEM
import sv.com.credicomer.murativ2.ui.ride.adapters.AdapterFSItem
import sv.com.credicomer.murativ2.ui.ride.adapters.ReservationFSAdapter
import sv.com.credicomer.murativ2.ui.ride.updateReservationsFSList
import sv.com.credicomer.murativ2.ui.ride.viewModels.ReservationFSViewModel
import timber.log.Timber


class ReservationFSFragment : Fragment() {

    private lateinit var binding: FragmentReservationFsBinding
    private lateinit var viewModel: ReservationFSViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_fs, container, false)
        viewModel = ViewModelProvider(this).get(ReservationFSViewModel::class.java)
        viewModel.getToken()

        val adapter=ReservationFSAdapter(viewModel, requireContext())
        binding.recyclerviewReservationfs.adapter=adapter
        binding.recyclerviewReservationfs.layoutManager=LinearLayoutManager(context)

        viewModel.getAllReservations().observe(viewLifecycleOwner, Observer {

            it.let {reservation->

                val headerList= reservation?.map {maped-> AdapterFSItem(maped, TYPE_ITEM) }?: emptyList()

                if(headerList.isEmpty()){ //Visibility on empty state
                    binding.emptyStateRide1.visibility = View.VISIBLE
                }else{
                    binding.emptyStateRide1.visibility = View.GONE
                }
                adapter.submitList( updateReservationsFSList(headerList))

                Timber.d("LIVEDATA %s", "the list is -> $it")

            }


        })


        viewModel.getUSerStates().observe(viewLifecycleOwner, Observer {


            it.let {


                Timber.d("STATES %s","the states are -> $it")

            }


        })


        return binding.root
    }


}
