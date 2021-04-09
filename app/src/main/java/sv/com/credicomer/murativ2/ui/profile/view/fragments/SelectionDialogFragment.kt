package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.app.Activity
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentSelectionDialogBinding
import sv.com.credicomer.murativ2.ui.profile.view.adapters.SelectReceiverAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel
import java.util.*

private const val EMAIL = "EMAIL"
private const val FROM_USER = "FROM_USER"
private const val RECEIVER = "RECEIVER"

class SelectionDialogFragment : Fragment() {

    lateinit var binding: FragmentSelectionDialogBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewModel:ProfileViewModel
    private var receiver:String? = null
    private var email: String? = null
    private var fromUser: Boolean? = null
    lateinit var date:Calendar
    private lateinit var listener: OnItemListClickListener
    private lateinit var myNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selection_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            myNavController = Navigation.findNavController(view)
        listener.setOnItemListClickListener(navController = myNavController)


        var usersList = listOf(
            "Gerson Aquino",
            "Manuel Samayoa",
            "katherine Lopez",
            "Ernesto Avila",
            "Jose Martinez",
            "Gerson Aquino",
            "Manuel Samayoa",
            "katherine Lopez",
            "Ernesto Avila",
            "Jose Martinez",
            "Gerson Aquino",
            "Manuel Samayoa",
            "katherine Lopez",
            "Ernesto Avila",
            "Jose Martinez",
            "Gerson Aquino",
            "Manuel Samayoa",
            "katherine Lopez",
            "Ernesto Avila",
            "Jose Martinez"
        )

        /*binding.usersList.setOnItemClickListener { adapterView, view, i, l ->
            Log.d("TAG", "it was click ${usersList[i]}")
        }

        ArrayAdapter(activity!!, R.layout.custom_spinner_layout, usersList).also {
            binding.usersList.apply {
                setAdapter(it)
            }
        }*/

        fun navigateOnItemListener(){
            listener.setOnItemListClickListener(navController = myNavController)
        }

        binding.receiverViewpager.adapter = SelectReceiverAdapter(parentFragment!!)

        TabLayoutMediator(binding.receiverTablelayout, binding.receiverViewpager){ tab, position ->  
            tab.text = when(position){
                0 -> "Colaborador"
                    else-> "grupo"
            }


        }.attach()


    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var users_list = listOf<UserCarnet>()
        var users = listOf<String>()

        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner, Observer {
            users_list = it
            users = it.map {
                it.name!!+ "-" + it.email
            }

            ArrayAdapter(activity!!, R.layout.custom_spinner_layout, users).also {
                binding.userAc.apply {
                    setAdapter(it)
                    threshold = 3
                }
            }

            if (fromUser!!){
                val receiver_text = it.filter {
                    it.email == receiver
                }
                binding.userAc.setText(receiver_text[0].name+"-"+receiver_text[0].email)
                binding.userAc.isEnabled = false
            }
        })

        binding.closeEmailContainer.setOnClickListener {
            this.dismiss()
        }

        binding.userAc.setOnItemClickListener { adapterView, view, i, l ->
            receiver = binding.userAc.text.toString().split("-")[1]
            binding.userAc.isEnabled = false
            binding.deleteUser.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    binding.userAc.isEnabled = true
                    binding.userAc.text.clear()
                    visibility = View.GONE
                }
            }
        }

        date = Calendar.getInstance()
        date.add(Calendar.DATE, 0)
        date.year
        date.month
        date.dayOfMonth
        date.time.hours
        var minutes = ""
        if (date.time.minutes < 10){
            minutes = "0${date.time.minutes}"
        }
        else{
            minutes = "${date.time.minutes}"
        }


        val date = "${date.dayOfMonth}-${date.month + 1}-${date.year} ${date.time.hours}:${minutes}"
        binding.sendEmail.setOnClickListener {
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            val randomString = (1..20)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");

            var isValidUser = users.contains(binding.userAc.text.toString().split("-")[1])
            if (binding.userAc.text.isNullOrEmpty() || binding.message.text.isNullOrEmpty() || isValidUser){
                Toast.makeText(context, "Completa todos los campos!", Toast.LENGTH_SHORT).show()
            }
            else{
                new_message = binding.message.text.toString()
                recognition = sv.com.credicomer.murativ2.ui.profile.model.Recognition(email ,receiver,new_message, date,false, randomString)
                viewModel.sendMessages(recognition!!, email!!,receiver!!)
                this.dismiss()
            }
        }
    }
    */
    override fun onStart() {
        super.onStart()
        //dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnItemListClickListener

    }

    interface OnItemListClickListener{
        fun setOnItemListClickListener(navController: NavController)
    }
    companion object {
        @JvmStatic
        fun newInstance(email: String, fromUser: Boolean, receiver:String) =
            SelectionDialogFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(EMAIL, email)
                    putBoolean(FROM_USER, fromUser)
                    putString(RECEIVER, receiver)
                }
            }
    }
}
