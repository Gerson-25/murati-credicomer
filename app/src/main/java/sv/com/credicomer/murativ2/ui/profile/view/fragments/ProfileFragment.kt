package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentProfileBinding
import sv.com.credicomer.murativ2.ui.profile.model.Acknowledge
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.RecognitionsAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.AchievementsAdapter
import sv.com.credicomer.murativ2.ui.profile.utils.CustomArrayAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel
import java.util.*

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var navController: NavController
    private lateinit var profileArgs: ProfileFragmentArgs

    lateinit var adapter: RecognitionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mainViewModel  = ViewModelProvider(this).get(MainViewModel::class.java)
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileArgs = arguments.let {
            ProfileFragmentArgs.fromBundle(it!!)
        }

        binding.user = profileArgs.user

        viewModel.getUsers()
        viewModel.getMessages(mainViewModel.getEmailStatic())

        viewModel.users.observe(viewLifecycleOwner, Observer {
            viewModel.messages.observe(viewLifecycleOwner, Observer { recognitionsList ->
                if(recognitionsList.isNullOrEmpty()){
                    binding.emptyStateContainer.visibility = View.VISIBLE
                }
                else{
                    binding.emptyStateContainer.visibility = View.GONE
                }
                binding.credicomerRecognitionsRv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RecognitionsAdapter(recognitionsList, it, ProfileViewModel(), mainViewModel.getEmailStatic())
                }
            })
        })



        viewModel.getRecognitions(profileArgs.user.email!!)

        viewModel.recognitions.observe(viewLifecycleOwner, Observer {
            binding.credicomerAchievementsRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = AchievementsAdapter(it, context)
            }
        })
    }

}