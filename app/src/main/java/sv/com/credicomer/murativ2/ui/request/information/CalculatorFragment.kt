package com.example.creditscomer.view.information


import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.creditscomer.viewmodel.UserInfoViewModel
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentCalculatorBinding
import sv.com.credicomer.murativ2.utils.GUARANTEES
import sv.com.credicomer.murativ2.utils.TERMS
import sv.com.credicomer.murativ2.utils.departamentos
import sv.com.credicomer.murativ2.utils.edit_text.addText
import sv.com.credicomer.murativ2.utils.edit_text.initField
import sv.com.credicomer.murativ2.utils.edit_text.saveInfo

class CalculatorFragment : Fragment() {

    private lateinit var binding: FragmentCalculatorBinding
    lateinit var navController: NavController
    lateinit var viewModel:UserInfoViewModel
    lateinit var mainViewModel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }
        mainViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator, container, false )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CalculatorFragmentArgs.fromBundle(arguments!!)
        val myInfo = args.formInfo

        val terms = arrayListOf(
            "6 meses",
            "1 año",
            "2 años",
            "3 años",
            "4 años",
            "5 años",
            "6 años",
            "7 años",
            "8 años",
            "9 años",
            "10 años"
        )

        initSpinners()

        binding.creditTerm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.creditTerm.let {
                    when(it.progress){
                        0 -> {
                            binding.creditTimeBox.text = terms[0]
                        }
                        10 -> {
                            binding.creditTimeBox.text = terms[1]
                        }
                        20 -> {
                            binding.creditTimeBox.text = terms[2]
                        }
                        30 -> {
                            binding.creditTimeBox.text = terms[3]
                        }
                        40 -> {
                            binding.creditTimeBox.text = terms[4]
                        }
                        50 -> {
                            binding.creditTimeBox.text = terms[5]
                        }
                        60 -> {
                            binding.creditTimeBox.text = terms[6]
                        }
                        70 -> {
                            binding.creditTimeBox.text = terms[7]
                        }
                        80 -> {
                            binding.creditTimeBox.text = terms[8]
                        }
                        90 -> {
                            binding.creditTimeBox.text = terms[9]
                        }
                        100 -> {
                            binding.creditTimeBox.text = terms[10]
                        }

                    }
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        var formField = arrayListOf(
            binding.creditAmountBox,
            binding.monthlyPaymentBox
        )

        formField.forEach {
            it.initField()
        }

        binding.creditAmountBox.addText("$", 1)

        navController = Navigation.findNavController(view)

        binding.confirmButton.setOnClickListener {
            if (binding.creditAmountBox.text.isNullOrEmpty()){
                binding.creditAmountBox.let {
                    showError(it)
                    it.initField()
                }
                Toast.makeText(context, "Ingresa el monto a solicitar", Toast.LENGTH_SHORT).show()
            }
            else{
                myInfo.cantidad = binding.creditAmountBox.text.toString().toInt()
                myInfo.plazo = binding.creditTermSpinner.saveInfo()
                myInfo.couta_mensual = binding.monthlyPaymentBox.text.toString().toInt()

                navController = Navigation.findNavController(it)
                navController.navigate(CalculatorFragmentDirections.actionCalculatorFragmentToConfirmationFragment(myInfo))

            }
        }

    }

    fun initSpinners(){

        val dep = departamentos.keys.toList()

        ArrayAdapter(activity!!, R.layout.custom_spinner_layout, TERMS)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.creditTermSpinner.adapter = it
            }
    }

    fun showError(view: View){
        view.background = resources.getDrawable(R.drawable.input_new_reservation_bg_error)
    }

}

