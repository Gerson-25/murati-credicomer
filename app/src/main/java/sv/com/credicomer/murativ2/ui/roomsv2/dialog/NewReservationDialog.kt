package sv.com.credicomer.murativ2.ui.roomsv2.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.DialogNewReservationBinding

class NewReservationDialog: DialogFragment() {

    private lateinit var binding:DialogNewReservationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_new_reservation, container, false)
        return binding.root
    }
}