package sv.com.credicomer.murativ2.ui.roomsv2.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.LayoutUserDialogBinding
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

class UsersDialog: DialogFragment() {

    lateinit var chipList: List<Chip>
    lateinit var binding: LayoutUserDialogBinding
    lateinit var viewModel:RoomDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity.run {
            ViewModelProvider(requireActivity()).get(RoomDetailViewModel::class.java)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_user_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonAdd.setOnClickListener {
            if (chipList[0].visibility == View.GONE){
                Toast.makeText(context, "No hay usuarios agregados", Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.getEmail(takeEmails())
                this@UsersDialog.dismiss()
            }
        }


        chipList = listOf(
            binding.chipOne,
            binding.chipTwo,
            binding.chipThree,
            binding.chipFour,
            binding.chipFive,
            binding.chipSix,
            binding.chipSeven,
            binding.chipEigth
        )

        var emailsCounter = 0

        binding.addUserButton.setOnClickListener {
            if (binding.inputEmails.text.isNullOrEmpty()){
                Toast.makeText(context, "Email vacio!", Toast.LENGTH_SHORT).show()
            }else{
                chipList[emailsCounter].apply {
                    visibility = View.VISIBLE
                    text = binding.inputEmails.text
                    isCheckedIconVisible = false
                    emailsCounter++
                    binding.inputEmails.text.clear()
                    setOnCloseIconClickListener {
                        visibility = View.GONE
                        emailsCounter--
                    }
                }
            }

        }
    }

    fun takeEmails():MutableList<String>{

        val emails = mutableListOf<String>()
        chipList.forEach {
            if (it.visibility == View.VISIBLE){
                emails.add(it.text.toString())
            }
        }
        return emails
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}