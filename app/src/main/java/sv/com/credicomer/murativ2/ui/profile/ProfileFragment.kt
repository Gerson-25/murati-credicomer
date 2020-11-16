package sv.com.credicomer.murativ2.ui.profile

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding:FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = "https://www.signfix.com.au/wp-content/uploads/2017/09/placeholder-600x400.png"
        Glide.with(this)
            .load(url)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .transform(CircleCrop())
            .into(binding.imageView)

        val urlPortada = "https://www.dinero.com.sv/media/k2/items/cache/fd8f7bbb057ea330f9a55f11147f1348_XL.jpg"
        Glide.with(this)
            .load(urlPortada)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .into(binding.portada)


        var list = mutableListOf(
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition"),
            Acknowledge("Recognition One", "this is a recognition")

        )

        binding.recyclerviewRecognition.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewRecognition.adapter = ProfileAdapter(list, context!!)

        viewModel.getRecognitions()

        /*binding.birthdayNotification.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigate(ProfileFragmentDirections.actionProfileFragment2ToBirthdayFragment())
        }

        viewModel.recognitions.observe(viewLifecycleOwner, Observer {
            if (it.size > 0){
                binding.birthdayNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_component_birthday_new))
                binding.linearLayout13.visibility = View.GONE
            }
            else{
                binding.birthdayNotification.setImageDrawable(resources.getDrawable(R.drawable.ic_component_birthday))
                binding.linearLayout13.visibility = View.VISIBLE
            }

        })*/
    }


}