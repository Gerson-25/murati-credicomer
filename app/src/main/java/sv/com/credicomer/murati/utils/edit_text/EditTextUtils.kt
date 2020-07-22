package sv.com.credicomer.murati.utils.edit_text

import android.text.InputFilter
import java.text.DecimalFormatSymbols

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