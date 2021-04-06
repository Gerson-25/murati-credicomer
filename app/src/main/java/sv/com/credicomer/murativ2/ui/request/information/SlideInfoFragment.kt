package sv.com.credicomer.murativ2.ui.request.information

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.creditscomer.viewmodel.UserInfoViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentSlideInfoBinding
import sv.com.credicomer.murativ2.ui.request.information.steps.*


private const val NUM_PAGES = 3
class SlideInfoFragment : Fragment(){

    var handler = Handler()
    lateinit var binding: FragmentSlideInfoBinding
    private lateinit var viewModel: UserInfoViewModel
    var adapter: ScreenSlideAdapter? = null
    lateinit var navController: NavController
    lateinit var pinfo: HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slide_info, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ScreenSlideAdapter(parentFragment!!)
        navController = Navigation.findNavController(view)


        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if (binding.infoSlidePager.currentItem != 0){
                handler.postDelayed({
                    binding.infoSlidePager.currentItem = binding.infoSlidePager.currentItem - 1
                }, 500)
            }
            else{
                this.remove()
            }
        }

        pinfo = hashMapOf()

        viewModel.pageScroll.observe(viewLifecycleOwner, Observer {
            handler.postDelayed({
                binding.infoSlidePager.currentItem = it
            }, 2000)
        })

        viewModel.personalInfo.observe(viewLifecycleOwner, Observer {
            //pinfo.putAll(it)
            if(binding.infoSlidePager.currentItem == 2){
                //viewModel.sendInfo(pinfo)
            }
        })

        binding.infoSlidePager.adapter = adapter
        binding.infoSlidePager.isFakeDragging
        var progressBarInfo = binding.progressInfo
        fun setAnimation(end:Int){
            val progressAnimator = ObjectAnimator.ofInt(progressBarInfo, "progress", end)
           progressAnimator.start()
        }

        binding.infoSlidePager.isUserInputEnabled = false

        binding.infoSlidePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            @RequiresApi(Build.VERSION_CODES.O)

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val title = binding.textFormTitle
                val buttonScroll = binding.completeButton
                when(position){
                    0 -> { setAnimation(33)
                        //title.text = "Informacion general"
                        }
                    1 -> { setAnimation(66)
                        //title.text = "Informacion laboral"
                            }
                    2 -> { setAnimation(100)
                        //title.text = "Informacion Adicional"
                        }
                }

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

    }

    inner class ScreenSlideAdapter(activity: Fragment): FragmentStateAdapter(activity){
        override fun getItemCount(): Int =
            NUM_PAGES

        override fun createFragment(position: Int) = when (position){
            0 -> StepOneFragment()
            1-> StepTwoFragment()
            2-> StepThreeFragment()
            3-> MapsFragment()
            else -> StepFourFragment()
        }

    }

}