package sv.com.credicomer.murativ2.ui.roomsv2.fragments


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import org.joda.time.format.DateTimeFormat
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentRoomsHomeBinding
import sv.com.credicomer.murativ2.ui.roomsv2.*
import sv.com.credicomer.murativ2.ui.roomsv2.adapters.RoomAdapter
import sv.com.credicomer.murativ2.ui.roomsv2.models.RoomQuery
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomViewModel
import timber.log.Timber
import java.util.*


class RoomsHomeFragment : Fragment() {
    private lateinit var binding: FragmentRoomsHomeBinding
    private lateinit var viewModel: RoomViewModel
    private lateinit var collectionPath: String
    private lateinit var subCollectionPath: String
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rooms_home, container, false)
        //  viewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        navController = findNavController()
        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        collectionPath = mainViewModel.roomCollectionPath.value.toString()
        subCollectionPath = mainViewModel.roomSubCollectionPath.value.toString()

        viewModel =
            activity.run { ViewModelProvider(requireActivity()).get(RoomViewModel::class.java) }

        viewModel.collectionPath = collectionPath
        viewModel.subCollectionPath = subCollectionPath

        setHasOptionsMenu(true)
        roomRecycler("")
        viewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            Timber.d("FECHA_HOME %s", it)
            if (it == null){
                roomRecycler("")
                }
            else {
                roomRecycler(it)
            }
        })

