package sv.com.credicomer.murativ2.ui.travel.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentDetailRecordBinding
import sv.com.credicomer.murativ2.ui.travel.models.Record
import sv.com.credicomer.murativ2.ui.travel.viewModel.DetailRecordViewModel
import sv.com.credicomer.murativ2.ui.travel.viewModel.TravelRegistrationViewModel

class DetailRecordFragment : Fragment() {

    lateinit var id:String
    private lateinit var viewModel: DetailRecordViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentDetailRecordBinding
    private lateinit var vieModelMainViewModel: MainViewModel
    lateinit var travelViewModel: TravelRegistrationViewModel

    //objeto que contiene los datos del detalle
    lateinit var objDetailData: Record
    var isActive: Boolean? = true //true= viaje actual

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_record, container, false)
        navController = findNavController()
        viewModel = ViewModelProvider(this).get(DetailRecordViewModel::class.java)

        if (!arguments?.isEmpty!!) {
            val args = arguments?.let { DetailRecordFragmentArgs.fromBundle(it) }
            objDetailData = args!!.record // isActive = args!!.isActive
            id = args.id
        }

        travelViewModel = ViewModelProvider(this).get(TravelRegistrationViewModel::class.java)

        vieModelMainViewModel =
            activity.run { ViewModelProvider(this!!).get(MainViewModel::class.java) }

        //isActive = vieModelMainViewModel.isActive.value


        travelViewModel.getTravel(id)

        travelViewModel.isActive.observe(viewLifecycleOwner, Observer {
            if(it == false){
                binding.deleteButtonDetail.visibility = View.GONE
                binding.editButtonDetail.visibility = View.GONE
            }
        })

        fillDetail()
        //animacion para la card que contiene la info(solo de entrada)
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        container!!.startAnimation(animation)
        //verifica de donde proviene para saber si mostrar o no los botones de edit/delete
        if (isActive == false) {
            binding.deleteButtonDetail.visibility = View.GONE
            binding.editButtonDetail.visibility = View.GONE
        }

        val parentThatHasBottomSheetBehavior =
            binding.frameDetailContainer

        var mBottomBehavior = BottomSheetBehavior.from(parentThatHasBottomSheetBehavior)
        mBottomBehavior.peekHeight = 110
        binding.headerContainer.requestLayout()

        mBottomBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                /*BottomSheetBehavior.STATE_COLLAPSED = 4 */
                BottomSheetBehavior.STATE_EXPANDED
                when (newState)
                {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.slideIcon.rotation = 0f}
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.slideIcon.rotation = 180f
                    }
                }
            }

        })
        binding.frameDetailContainer.setOnClickListener {
            binding.photoRecordDetail.isZoomable = false
        }
        mBottomBehavior.isHideable = false
        mBottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED


        /*binding.arrowDetail.setOnClickListener {
            //animacion para ocultar parte del frame al dar clic en la flecha
            animtationFrame()
        }*/
        /*binding.icInfo.setOnClickListener {
            //animacion al dar clic en la foto
            /*animtationFrame()*/
            val action = DetailRecordFragmentDirections.actionDetailRecordFragmentToDetailsBottomFragment()
            Navigation.findNavController(it).navigate(action)
        }*/

        binding.deleteButtonDetail.setOnClickListener{
            showDialog()
        }

        binding.editButtonDetail.setOnClickListener {
            navController.navigate(
                DetailRecordFragmentDirections.actionDetailRecordFragmentToAddRecordFragment(
                    vieModelMainViewModel.travelId.value.toString(),
                    objDetailData
                )
            )
        }
        return binding.root
    }

    private fun fillDetail() {
        //Seteo de datos
        binding.recordNameDetail.text = objDetailData.recordName
        binding.recordDateDetail.text = objDetailData.recordDate
        binding.recordDescriptionDetail.text = objDetailData.recordDescription
        binding.recordPriceDetail.text = "$" + objDetailData.recordMount

        //Se asigna la imagen
        Glide.with(this).load(objDetailData.recordPhoto).into(binding.photoRecordDetail)


        //se toma la categoria y dependiendo de la que se obtiene se setea
        when (objDetailData.recordCategory) {
            "0" -> {
                binding.imageCategoryDetail.setImageResource(R.drawable.ic_cat_food)
                binding.categoyTittleDetail.text = "Comida"
                binding.categoyTittleDetail.setTextColor(Color.parseColor("#FEC180"))
            }

            "1" -> {
                binding.imageCategoryDetail.setImageResource(R.drawable.ic_cat_car)
                binding.categoyTittleDetail.text = "Transporte"
                binding.categoyTittleDetail.setTextColor(Color.parseColor("#FBE339"))
            }

            "2" -> {
                binding.imageCategoryDetail.setImageResource(R.drawable.ic_cat_hotel)
                binding.categoyTittleDetail.text = "Hospedaje"
                binding.categoyTittleDetail.setTextColor(Color.parseColor("#BAFC8B"))
            }

            "3" -> {
                binding.imageCategoryDetail.setImageResource(R.drawable.ic_cat_other)
                binding.categoyTittleDetail.text = "Otro"
                binding.categoyTittleDetail.setTextColor(Color.parseColor("#E39BFC"))
            }
        }
    }

    /*fun animtationFrame() {
        if (binding.arrowDetail.rotation == 0f) {
            val animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            binding.frameDetailContainer.startAnimation(animation2)
            Handler().postDelayed({
                binding.recordNameDetail.visibility = View.VISIBLE
                binding.recordDateDetail.visibility = View.VISIBLE
                binding.recordDescriptionDetail.visibility = View.VISIBLE
                binding.recordPriceDetail.visibility = View.VISIBLE
                binding.categoyTittleDetail.visibility = View.VISIBLE
                binding.imageCategoryDetail.visibility = View.VISIBLE
                binding.textViewDescripTittle.visibility = View.VISIBLE
                binding.arrowDetail.rotation = 180f
                if (isActive == true) {
                    binding.deleteButtonDetail.visibility = View.VISIBLE
                    binding.editButtonDetail.visibility = View.VISIBLE
                }
            }, 250)
        } else {
            val animation2 = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            binding.frameDetailContainer.startAnimation(animation2)
            Handler().postDelayed({
                binding.recordNameDetail.visibility = View.GONE
                binding.recordDateDetail.visibility = View.GONE
                binding.recordDescriptionDetail.visibility = View.GONE
                binding.recordPriceDetail.visibility = View.GONE
                binding.categoyTittleDetail.visibility = View.GONE
                binding.imageCategoryDetail.visibility = View.GONE
                binding.deleteButtonDetail.visibility = View.GONE
                binding.editButtonDetail.visibility = View.GONE
                binding.textViewDescripTittle.visibility = View.GONE
                binding.arrowDetail.rotation = 0f
            }, 250)
        }
    }*/

    private fun showDialog(){
        val alertDialog = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Eliminar el registro")
                .setMessage("¿Seguro que dese eliminar el registro de su viaje?")
                .setPositiveButton("ELIMINAR"){_, _ ->
                    //metodo para eliminar el registro
                    viewModel.deleteRecord(objDetailData.recordPhoto!!,
                                            vieModelMainViewModel.travelId.value.toString(),
                                            objDetailData.recordId.toString())

                    Toast.makeText(context, "Registro Eliminado", Toast.LENGTH_SHORT).show()

                    //send to homeTravel
                    navController.popBackStack()
                }
                .setNegativeButton("CANCELAR"){_, _ ->
                    Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()}
        }
        alertDialog!!.show()
    }

    fun CheckIsActive(){
        travelViewModel
    }
}
