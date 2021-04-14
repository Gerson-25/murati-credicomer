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
    private var handler = Handler()
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

        val profileData = profileArgs.user

        val achievementsList = listOf<Acknowledge>(
        )



        binding.credicomerAchievementsRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = AchievementsAdapter(achievementsList, context)
        }

        val recognitionsList =
            mutableListOf(Recognition("gmisael@gmail.com", listOf("josegonzales@gmail.com"), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("miguel@gmail.com", listOf("josegonzales@gmail.com"), " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("gmisael@gmail.com", listOf("miguel@gmail.com"), "Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("josegonzales@gmail.com", listOf("miguel@gmail.com"), "Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.", "25 de marzo", listOf(), "odoadoaidoiasod")
            )
        val users = listOf(
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "https://qph.fs.quoracdn.net/main-qimg-616a3b9ebb3e90632c354684d4ed811e", "undefined"),
            UserCarnet("josegonzales@gmail.com", "Jose Gonzales", "undefined", "undefined", "https://image.freepik.com/free-photo/portrait-male-call-center-agent_23-2148096557.jpg", "undefined"),
            UserCarnet("miguel@gmail.com", "Miguel Gutierrez", "undefined", "undefined", "https://www.noblesystems.com/wp-content/uploads/2019/07/Featured_Blog_Agent-Burnout-p1-Warning-Signs.jpg", "undefined")
        )

        binding.credicomerRecognitionsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RecognitionsAdapter(recognitionsList, users, ProfileViewModel(), true)
        }

    }

    /*

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myEmail = mainViewModel.getEmailStatic()
        mainViewModel.getCredentials()
        mainViewModel.email.observe(viewLifecycleOwner, Observer {
            binding.sendMessageBtn.setOnClickListener { view ->
                openDialog(myEmail, true, it)
            }
            disablePersonalFunctions(myEmail, it)
            val user_adapter = CustomArrayAdapter(activity!!, R.layout.custom_list_item, listOf(), mainViewModel)
            binding.usersList.adapter = user_adapter
            binding.searchUser.clearFocus()
            binding.searchBarContainer.background = resources.getDrawable(R.drawable.bg_transparent_total)
            setProfile(it)
            val isMyUser = myEmail == it
            loadMessages(it, isMyUser)
            loadAchievements(it)
        })

        loadUsers()

        binding.goBackToProfilr.setOnClickListener {
            mainViewModel.changeProfile(myEmail)
        }

        binding.modifyProfile.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigate(
                ProfileFragmentDirections.actionProfileFragment2ToCreateCarnetFragment(
                    2
                )
            )
        }

        binding.scrollContainer.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            handler.postDelayed({
                binding.addEvent.background = resources.getDrawable(R.drawable.bg_room_items)
            }, 1000)
            binding.addEvent.background = resources.getDrawable(R.drawable.btn_accent)
        }

    }

    private fun scaler(star: LinearLayout) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun fader(star: LinearLayout) {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun disablePersonalFunctions(myEmail: String, currentEmail: String){
        if (myEmail == currentEmail){
            binding.addEvent.visibility = View.VISIBLE
            binding.modifyProfile.visibility = View.VISIBLE
            binding.sendMessageBtn.visibility = View.GONE
            binding.goBackToProfilr.visibility = View.GONE
        }
        else{
            binding.goBackToProfilr.visibility = View.VISIBLE
            binding.addEvent.visibility = View.GONE
            binding.modifyProfile.visibility = View.GONE
            binding.sendMessageBtn.visibility = View.VISIBLE
        }
    }

    private fun openDialog(email:String, fromUser:Boolean, receiver: String){
        val newMessageDialog =
            BirthdayDialogFragment.newInstance(
                email,
                fromUser,
                receiver
            )
        newMessageDialog.show(parentFragmentManager, "new message")
    }

    private fun setProfile(email: String){
        viewModel.getMyUser(email)
        viewModel.myUser.observe(viewLifecycleOwner, Observer {
            binding.myUser = it
            binding.imageView.apply {
                Glide.with(this).load(it.carnetPhoto).into(this)
            }
            binding.addEvent.setOnClickListener { listener ->
                openDialog(it.email!!, false, "")
            }
        })
    }

    private fun loadMessages(email: String, isMyUser: Boolean){
        //take messages from DB
        viewModel.getMessages(email)
        viewModel.getUsers()
        //observe result and fill adapter
        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            if(messages.isNullOrEmpty()){
                binding.recyclerviewMessages.visibility = View.GONE
            }
            else{
                binding.recyclerviewMessages.visibility = View.VISIBLE
                messages.sortBy {
                    it.date
                }
                binding.eventsCounter.text = messages.size.toString()
                viewModel.users.observe(viewLifecycleOwner, Observer { users->
                    binding.recyclerviewMessages.adapter =
                        RecognitionsAdapter(
                            messages,
                            users,
                            viewModel,
                            isMyUser
                        )
                })
            }
        })

        //set recyclerview
        binding.recyclerviewMessages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
    }


    private fun loadAchievements(email: String){

        viewModel.getRecognitions(email)

        viewModel.recognitions.observe(viewLifecycleOwner, Observer {
            binding.recognitionsCounter.text = it.size.toString()
            if (it.size > 0){
                it.sortBy {
                    it.postTime
                }
                binding.recyclerviewRecognition.adapter =
                    AchievementsAdapter(
                        it,
                        context!!
                    )
            }
        })

        binding.recyclerviewRecognition.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewRecognition.adapter =
            AchievementsAdapter(
                mutableListOf(),
                context!!
            )

    }

    private fun loadUsers(){
        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner, Observer { list ->
            val users_list = list
            val users = list.map {
                it.name!!
            }
            var user_adapter = CustomArrayAdapter(activity!!, R.layout.custom_list_item, users_list, mainViewModel)
            binding.searchUser.setOnSearchClickListener {
                binding.searchBarContainer.background = resources.getDrawable(R.drawable.bg_transparent_20)
                binding.usersList.adapter = user_adapter
            }
            binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    val new_list = users_list.filter {
                        val name = it.name!!.toLowerCase(Locale.ROOT)
                        name.contains(p0!!)
                    }
                    if (new_list.isNullOrEmpty()){
                        binding.noResults.visibility = View.VISIBLE
                    }
                    else{
                        binding.noResults.visibility = View.GONE
                    }
                    user_adapter = CustomArrayAdapter(activity!!, R.layout.custom_list_item, new_list, mainViewModel)
                    binding.usersList.adapter = user_adapter
                    return false
                }

            })
        })

        binding.searchUser.setOnCloseListener {
            binding.noResults.visibility = View.GONE
            binding.searchBarContainer.background = resources.getDrawable(R.drawable.bg_transparent_total)
            val user_adapter = ArrayAdapter(activity!!, R.layout.custom_spinner_layout, listOf<String>())
            binding.usersList.adapter = user_adapter
            return@setOnCloseListener false
        }

    } */

}