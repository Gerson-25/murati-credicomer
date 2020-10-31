package sv.com.credicomer.murativ2.ui.roomsv2.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerRoom : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val date =DatePickerDialog(requireContext(), listener, year, month, day)
            date.datePicker.minDate = c.timeInMillis
            date.show()

        return date
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerRoom {
            val fragment = DatePickerRoom()
            fragment.listener = listener
            return fragment
        }
    }

}