/*
        binding.btnRoomHour.setOnClickListener { btn ->

            btn.isEnabled = false
            binding.chipHour.let { chip ->

                chip.visibility = View.VISIBLE
                chip.setOnCloseIconClickListener {
                    chip.visibility = View.GONE
                    btn.isEnabled = true
                }

                chip.setOnClickListener {

                }
            }
        }*/

        binding.btnRoomDate.setOnClickListener { btn ->
            btn.isEnabled = false
            binding.chipDate.let { chip ->

                chip.visibility = View.VISIBLE
                chip.setOnCloseIconClickListener {
                    chip.visibility = View.GONE
                    btn.isEnabled = true
                }
                chip.setOnClickListener {

                    showDatePickerDialog()
                }
            }

        }

        binding.btnRoomHour.setOnClickListener {
            viewModel.showFilterDialog(requireContext(), HOUR_FILTER)
        }


        binding.btnRoomLocation.setOnClickListener {

            viewModel.showFilterDialog(requireContext(), LOCATION_FILTER)


        }

        binding.btnRoomEquipment.setOnClickListener {

            viewModel.showFilterDialog(requireContext(), EQUIPMENT_FILTER)


        }

        binding.btnRoomCapacity.setOnClickListener {

            viewModel.showFilterDialog(requireContext(), CAPACITY_FILTER)

        }

        binding.btnRoomDate.setOnClickListener {

            showDatePickerDialog()
        }

        viewModel.selectedCapacityChip.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.btnRoomCapacity.isEnabled = false
                binding.chipCapacity.let { chip ->

                    chip.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        chip.visibility = View.GONE
                        binding.btnRoomCapacity.isEnabled = true
                        viewModel.resetChip(CAPACITY_FILTER)
                    }
                    chip.setOnClickListener {

                        viewModel.showFilterDialog(requireContext(), CAPACITY_FILTER)

                    }
                }
            }
        })

        viewModel.selectedEquipmentChip.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.btnRoomEquipment.isEnabled = false
                binding.chipEquipment.let { chip ->

                    chip.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        chip.visibility = View.GONE
                        binding.btnRoomEquipment.isEnabled = true
                        viewModel.resetChip(EQUIPMENT_FILTER)
                    }
                    chip.setOnClickListener {

                        viewModel.showFilterDialog(requireContext(), EQUIPMENT_FILTER)

                    }
                }
            }

        })

        viewModel.selectedLocationChip.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.btnRoomLocation.isEnabled = false
                binding.chipLocation.let { chip ->

                    chip.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        chip.visibility = View.GONE
                        binding.btnRoomLocation.isEnabled = true
                        viewModel.resetChip(LOCATION_FILTER)
                    }
                    chip.setOnClickListener {
                        viewModel.showFilterDialog(requireContext(), LOCATION_FILTER)

                    }

                }

            }
        })

        viewModel.selectedHoursChip.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.btnRoomHour.isEnabled = false
                binding.chipHour.let { chip ->

                    chip.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        chip.visibility = View.GONE
                        binding.btnRoomHour.isEnabled = true
                        viewModel.resetChip(HOUR_FILTER)
                    }
                    chip.setOnClickListener(null)

                }

            }


        })



        viewModel.selectedDateChip.observe(viewLifecycleOwner, Observer {

            if (it) {
                binding.btnRoomDate.isEnabled = false
                binding.chipDate.let { chip ->

                    chip.visibility = View.VISIBLE
                    chip.setOnCloseIconClickListener {
                        chip.visibility = View.GONE
                        binding.btnRoomDate.isEnabled = true
                        viewModel.resetChip(DATE_FILTER)
                    }
                    chip.setOnClickListener {
                        showDatePickerDialog()

                    }

                }

            } else {
                binding.chipDate.visibility = View.GONE
                binding.btnRoomDate.isEnabled = true

                binding.chipHour.visibility = View.GONE
                binding.btnRoomHour.isEnabled = true

            }


        })



        viewModel.selectedHours.observe(viewLifecycleOwner, Observer {

            Timber.d("HOURS %s", it)
            binding.chipHour.text = it.combinedHour

        })

        viewModel.selectedCapacity.observe(viewLifecycleOwner, Observer {

            Timber.d("CAPACITY %s", it)
            binding.chipCapacity.text = it

        })

        viewModel.selectedEquipment.observe(viewLifecycleOwner, Observer {
            Timber.d("EQUIPMENT %s", it)
            binding.chipEquipment.text = it

        })

        viewModel.selectedLocation.observe(viewLifecycleOwner, Observer {

            Timber.d("LOCATION %s", it)
            binding.chipLocation.text = it


        })

        viewModel.selectedDate.observe(viewLifecycleOwner, Observer {

            Timber.d("DATE %s", it)
            binding.chipDate.text = it


        })


        binding.searchViewRooms.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText == "") {
                    viewModel.resetQueryRoom()
                }
                return false
            }

            @SuppressLint("DefaultLocale")
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(requireContext(), "the query is -> $query", Toast.LENGTH_LONG).show()
                val queryAux = query.toLowerCase()

                val queryList = mutableListOf(RoomQuery("roomName", mutableListOf(queryAux)))
                viewModel.queryRooms(collectionPath, queryList)
                return false
            }

        })


        return binding.root
    }

    private fun showDatePickerDialog() {

        val lastDateValue = viewModel.selectedDate.value
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        if (lastDateValue!=null) {

            val sdf = DateTimeFormat.forPattern("dd-MM-yyyy")
            val date = sdf.parseDateTime(lastDateValue)
            val cal = Calendar.getInstance()
            cal.time = date.toDate()


            month = cal.get(Calendar.MONTH)
            day = cal.get(Calendar.DAY_OF_MONTH)

        }


        val dateP = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { _, yearListener, monthOfYear, dayOfMonth ->

                val selectedDate = getParsedDate(dayOfMonth, (monthOfYear + 1), yearListener)

                Timber.d("SDATE %s", "the date is->$selectedDate")
                viewModel.pushDetailId(selectedDate, DATE_FILTER)
                //viewModel.dateFilter(selectedDate)
            },
            year,
            month,
            day
        )

        dateP.datePicker.minDate = c.timeInMillis
        dateP.show()

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.rooms_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.nav_profile -> {
                navController.navigate(
                    RoomsHomeFragmentDirections.actionNavRooms2ToMyReservationsFragment()
                )
                true
            }
            else -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }

    private fun roomRecycler(date:String){
        val roomAdapter = RoomAdapter(date)
        roomAdapter.notifyDataSetChanged() //added bc icon
        binding.recyclerViewRoomsHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRoomsHome.adapter = roomAdapter

        if (viewModel.rooms.value.isNullOrEmpty()) {
            viewModel.getAllRooms(collectionPath).observe(viewLifecycleOwner, Observer {
                it.let {
                    if (it.isNotEmpty()) {
                        roomAdapter.notifyDataSetChanged()
                        roomAdapter.submitList(it)
                    } else {

                        Toast.makeText(
                            requireContext(),
                            "No hay resultados de tu consulta",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    Timber.d("OBS %s", "$it")
                }

            })
        } else {
            viewModel.rooms.observe(viewLifecycleOwner, Observer {
                it.let {
                    if (it.isNotEmpty()) {
                        roomAdapter.notifyDataSetChanged()
                        roomAdapter.submitList(it)
                    } else {

                        Toast.makeText(
                            requireContext(),
                            "No hay resultados de tu consulta",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Timber.d("OBS %s", "$it")
                }

            })
        }
    }

}
