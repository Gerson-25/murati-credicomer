package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_selection_dialog.*
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAchievementListBinding
import sv.com.credicomer.murativ2.ui.profile.model.Acknowledge
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.AchievementsAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.MyItemRecyclerViewAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.RecognitionsAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AchievementListFragment : Fragment() {

    private lateinit var binding: FragmentAchievementListBinding
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mainViewModel: MainViewModel
    private lateinit var navController:NavController
    private var handler = Handler()
    private lateinit var users : MutableList<UserCarnet>
    private lateinit var usersAdapter: MyItemRecyclerViewAdapter
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel  = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievement_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.getUsers()
        viewModel.getAllMessages()

        viewModel.users.observe(viewLifecycleOwner, Observer {
            users = it.toMutableList()
            binding.userListRv.apply{
                usersAdapter = MyItemRecyclerViewAdapter(it.toMutableList(),
                    object : OnItemListClickListener {
                        override fun setOnItemListClickListener(userCarnet: UserCarnet) {
                            navController.navigate(AchievementListFragmentDirections.actionAchievementListFragmentToProfileFragment2(userCarnet))
                        }
                    },1, context!!)
                layoutManager = LinearLayoutManager(context)
                adapter = usersAdapter
                binding.usersEt.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        val newValues = it.filter {
                            it.email!!.contains(p0!!)
                        }

                        binding.userListRv.adapter = MyItemRecyclerViewAdapter(newValues.toMutableList(),
                            object : OnItemListClickListener {
                                override fun setOnItemListClickListener(userCarnet: UserCarnet) {
                                    navController.navigate(AchievementListFragmentDirections.actionAchievementListFragmentToProfileFragment2(userCarnet))
                                }
                            },1, context!!)

                    }
                })
            }

            binding.sendRecongnitionBtn.setOnClickListener {
                navController.navigate(AchievementListFragmentDirections.actionAchievementsListToBirthdayFragment())
            }
            viewModel.messages.observe(viewLifecycleOwner, Observer { recognitions->
                if(recognitions.isNullOrEmpty()){
                    binding.emptyStateContainer.visibility = View.VISIBLE
                }
                else{
                    binding.emptyStateContainer.visibility = View.GONE
                }
                binding.credicomerRecognitionsRv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RecognitionsAdapter(recognitions, it, ProfileViewModel(), mainViewModel.getEmailStatic())
                }
            })

        })

        viewModel.getUsers()


        navController = Navigation.findNavController(view)


        //load first recycler view

        binding.usersEt.setOnFocusChangeListener { editText, b ->
            if (b) {
                binding.emptyStateContainer.visibility = View.GONE
                binding.userListRv.visibility = View.VISIBLE
                binding.credicomerRecognitionsRv.visibility = View.GONE
                requireActivity().onBackPressedDispatcher.addCallback(this){
                        handler.postDelayed({
                            binding.userListRv.visibility = View.GONE
                            binding.usersEt.clearFocus()
                            binding.usersEt.setText("")
                            binding.credicomerRecognitionsRv.visibility = View.VISIBLE
                        }, 500)
                }
            }
            else {
                binding.credicomerRecognitionsRv.visibility = View.VISIBLE
                binding.userListRv.visibility = View.GONE
            }
        }



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.nav_profile -> {
                val myuser = users.filter {
                    it.email == mainViewModel.getEmailStatic()
                }
                navController.navigate(AchievementListFragmentDirections.actionAchievementListFragmentToProfileFragment2(myuser[0]))
                true
            }
            R.id.nav_create_group -> {
                navController.navigate(AchievementListFragmentDirections.actionAchievementsListToAddGroupFragment())
                true
            }
            R.id.nav_add_message -> {
                navController.navigate(AchievementListFragmentDirections.actionAchievementsListToBirthdayFragment())
                true
            }
            else ->{
                NavigationUI.onNavDestinationSelected(item, navController)
            }

        }

    }


}