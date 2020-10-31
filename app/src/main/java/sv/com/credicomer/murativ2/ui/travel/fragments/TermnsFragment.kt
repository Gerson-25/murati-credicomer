package sv.com.credicomer.murativ2.ui.travel.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController

import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.TermnsFragmentBinding
import sv.com.credicomer.murativ2.ui.travel.viewModel.TermnsViewModel

class TermnsFragment : Fragment() {


    private lateinit var viewModel: TermnsViewModel
    private lateinit var navController: NavController
    private lateinit var binding: TermnsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.termns_fragment, container, false)

        viewModel = ViewModelProviders.of(this).get(TermnsViewModel::class.java)


        return binding.root
    }


}
