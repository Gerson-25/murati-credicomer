package sv.com.credicomer.murativ2.ui.request.information.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.creditscomer.view.information.CalculatorFragmentArgs
import com.example.creditscomer.viewmodel.UserInfoViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStepTwoBinding
import sv.com.credicomer.murativ2.ui.travel.fragments.DatePickerFragment
import sv.com.credicomer.murativ2.utils.GUARANTEES

class StepTwoFragment : Fragment() {

    lateinit var binding: FragmentStepTwoBinding
    lateinit var userViewModel: UserInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_two, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = StepTwoFragmentArgs.fromBundle(arguments!!)
        val requestInfo = args.id

        userViewModel.getRequest(requestInfo)

        userViewModel.requestList.observe(viewLifecycleOwner, Observer {
            binding.textMonto.text = "Solicitud por el monto de: ${it[0].cantidad}"
            binding.textPlazo.text = "Plazo solicitado: ${it[0].plazo}"
            binding.textCouta.text = "Cuota deseada: ${it[0].couta_mensual}"
            binding.textFecha.text = "Fecha de solicitud: ${it[0].fecha_solicitud}"
        })

    }

}

