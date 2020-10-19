package sv.com.credicomer.murati.ui.travel.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentHomeTrackerBinding
import sv.com.credicomer.murati.ui.travel.adapter.HistoryTravelAdapter
import sv.com.credicomer.murati.ui.travel.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeTrackerBinding
    private lateinit var navController: NavController


    @SuppressLint("RestrictedApi", "LogNotTimber")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_tracker, container, false)
        //binding.containerBienvenida.visibility = View.GONE
        //binding.etTextoBienvenida.visibility = View.GONE
        //binding.textViewBienvenida.visibility = View.GONE
        return binding.root
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { HomeFragmentArgs.fromBundle(it) }
        if (args!!.isTravelActive) {
            binding.floatingActionButtonAddviaje.visibility = View.GONE
        } else {
            binding.floatingActionButtonAddviaje.visibility = View.VISIBLE
        }

        Log.d("TAG", "This is the home screen")

        setHasOptionsMenu(true)
        homeViewModel.setUpRecyclerView()
        navController = findNavController()

        val adapter = HistoryTravelAdapter(homeViewModel)
        binding.recyclerHistorial.adapter = adapter
        binding.recyclerHistorial.layoutManager = LinearLayoutManager(this.context)

        homeViewModel.travelList.observe(viewLifecycleOwner, Observer {
            Log.d("TAG", "$it")
            if (it.isNullOrEmpty()) {
                binding.recyclerHistorial.visibility = View.GONE
                binding.emptyStateEtrackerNoTravel.visibility = View.VISIBLE
                //binding.containerBienvenida.visibility = View.VISIBLE
                //binding.etTextoBienvenida.visibility = View.VISIBLE
                //binding.textViewBienvenida.visibility = View.VISIBLE
                adapter.submitList(it)
            } else {
                binding.recyclerHistorial.visibility = View.VISIBLE
                binding.emptyStateEtrackerNoTravel.visibility = View.GONE
                //binding.containerBienvenida.visibility = View.GONE
                //binding.etTextoBienvenida.visibility = View.GONE
                //binding.textViewBienvenida.visibility = View.GONE
                adapter.submitList(it)
            }
        })

        /*homeViewModel.travelList.observe(viewLifecycleOwner, Observer {

            Log.d("TAG", "lista de viajes: $it")
            if (it.isNullOrEmpty()) {
                binding.recyclerHistorial.visibility = View.GONE
                binding.emptyStateEtrackerNoTravel.visibility = View.VISIBLE
                //binding.containerBienvenida.visibility = View.VISIBLE
                //binding.etTextoBienvenida.visibility = View.VISIBLE
                //binding.textViewBienvenida.visibility = View.VISIBLE
                adapter.submitList(it)
            } else {
                binding.recyclerHistorial.visibility = View.VISIBLE
                binding.emptyStateEtrackerNoTravel.visibility = View.GONE
                //binding.containerBienvenida.visibility = View.GONE
                //binding.etTextoBienvenida.visibility = View.GONE
                //binding.textViewBienvenida.visibility = View.GONE
                adapter.submitList(it)
            }

        })*/

        binding.floatingActionButtonAddviaje.setOnClickListener {
            navController.navigate(
                HomeFragmentDirections.actionNavHomeToTravelRegistrationFragment2(
                    "",
                    ""
                )
            )
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.termnsFragment -> {
                //Toast.makeText(requireContext(), "Estas en terminos", Toast.LENGTH_LONG).show()
                navController.navigate(HomeFragmentDirections.actionNavHomeToTermnsFragment())

                true
            }
            else -> {
                return NavigationUI.onNavDestinationSelected(item, navController)
            }

        }
    }


}



