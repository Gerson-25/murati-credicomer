package sv.com.credicomer.murativ2.ui.alliance.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentPromotionDetailBinding
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS
import sv.com.credicomer.murativ2.ui.alliance.models.RatingUsers
import sv.com.credicomer.murativ2.ui.alliance.viewModels.PromotionsDetailViewModel
import timber.log.Timber


class PromotionDetailFragment : Fragment() {

    private lateinit var binding: FragmentPromotionDetailBinding
    private lateinit var viewModel: PromotionsDetailViewModel
    private lateinit var promotion: PromotionFS
    private var auth = FirebaseAuth.getInstance().currentUser
    private lateinit var mainViewModel: MainViewModel
    private lateinit var collectionPath: String
    private lateinit var subCollectionPath: String
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_promotion_detail, container, false)

        viewModel = ViewModelProvider(this).get(PromotionsDetailViewModel::class.java)
        navController = findNavController()

        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        collectionPath=mainViewModel.allianceCollectionPath.value.toString()
        subCollectionPath=mainViewModel.allianceSubCollectionPath.value.toString()

        val args = arguments?.let { PromotionDetailFragmentArgs.fromBundle(it) }

        if (args != null) {
            promotion = args.promotion
            viewModel._promotion.value = promotion
            binding.promotion = promotion
            Timber.d("PROMO %s","values are not null")
            Glide.with(this).load(promotion.image).into(binding.imageViewPromotion)
            Glide.with(this).load(promotion.establishment_image).into(binding.imageLogo2)

        }else{

            Timber.d("PROMO %s","values are null")
        }

        binding.btnRatePromotion.setOnClickListener {
            val ratingDialogFragment = RatingDialogFragment.newInstance("", "", promotion)
            ratingDialogFragment.show(parentFragmentManager, "rating dialog")
        }

        /*promotion.rated_users?.filter { it.email == auth?.email }.apply {

            if (this.isNullOrEmpty()) {

                binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                    val alertDialog = AlertDialog.Builder(requireContext())
                        .setTitle(context?.getString(R.string.dialog_rating_title))
                        .setMessage("${context?.getString(R.string.dialog_rating_description)} $rating")
                        .setPositiveButton(context?.getString(R.string.dialog_confirmation_rating)) { _, _ ->

                            viewModel.updateRating(promotion, rating.toInt(),collectionPath,subCollectionPath)
                            binding.ratingBar.isEnabled = false
                            promotion.rated_users?.add(RatingUsers(auth?.email,rating.toInt()))
                        }
                        .setNegativeButton(context?.getString(R.string.dialog_confirmation_negative_btn)) { _, _ ->

                        }
                    alertDialog.show()


                }


            } else {
                binding.ratingBar.setIsIndicator(true)
                binding.ratingBar.rating = this[0].rating?.toFloat() ?: 0F
            }

        }*/


        binding.buttonShowCard.setOnClickListener {
            navController.navigate(PromotionDetailFragmentDirections.actionPromotionDetailFragmentToCreateCarnetFragment(0))
        }




        return binding.root
    }


}
