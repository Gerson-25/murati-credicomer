package sv.com.credicomer.murativ2.ui.request.information.steps

import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.creditscomer.viewmodel.UserInfoViewModel
import com.uttampanchasara.pdfgenerator.CreatePdf
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStepOneBinding
import sv.com.credicomer.murativ2.ui.request.information.SlideInfoFragmentDirections
import sv.com.credicomer.murativ2.ui.request.model.FamilyReferences
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfo
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfoModel
import sv.com.credicomer.murativ2.ui.request.model.PersonalReferences
import sv.com.credicomer.murativ2.utils.GUARANTEES
import sv.com.credicomer.murativ2.utils.OCUPPATIONS
import sv.com.credicomer.murativ2.utils.PdfCreator
import sv.com.credicomer.murativ2.utils.departamentos
import sv.com.credicomer.murativ2.utils.edit_text.dateDrawer
import sv.com.credicomer.murativ2.utils.edit_text.initField
import sv.com.credicomer.murativ2.utils.edit_text.saveInfo

class StepOneFragment : Fragment() {
    lateinit var viewModel: UserInfoViewModel
    lateinit var binding: FragmentStepOneBinding
    lateinit var formFields: List<EditText>
    lateinit var valuesFormFields: List<String>
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_one,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var otherIncomes = 0
        var garantia = "no garantias"
        var homeType = "propia"
        var debtValue = 0

        binding.spinnerDepartamento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val departamento = binding.spinnerDepartamento.selectedItem.toString()

                ArrayAdapter(activity!!, R.layout.custom_spinner_layout, departamentos[departamento]!!)
                    .also {
                        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerMunicipio.adapter = it
                    }
            }
        }

        showInputtext(binding.checkboxTwo, binding.edittextTwo)
        showInputtext(binding.checkboxThree, binding.edittextThree)
        showInputTextFromRadio(binding.radiobuttonGuarantee, binding.spinnerGuarantee)
        showInputTextFromRadio2(binding.radiobuttonDebt, binding.fieldDebt)

        binding.edittextDate.dateDrawer()

        formFields = listOf(
            binding.edittextName,
            binding.edittextPhone,
            binding.edittextAddress,
            binding.edittextEspecificAddress,
            binding.edittextDate,
            binding.edittextRfName,
            binding.edittextRfContact,
            binding.edittextRfRelationship,
            binding.edittextRfNameSecond,
            binding.edittextRfContactSecond,
            binding.edittextRfRelationshipSecond,
            binding.edittextRpName,
            binding.edittextRpContact,
            binding.edittextRpNameSecond,
            binding.edittextRpContactSecond,
            binding.editTextComments
        )

        binding.radiobuttonType.setOnCheckedChangeListener { radioGroup, i ->
            homeType = if (binding.radioButtonPropia.isChecked) binding.radioButtonPropia.text.toString() else binding.radioButtonAlquilada.text.toString()
        }

        formFields.forEach {
            it.initField()
        }

        binding.btnSendInfo.setOnClickListener {

            valuesFormFields = formFields.map {
                it.saveInfo()
            }

            var emptyField = false
            formFields.forEachIndexed { i , v ->
                if (valuesFormFields[i].isEmpty()){
                    showError(v)
                    emptyField = true
                }
            }

            if (emptyField){
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
            else{

                if (binding.radiobuttonGuarantee.isChecked){
                    garantia = binding.spinnerGuarantee.selectedItem.toString()
                }

                if (binding.checkboxThree.isChecked){
                    otherIncomes += binding.edittextThree.text.toString().toInt()
                }

                if (binding.checkboxTwo.isChecked){
                    otherIncomes += binding.edittextTwo.text.toString().toInt()
                }

                if(binding.radiobuttonDebt.isChecked){
                    debtValue += binding.fieldDebt.text.toString().toInt()
                }

                val myInfo = GeneralInfoModel(
                    "",
                    debtValue,
                    viewModel.getEmail()!!,
                    viewModel.getId()!!,
                    binding.edittextName.saveInfo(),
                    binding.edittextPhone.saveInfo(),
                    binding.spinnerMunicipio.saveInfo(),
                    binding.spinnerDepartamento.saveInfo(),
                    binding.edittextAddress.saveInfo(),
                    binding.edittextEspecificAddress.saveInfo(),
                    homeType,
                    binding.edittextDate.saveInfo(),
                    otherIncomes,
                    garantia,
                    binding.edittextRfContact.saveInfo(),
                    binding.edittextRfName.saveInfo(),
                    binding.edittextRfRelationship.saveInfo(),
                    binding.edittextRfContactSecond.saveInfo(),
                    binding.edittextRfContactSecond.saveInfo(),
                    binding.edittextRfRelationshipSecond.saveInfo(),
                    binding.edittextRpContact.saveInfo(),
                    binding.edittextRpName.saveInfo(),
                    binding.edittextRpContactSecond.saveInfo(),
                    binding.edittextRpNameSecond.saveInfo(),
                    binding.editTextComments.saveInfo()
                )

                navController = Navigation.findNavController(it)
                navController.navigate(SlideInfoFragmentDirections.actionSlideInfoFragmentToCalculatorFragment(myInfo))

            }
        }

        initSpinners()

    }

    fun initSpinners(){

        val dep = departamentos.keys.toList()

        ArrayAdapter(activity!!, R.layout.custom_spinner_layout, dep)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerDepartamento.adapter = it
            }

        ArrayAdapter(activity!!, R.layout.custom_spinner_layout, GUARANTEES)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerGuarantee.adapter = it
            }
    }

    fun showInputtext(p0: CheckBox, p1: EditText): Boolean{
        var selected = false
        p0.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                p1.visibility = View.VISIBLE
            }
            else{
                p1.visibility = View.GONE
            }
            selected = b
        }
        return selected
    }

    fun showError(view: View){
        view.background = resources.getDrawable(R.drawable.input_new_reservation_bg_error)
    }


    fun showInputTextFromRadio(p0: RadioButton, p1: Spinner):Boolean{
        var selected = false
        p0.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                p1.visibility = View.VISIBLE
            }
            else{
                p1.visibility = View.GONE
            }
            selected = b
        }
        return selected
    }

    fun showInputTextFromRadio2(p0: RadioButton, p1: EditText):Boolean{
        var selected = false
        p0.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                p1.visibility = View.VISIBLE
            }
            else{
                p1.visibility = View.GONE
            }
            selected = b
        }
        return selected
    }
}
