package sv.com.credicomer.murati.ui.roomsv2

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import sv.com.credicomer.murati.ui.roomsv2.models.RoomResult
import timber.log.Timber

fun showFilterDialog(filterList: MutableList<String>, context: Context) {

    lateinit var filterDialog: AlertDialog

    // Initialize an array of colors
    val arrayOfFilterList = filterList.toTypedArray()

    // Initialize a boolean array of checked items
    //val arrayChecked = booleanArrayOf(true,false,true,false,false,false)

    val arrayChecked = BooleanArray(arrayOfFilterList.size)

    // Initialize a new instance of alert dialog builder object
    val builder = AlertDialog.Builder(context)

    // Set a title for alert dialog
    builder.setTitle("Selecciona una opcion")

    // Define multiple choice items for alert dialog
    builder.setMultiChoiceItems(arrayOfFilterList, arrayChecked) { _, which, isChecked ->
        // Update the clicked item checked status
        arrayChecked[which] = isChecked

        // Get the clicked item
        val equip = arrayOfFilterList[which]

        // Display the clicked item text
        Toast.makeText(context, "$equip clicked.", Toast.LENGTH_LONG).show()
    }


    // Set the positive/yes button click listener
    builder.setPositiveButton("OK") { _, _ ->
        // Do something when click positive button

    }
    builder.setNegativeButton("Cancel") { _, _ ->

    }

    // Initialize the AlertDialog using builder object
    filterDialog = builder.create()

    // Finally, display the alert dialog
    filterDialog.show()
}

fun textEquipmentConcat(equipmentList: MutableList<String>): String {

    var chain = ""

    equipmentList.forEachIndexed { index, item ->

        chain += if (index == equipmentList.size - 1) {
            item
        } else {
            "$item,"
        }

    }

    return chain

}

fun mergeLists(date: String, sortedList: MutableList<RoomResult>): MutableList<RoomResult> {

    var finalList: MutableList<RoomResult> = mutableListOf()

    var checkMerge = false
    val singular: MutableList<RoomResult> = mutableListOf()

    sortedList.forEachIndexed { index, item ->

        sortedList.forEachIndexed { index2, item2 ->
            if (index2 > index) {

                if (item.interval!![1] == item2.interval!![0]) {

                    finalList.any { it.interval!!.contains(item2.interval!![0]) }.apply {

                        if (this) {

                            if (finalList.isNotEmpty() && finalList.last().interval!![1] == item2.interval!![0]) {

                                val hora =
                                    "${finalList.last().interval!![0]}-${item2.interval!![1]}"
                                val aux = finalList.last()
                                finalList.removeAt(finalList.size - 1)

                                checkMerge = true
                                finalList.add(
                                    RoomResult(
                                        hora,
                                        mutableListOf(aux.interval!![0], item2.interval!![1])
                                    )
                                )
                            }


                        } else {

                            val hora = "${item.interval!![0]}-${item2.interval!![1]}"

                            checkMerge = true
                            finalList.add(
                                RoomResult(
                                    hora,
                                    mutableListOf(item.interval!![0], item2.interval!![1])
                                )
                            )

                        }
                    }


                } else {

                    if (!finalList.any { it.hour == item.hour } && !singular.any { it.hour == item.hour } && !checkMerge)
                        singular.add(RoomResult((item.hour), item.interval))
                }

            }


        }
        checkMerge = false

    }
    if (sortedList.isNotEmpty()) {
        singular.add(sortedList.last())
    }

    //val fbdate = "11-03-2020"
    val singleListParsed = mergeGroupedIntervalsAndSingleIntervals(date, finalList, singular)

    val list = mutableListOf<String>()

    finalList = (finalList + singleListParsed).toMutableList()
    // finalList=sortRoomsFun(finalList)
    finalList.forEach {
        list.add(it.interval.toString())
    }
    Timber.d("FECHLIST %s", "$list")

    return finalList

}

fun sortRoomsFun(roomList: MutableList<RoomResult>): MutableList<RoomResult> {

    val sortRooms: (RoomResult) -> DateTime = {
        DateTime.parse(it.sortParam, DateTimeFormat.forPattern("dd-MM-yyyy|HH:mm"))
    }

    return roomList.sortedBy(sortRooms).toMutableList()

}

fun mergeGroupedIntervalsAndSingleIntervals(
    date: String,
    mergedIntervals: MutableList<RoomResult>,
    SingleIntervals: MutableList<RoomResult>
): MutableList<RoomResult> {

    var singleListParsed = SingleIntervals
    SingleIntervals.forEach { sing ->

        mergedIntervals.forEach { fin ->

            if (compareTime(date, fin.interval!![0], fin.interval!![1], sing.interval!![0])) {
                singleListParsed = singleListParsed.filter { it.hour != sing.hour }.toMutableList()
            }

        }
    }

    return singleListParsed
}

fun compareTime(
    selectedDate: String,
    initialHour: String,
    finalHour: String,
    compareHour: String
): Boolean {
    val concat = "$selectedDate|$initialHour"
    val concat2 = "$selectedDate|$finalHour"
    val concatCompare = "$selectedDate|$compareHour"
    val date1 = DateTime.parse(concat, DateTimeFormat.forPattern("dd-MM-yyyy|HH:mm"))
    val date2 = DateTime.parse(concat2, DateTimeFormat.forPattern("dd-MM-yyyy|HH:mm"))
    val dateComp = DateTime.parse(concatCompare, DateTimeFormat.forPattern("dd-MM-yyyy|HH:mm"))

    return dateComp.isAfter(date1) && dateComp.isBefore(date2)

}

fun getDatePlusOneDay(): String {

    val current = DateTime.now()
    val plusDay = current.plusDays(1)
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")
    Timber.d("FECHA %s", current.toString(formatter))

    return plusDay.toString(formatter)
}

fun getParsedDate(day: Int, month: Int, year: Int): String {
    var dayTemp = day.toString()
    var monthTemp = month.toString()
    if (dayTemp.length == 1) {
        dayTemp = "0$day"
    }
    if (monthTemp.length == 1) {
        monthTemp = "0$monthTemp"
    }
    return "$dayTemp-$monthTemp-$year"
}

fun getClosestHour(): String {
    val current = DateTime.now()
    val hour = current.hourOfDay().asShortText
    val minutes = current.minuteOfHour().get()
    Timber.d("the closest hour -> %s", current.hourOfDay().asShortText)
    Timber.d("the closest miuntes -> %s", "${current.minuteOfHour().get()}")

    val finalHour: String
    val hourPlusOne = hour.toInt() + 1
    finalHour = if (minutes > 30) {

        "$hourPlusOne:00-$hourPlusOne:30"

    } else {
        "$hour:30-$hourPlusOne:00"

        /* 03:41 proxima hora es 04:00-04:30
        03:10 proxima hora es 03:30-04:00*/
    }
    Timber.d("the final closest hour -> %s", finalHour)
    return finalHour
}

fun calculateRating(rating:MutableMap<String,Int>):Double{
    val ratingAvg= rating["five"]!! *5+rating["four"]!!*4+rating["three"]!!*3+rating["two"]!!*2+rating["one"]!!
    Timber.d("rating sum %s","the rating avg sum is ->${ratingAvg}");

    val ratingCount=rating["five"]!!+rating["four"]!!+rating["three"]!!+rating["two"]!!+rating["one"]!!
    //ratingAvg /= ratingCount
    return ratingAvg/ratingCount.toDouble()
}


