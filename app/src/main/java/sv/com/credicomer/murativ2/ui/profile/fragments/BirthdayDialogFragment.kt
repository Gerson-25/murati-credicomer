package sv.com.credicomer.murativ2.ui.profile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentBirthdayDialogBinding
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.hours

private const val EMAIL = "EMAIL"
private const val FROM_USER = "FROM_USER"
private const val RECEIVER = "RECEIVER"

class BirthdayDialogFragment : DialogFragment() {

    lateinit var binding: FragmentBirthdayDialogBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewModel:ProfileViewModel
    private var receiver:String? = null
    private var message:sv.com.credicomer.murativ2.ui.profile.model.Message? = null
    private var new_message:String? = null
    private var email: String? = null
    private var fromUser: Boolean? = null
    lateinit var date:Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(EMAIL)
            fromUser = it.getBoolean(FROM_USER)
            receiver = it.getString(RECEIVER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_birthday_dialog, container, false)
        return binding.root
    }

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
                message = sv.com.credicomer.murativ2.ui.profile.model.Message(email ,receiver,new_message, date,false, randomString)
                viewModel.sendMessages(message!!, email!!,receiver!!)
                this.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        @JvmStatic
        fun newInstance(email: String, fromUser: Boolean, receiver:String) =
            BirthdayDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(EMAIL, email)
                    putBoolean(FROM_USER, fromUser)
                    putString(RECEIVER, receiver)
                }
            }
    }
}
