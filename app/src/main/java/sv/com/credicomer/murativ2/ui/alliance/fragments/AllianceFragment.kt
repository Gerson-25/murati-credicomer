package sv.com.credicomer.murativ2.ui.alliance.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAllianceBinding
import sv.com.credicomer.murativ2.ui.alliance.ScrollStateHolder
import sv.com.credicomer.murativ2.ui.alliance.adapters.CategoriesAdapter
import sv.com.credicomer.murativ2.ui.alliance.adapters.PromotionsPaginatorAdapter
import sv.com.credicomer.murativ2.ui.alliance.viewModels.AllianceViewModel
import sv.com.credicomer.murativ2.ui.ride.getToken
import sv.com.credicomer.murativ2.ui.ride.subscribeTopicNotifications


class AllianceFragment : Fragment() {
    private lateinit var binding: FragmentAllianceBinding
    private lateinit var allianceViewModel: AllianceViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var promotionsPaginatorAdapter: PromotionsPaginatorAdapter
    private lateinit var scrollStateHolder: ScrollStateHolder
    private lateinit var collectionPath: String
    private lateinit var subCollectionPath: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        collectionPath = mainViewModel.allianceCollectionPath.value.toString()
        subCollectionPath = mainViewModel.allianceSubCollectionPath.value.toString()

        allianceViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(AllianceViewModel::class.java) }

        getToken()

        if (collectionPath.contains("unicomer")) {
            subscribeTopicNotifications("alliance_unicomer")
        } else {
            subscribeTopicNotifications("alliance")
        }

        val id = arguments?.get("id_promotion")

        /* allianceViewModel =
             ViewModelProvider(this).get(AllianceViewModel::class.java)*/
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alliance, container, false)
        navController = findNavController()


            binding.promotionsRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        binding.allianceScrollview.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->



            allianceViewModel.scrollYposition = scrollY

        }


        if (allianceViewModel.adapter.value == null) {
            allianceViewModel.fireStoreAdapter(
                requireContext(),
                binding,
                viewLifecycleOwner,
                subCollectionPath
            ).observe(viewLifecycleOwner, Observer {
                promotionsPaginatorAdapter = it
                binding.promotionsRecyclerView.adapter = promotionsPaginatorAdapter
            })
        } else {
/*
            promotionsPaginatorAdapter = allianceViewModel.adapter.value!!
            binding.promotionsRecyclerView.adapter = promotionsPaginatorAdapter*/


            allianceViewModel.adapter.observe(viewLifecycleOwner, Observer {

                promotionsPaginatorAdapter = it
                binding.promotionsRecyclerView.adapter = promotionsPaginatorAdapter

            })

        }

        val categoriesAdapter = CategoriesAdapter()

        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.categoriesRecyclerView.adapter = categoriesAdapter



        binding.swipeRefreshLayout.setOnRefreshListener {
            allianceViewModel.getFireStoreCategoriesRefresh(collectionPath)
            //promotionsPaginatorAdapter.refresh()


            allianceViewModel.fireStoreAdapter(
                requireContext(),
                binding,
                viewLifecycleOwner,
                subCollectionPath
            ).observe(viewLifecycleOwner, Observer {
                promotionsPaginatorAdapter = it
                binding.promotionsRecyclerView.adapter = promotionsPaginatorAdapter

            })

        }

        if (allianceViewModel.categoriesFS.value.isNullOrEmpty()) {

            allianceViewModel.getFireStoreCategories(collectionPath)
                .observe(viewLifecycleOwner, Observer {

                    categoriesAdapter.submitList(it)



                })

        } else {

            allianceViewModel.categoriesFS.observe(viewLifecycleOwner, Observer {
                categoriesAdapter.submitList(it)


            })

        }



        return binding.root
    }


}