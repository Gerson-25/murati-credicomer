package sv.com.credicomer.murativ2.ui.alliance.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.EstablishmentsFragmentBinding
import sv.com.credicomer.murativ2.ui.alliance.adapters.EstablishmentsAdapter
import sv.com.credicomer.murativ2.ui.alliance.viewModels.EstablishmentsViewModel


class EstablishmentsFragment : Fragment() {


    private lateinit var viewModel: EstablishmentsViewModel
    private lateinit var binding: EstablishmentsFragmentBinding
    private lateinit var collectionPath:String
    private lateinit var subCollectionPath:String
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.establishments_fragment, container, false)
        viewModel = ViewModelProvider(this).get(EstablishmentsViewModel::class.java)

        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        collectionPath=mainViewModel.allianceCollectionPath.value.toString()
        subCollectionPath=mainViewModel.allianceSubCollectionPath.value.toString()


       val args =EstablishmentsFragmentArgs.fromBundle(arguments!!)

        val idCategory=args.idCategory

        binding.categoryImage.apply {
            when(idCategory){
                "Clinica" -> setImageResource(R.drawable.ic_medicine)
                "sports"-> setImageResource(R.drawable.ic_alliance_sport)
                "Diversion" -> setImageResource(R.drawable.ic_gamepad)
                "Comida" -> setImageResource(R.drawable.ic_burger)
                "Salud" -> setImageResource(R.drawable.ic_stethoscope)
                "Belleza"->setImageResource(R.drawable.ic_hair_dryer)
                "Educacion"->setImageResource(R.drawable.ic_newspaper)
                "Hogar"->setImageResource(R.drawable.ic_double_bed)
                "Moda"->setImageResource(R.drawable.ic_008_discount)
            }
        }
        binding.categoryName.text = idCategory


        viewModel.getFireStoreEstablishments(idCategory,collectionPath)

        val establishmentAdapter = EstablishmentsAdapter(idCategory)
        binding.recyclerEstablishment.adapter = establishmentAdapter
        binding.recyclerEstablishment.layoutManager = LinearLayoutManager(this.context)

        viewModel.establishmentFS.observe(viewLifecycleOwner, Observer {

            establishmentAdapter.submitList(it)

        })




        return binding.root
    }


}
