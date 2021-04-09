package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAchievementListBinding
import sv.com.credicomer.murativ2.ui.profile.model.Acknowledge
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.AchievementsAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.RecognitionsAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AchievementListFragment : Fragment() {


    private lateinit var binding: FragmentAchievementListBinding
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        navController = Navigation.findNavController(view)

        val achievementsList = listOf(
            Acknowledge("credicomer", "this is a simple message to show", "25 de marzo", "oiaoio3io434"),
            Acknowledge("credicomer", "this is a simple message to show", "25 de marzo", "oiaoio3io434"),
            Acknowledge("credicomer", "this is a simple message to show", "25 de marzo", "oiaoio3io434"),
            Acknowledge("credicomer", "this is a simple message to show", "25 de marzo", "oiaoio3io434")
        )

        val recognitionsList =
            mutableListOf(Recognition("gmisael@gmail.com", "Ernesto Guzman", "this is a simple message to show", "25 de marzo", false, "odoadoaidoiasod"),
                Recognition("gmisael@gmail.com", "Ernesto Guzman", "this is a simple message to show", "25 de marzo", false, "odoadoaidoiasod"),
                Recognition("gmisael@gmail.com", "Ernesto Guzman", "this is a simple message to show", "25 de marzo", false, "odoadoaidoiasod"),
                Recognition("gmisael@gmail.com", "Ernesto Guzman", "this is a simple message to show", "25 de marzo", false, "odoadoaidoiasod")
            )
        val users = listOf(
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "undefined", "undefined"),
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "undefined", "undefined"),
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "undefined", "undefined"),
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "undefined", "undefined"),
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "undefined", "undefined")
        )

        //load first recycler view
        binding.credicomerAchievementsRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AchievementsAdapter(achievementsList, context)
        }

        //load second recycler view
        binding.credicomerRecognitionsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RecognitionsAdapter(recognitionsList, users, ProfileViewModel(), true)
        }

        //navigate to select users
        binding.sendRecognition.setOnClickListener {
           navController.navigate(AchievementListFragmentDirections.actionAchievementsListToBirthdayFragment())
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
                navController.navigate(AchievementListFragmentDirections.actionAchievementListFragmentToProfileFragment2())
                true
            }
            else ->{
                NavigationUI.onNavDestinationSelected(item, navController)
            }

        }


    }

}