package sv.com.credicomer.murati.ui.alliance.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import sv.com.credicomer.murati.MainViewModel
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.PromotionsFragmentBinding
import sv.com.credicomer.murati.ui.alliance.adapters.PromotionsPaginatorAdapter2
import sv.com.credicomer.murati.ui.alliance.viewModels.PromotionsViewModel

class PromotionsFragment : Fragment() {


    private lateinit var viewModel: PromotionsViewModel
    private lateinit var binding: PromotionsFragmentBinding
    private lateinit var navController: NavController
    private lateinit var promotionsPaginatorAdapter: PromotionsPaginatorAdapter2
    private lateinit var collectionPath:String
    private lateinit var subCollectionPath:String
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.promotions_fragment, container, false)
        viewModel = ViewModelProvider(this).get(PromotionsViewModel::class.java)

        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        collectionPath=mainViewModel.allianceCollectionPath.value.toString()
        subCollectionPath=mainViewModel.allianceSubCollectionPath.value.toString()

        navController = findNavController()

        val args = PromotionsFragmentArgs.fromBundle(arguments!!)
        val establishment = args.establishment
        val idCategory = args.idCategory

        binding.establishment=establishment

        Glide.with(this).load(establishment.establishment_image).into(binding.imageLogo)


        binding.recyclerPromotions.layoutManager = LinearLayoutManager(this.context)

        viewModel.getPromotions(requireContext(),binding,viewLifecycleOwner,idCategory,establishment.establishment_id,collectionPath,subCollectionPath).observe(viewLifecycleOwner,
            Observer {
                promotionsPaginatorAdapter=it
                binding.recyclerPromotions.adapter=promotionsPaginatorAdapter
            })

        binding.swipeRefreshPromotions.setOnRefreshListener {
            promotionsPaginatorAdapter.refresh()
        }




        return binding.root
    }


}
