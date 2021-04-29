package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAddMessageBinding
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.AddMessageAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.MyItemRecyclerViewAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel
import sv.com.credicomer.murativ2.ui.roomsv2.fragments.RoomDetailFragmentArgs
import java.util.*


class AddMessageFragment : Fragment() {

    private lateinit var binding: FragmentAddMessageBinding
    private lateinit var args: AddMessageFragmentArgs
    lateinit var viewModel: ProfileViewModel
    lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mainViewModel  = ViewModelProvider(this).get(MainViewModel::class.java)
        args = arguments?.let {
            AddMessageFragmentArgs.fromBundle(it)
        }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_message, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        val myEmail = mainViewModel.getEmailStatic()
        var receivers = args.listOfUsers.map {
            it.email!!
        }

        val dateCalendar = Calendar.getInstance()
        dateCalendar.add(Calendar.DATE, 0)
        dateCalendar.year
        dateCalendar.month
        dateCalendar.dayOfMonth
        dateCalendar.time.hours
        var minutes = ""
        if (dateCalendar.time.minutes < 10){
            minutes = "0${dateCalendar.time.minutes}"
        }
        else{
            minutes = "${dateCalendar.time.minutes}"
        }


        val date = "${dateCalendar.dayOfMonth}-${dateCalendar.month + 1}-${dateCalendar.year} ${dateCalendar.time.hours}:${minutes}"

        binding.btnSendMessage.setOnClickListener {
            val message = binding.message.text.toString()
            val recognition = Recognition(myEmail, receivers, message, date, listOf(), "", "pendiente")
            viewModel.sendMessages(recognition)

            viewModel.messageIsSent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it){
                    navController.navigate(AddMessageFragmentDirections.actionAddMessageFragmentToPendingRecognitions())
                }
                else{
                    Toast.makeText(context,"Algo salio mal, intenta mas tarde", Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.usersRv.layoutManager = LinearLayoutManager(context)
        binding.usersRv.adapter = AddMessageAdapter(args.listOfUsers.toList())

    }

}