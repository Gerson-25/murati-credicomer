package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_selection_dialog.*
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAddGroupBinding
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.PendingRecognitionsAdapter
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel


class PendingRecognitionsFragment : Fragment() {

    lateinit var binding: FragmentAddGroupBinding

    lateinit var viewModel: ProfileViewModel
    lateinit var mainViewModel: MainViewModel
    private var handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mainViewModel  = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_group, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this){
            handler.postDelayed({
            }, 500)
        }

        /*val recognitionsList =
            mutableListOf(Recognition("gmisael@gmail.com", listOf("josegonzales@gmail.com"), "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("miguel@gmail.com", listOf("josegonzales@gmail.com"), " It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("gmisael@gmail.com", listOf("miguel@gmail.com"), "Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.", "25 de marzo", listOf(), "odoadoaidoiasod"),
                Recognition("josegonzales@gmail.com", listOf("miguel@gmail.com"), "Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.", "25 de marzo", listOf(), "odoadoaidoiasod")
            )
        val users = mutableListOf(
            UserCarnet("gmisael@gmail.com", "Jose Martinez", "undefined", "undefined", "https://qph.fs.quoracdn.net/main-qimg-616a3b9ebb3e90632c354684d4ed811e", "undefined"),
            UserCarnet("josegonzales@gmail.com", "Jose Gonzales", "undefined", "undefined", "https://image.freepik.com/free-photo/portrait-male-call-center-agent_23-2148096557.jpg", "undefined"),
            UserCarnet("miguel@gmail.com", "Miguel Gutierrez", "undefined", "undefined", "https://www.noblesystems.com/wp-content/uploads/2019/07/Featured_Blog_Agent-Burnout-p1-Warning-Signs.jpg", "undefined")
        )*/
        binding.pendingItemsRv.layoutManager = LinearLayoutManager(context)
        viewModel.getMessages(mainViewModel.getEmailStatic())
        viewModel.getUsers()

        viewModel.users.observe(viewLifecycleOwner, Observer {users->
            viewModel.messages.observe(viewLifecycleOwner, Observer { recognitionsList ->
                if (recognitionsList.isNullOrEmpty()){

                }
                else{
                    binding.pendingItemsRv.adapter = PendingRecognitionsAdapter(recognitionsList, users)
                }
            })
        })


    }


}