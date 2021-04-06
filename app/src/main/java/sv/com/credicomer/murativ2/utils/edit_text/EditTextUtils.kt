package sv.com.credicomer.murativ2.utils.edit_text

import android.annotation.SuppressLint
import android.os.Build
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import com.afollestad.date.month
import com.afollestad.date.year
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.roomsv2.models.Date
import java.text.DecimalFormatSymbols
import java.text.FieldPosition
import java.util.*

object EditTextUtils {

    private var decimalFormatSymbols = DecimalFormatSymbols()

    private val decimalNumber: InputFilter= InputFilter { string, start, end, dest, dstart, send ->
        val indexPoint = dest.toString().indexOf(decimalFormatSymbols.decimalSeparator)

        if (indexPoint == -1){
            return@InputFilter string
        }
        else {
            val decimal = send -(indexPoint+1)
            return@InputFilter if (decimal<2) string else ""
        }
        null
    }

    private val onlyLetter: InputFilter = InputFilter { string, start, end, _, _, _ ->
        (start until end)
            .filter { Character.isDigit(string[it]) }
            .forEach { _ -> return@InputFilter "" }
        null
    }

    fun getDecimalNumber(): Array<InputFilter> = arrayOf(decimalNumber)
    fun getOnlyLetter(): Array<InputFilter> = arrayOf(onlyLetter)

}

fun TextView.saveInfo():String{
    return this.text.toString()
}

fun Spinner.saveInfo():String {
    return this.selectedItem.toString()
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
fun TextView.initField(){
    this.doOnTextChanged { text, start, count, after ->
        this.background = resources.getDrawable(R.drawable.input_new_reservation_bg)
    }
}

fun EditText.dateDrawer(){
    this.doOnTextChanged { text, start, count, after ->
        
        if (text!!.length == 2 && count == 0 || text.length == 5 && count == 0){
            this.text.insert(text.length, "/")
        }
        else if(text.length == 2 && count == 1 || text.length == 5 && count == 1){
            this.text.delete(text.length, text.length)
        }

    }
    this.doBeforeTextChanged { text, start, count, after ->

            if (text!!.length == 9){
                this.text.replace(text.length, text.length, " ")
            }
    }
}

fun EditText.addText(value: String, position: Int){
    this.doOnTextChanged { text, start, count, after ->
        if (text!!.length == position){
            this.text.insert(position, "${value}")
        }
    }
}

class DateWorker(){
    companion object{
        fun getDate(number:Int, selectedValue:Int):Date{
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, number).toString()
            val dateSplitter = date.time.toString().split(" ")
            return Date(dateSplitter[2], dateSplitter[0], number == selectedValue-1, date.time.toString(), date.month.toString(), date.year.toString())
        }
    }
}


