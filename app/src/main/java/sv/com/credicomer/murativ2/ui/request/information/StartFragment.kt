package sv.com.credicomer.murativ2.ui.request.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditscomer.viewmodel.UserInfoViewModel
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: UserInfoViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }
        mainViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getCredentials()

        mainViewModel.email.observe(viewLifecycleOwner, Observer {
            viewModel.getCreditRequests(it)
        })

        viewModel.requestList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0){
                binding.onboardingContainer.visibility = View.VISIBLE
                binding.recyclerContainer.visibility = View.GONE
            }
            else{
                binding.onboardingContainer.visibility = View.GONE
                binding.recyclerContainer.visibility = View.VISIBLE
                binding.recyclerviewRequests.layoutManager = LinearLayoutManager(context)
                binding.recyclerviewRequests.adapter = RequestsAdapter(it)
            }
        })

        binding.btnNewRequest.setOnClickListener{
            navController = Navigation.findNavController(it)
            navController.navigate(StartFragmentDirections.actionStartFragmentToSlideInfoFragment())
        }

        binding.newRequest.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigate(StartFragmentDirections.actionStartFragmentToSlideInfoFragment())
        }
    }

}