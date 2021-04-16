package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.app.Activity
import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentSelectionDialogBinding
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.adapters.MyItemRecyclerViewAdapter
import sv.com.credicomer.murativ2.ui.profile.view.adapters.SelectReceiverAdapter
import sv.com.credicomer.murativ2.ui.profile.view.fragments.dummy.DummyContent
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel
import java.util.*

private const val EMAIL = "EMAIL"
private const val FROM_USER = "FROM_USER"
private const val RECEIVER = "RECEIVER"

class SelectionDialogFragment : Fragment() {

    private var columnCount = 1

    lateinit var binding: FragmentSelectionDialogBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewModel:ProfileViewModel
    private var receiver:String? = null
    private var email: String? = null
    private var fromUser: Boolean? = null
    lateinit var date:Calendar
    private lateinit var myNavController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selection_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedColor = resources.getColor(R.color.colorPrimary)

        myNavController = Navigation.findNavController(view)

        var selectedUsers = mutableListOf<UserCarnet>()

        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner, androidx.lifecycle.Observer {usersList ->
            val users = usersList.toMutableList()
            binding.list.apply{
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    MyItemRecyclerViewAdapter(
                        users,
                        object : OnItemListClickListener {
                            override fun setOnItemListClickListener(userCarnet: UserCarnet) {
                                selectedUsers.add(userCarnet)
                                val newChip = Chip(context, null, R.style.Widget_MaterialComponents_Chip_Entry)
                                newChip.isCloseIconVisible = true
                                newChip.text = "${userCarnet.email}"
                                newChip.contentDescription = userCarnet.name
                                newChip.setOnCloseIconClickListener {chip ->
                                    users.add(selectedUsers.find { it.name ==  chip.contentDescription}!!)
                                    selectedUsers.remove(selectedUsers.find { it.name ==  chip.contentDescription}!!)
                                    binding.selectedUsersContainer.removeView(chip)
                                    if (selectedUsers.isEmpty()){
                                        binding.floatingActionButton2.visibility = View.GONE
                                    }
                                }
                                binding.selectedUsersContainer.addView(newChip)
                                binding.floatingActionButton2.visibility = View.VISIBLE
                                users.remove(userCarnet)
                                //myNavController.navigate(SelectionDialogFragmentDirections.actionSelectionDialogFragmentToBlankFragment())
                            }
                        }
                        , selectedColor, context)
                        binding.userAc.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                        val newValues = usersList.filter {
                            it.email!!.contains(p0!!)
                        }

                        binding.list.adapter = MyItemRecyclerViewAdapter(newValues.toMutableList(),
                            object : OnItemListClickListener {
                                override fun setOnItemListClickListener(userCarnet: UserCarnet) {
                                selectedUsers.add(userCarnet)
                                val newChip = Chip(context, null, R.style.Widget_MaterialComponents_Chip_Entry)
                                newChip.isCloseIconVisible = true
                                newChip.text = "${userCarnet.email}"
                                newChip.contentDescription = userCarnet.name
                                newChip.setOnCloseIconClickListener { chip ->
                                    users.add(selectedUsers.find { it.name == chip.contentDescription }!!)
                                    selectedUsers.remove(selectedUsers.find { it.name == chip.contentDescription }!!)
                                    binding.selectedUsersContainer.removeView(chip)
                                    if (selectedUsers.isEmpty()) {
                                        binding.floatingActionButton2.visibility = View.GONE
                                    }
                                } }
                            },selectedColor, context!!)

                    }
                })
            }

        })


        binding.floatingActionButton2.setOnClickListener {
            myNavController.navigate(SelectionDialogFragmentDirections.actionSelectionDialogFragmentToBlankFragment(selectedUsers.toTypedArray()))
        }

    }


}

interface OnItemListClickListener{
    fun setOnItemListClickListener(userCarnet: UserCarnet)
}