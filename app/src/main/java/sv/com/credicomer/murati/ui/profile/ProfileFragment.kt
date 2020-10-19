package sv.com.credicomer.murati.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_profile.*
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentProfileBinding
import java.lang.reflect.Array.get

class ProfileFragment : Fragment() {

    lateinit var binding:FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.recyclerviewRecognition.layoutManager = LinearLayoutManager(context)

        viewModel.getRecognitions()

        binding.birthdayNotification.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigate(ProfileFragmentDirections.actionProfileFragment2ToBirthdayFragment())
        }

        viewModel.recognitions.observe(viewLifecycleOwner, Observer {
            if (it.size > 0){
                binding.birthdayNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_component_birthday_new))
                binding.linearLayout13.visibility = View.GONE
            }
            else{
                binding.birthdayNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_component_birthday))
                binding.linearLayout13.visibility = View.VISIBLE
            }
            binding.recyclerviewRecognition.adapter = ProfileAdapter(it, context!!)
        })
    }


}