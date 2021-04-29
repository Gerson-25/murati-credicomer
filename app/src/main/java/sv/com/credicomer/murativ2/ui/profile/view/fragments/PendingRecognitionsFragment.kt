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

        binding.pendingItemsRv.layoutManager = LinearLayoutManager(context)
        viewModel.getPendingMessages(mainViewModel.getEmailStatic())
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