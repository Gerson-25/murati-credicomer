package sv.com.credicomer.murativ2.ui.alliance.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentRatingDialogBinding
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS
import sv.com.credicomer.murativ2.ui.alliance.models.RatingUsers
import sv.com.credicomer.murativ2.ui.alliance.viewModels.PromotionsDetailViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PROMO = "PROMO"


class RatingDialogFragment : DialogFragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentRatingDialogBinding
    private lateinit var promotion: PromotionFS
    private lateinit var viewModel: PromotionsDetailViewModel
    private var auth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            promotion = it.getParcelable(PROMO)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(PromotionsDetailViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            this.dismiss()
        }

        sendRating()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun sendRating(){
        /*promotion.rated_users?.filter { it.email == auth?.email }.apply {

        if (this.isNullOrEmpty()) {
            binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            *//*val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(context?.getString(R.string.dialog_rating_title))
            .setMessage("${context?.getString(R.string.dialog_rating_description)} $rating")
            .setPositiveButton(context?.getString(R.string.dialog_confirmation_rating)) { _, _ ->

            }
            .setNegativeButton(context?.getString(R.string.dialog_confirmation_negative_btn)) { _, _ ->

            }
            alertDialog.show()*//*
                binding.sendRating.setOnClickListener {
                    if (binding.editTextComment.text.isNullOrEmpty() || binding.editTextReceipe.text.isNullOrEmpty()){
                        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                    }else{
                        viewModel.updateRating(promotion, rating.toInt(),"alliance","promotions", binding.editTextComment.text.toString(), binding.editTextReceipe.text.toString())
                        //binding.ratingBar.isEnabled = false
                        promotion.rated_users?.add(RatingUsers(auth?.email,rating.toInt()))
                    }
                }
            }

        } else {
        //binding.ratingBar.setIsIndicator(true)
        //binding.ratingBar.rating = this[0].rating?.toFloat() ?: 0F
            }
        }*/
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (!it){
                Toast.makeText(context, "Gracias por tu feedback!", Toast.LENGTH_SHORT).show()
            }
        })

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.sendRating.setOnClickListener {
                if (binding.editTextComment.text.isNullOrEmpty() || binding.editTextReceipe.text.isNullOrEmpty()){
                    Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.updateRating(promotion, rating.toInt(),"alliance","promotions", binding.editTextComment.text.toString(), binding.editTextReceipe.text.toString())
                    //binding.ratingBar.isEnabled = false
                    promotion.rated_users?.add(RatingUsers(auth?.email,rating.toInt()))
                    this.dismiss()

                }
            }
        }

    }

    companion object {
        fun newInstance(param1: String, param2: String, promo: PromotionFS) =
            RatingDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putParcelable(PROMO, promo)
                }
            }
    }
}