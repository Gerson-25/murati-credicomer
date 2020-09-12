package sv.com.credicomer.murati.ui.roomsv2.viewModels


import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.DialogSpinnersHoursBinding
import sv.com.credicomer.murati.ui.ride.getDateJoda
import sv.com.credicomer.murati.ui.roomsv2.*
import sv.com.credicomer.murati.ui.roomsv2.models.*
import timber.log.Timber


class RoomViewModel : ViewModel() {

    /** these 2 variables are for querying the right path of collections UNICOMER-CREDICOMER*/

    var collectionPath: String? = null

    var subCollectionPath: String? = null

    /** this is an instance to use firebase cloud functions */

    private var functions = FirebaseFunctions.getInstance()

    /** this is an instance to use firebase firestore */
    private var db = FirebaseFirestore.getInstance()


    /** this is the mutable live data to display all the rooms or filtered rooms on the recyclerview */
    private var _rooms = MutableLiveData<MutableList<Room>>()




    /** this are the group of values displayed on the chip, and they are stored for be edited later when
     * the user clicks on the edit chip
     * */
    private var _selectedCapacity = MutableLiveData<String>()

    private var _selectedLocation = MutableLiveData<String>()

    private var _selectedEquipment = MutableLiveData<String>()

    private var _selectedDate = MutableLiveData<String>()


    /** this is an Chip object type for because it is needed to display the range of hours in a string for example
     *      "7:00-:7:30", but also for editing we need to split the hour to get the individual values ["7:00","7:30"]
     *      to put then on the spinner items for being edited
     *
     * */
    private var _selectedHours = MutableLiveData<ChipHour>()




    /** this variables are used when the chips are created for the first time, then if a chip is active the respective
     * button is disabled, waiting for the chip to be closed to be enabled again
     * */
    private var _selectedCapacityChip = MutableLiveData<Boolean>()

    private var _selectedLocationChip = MutableLiveData<Boolean>()

    private var _selectedEquipmentChip = MutableLiveData<Boolean>()

    private var _selectedHoursChip = MutableLiveData<Boolean>()

    private var _selectedDateChip = MutableLiveData<Boolean>()

    private var _roomsRT = MutableLiveData<MutableMap<String, Room>>()
    val roomsRT: LiveData<MutableMap<String, Room>>
        get() = _roomsRT



    /** GETTERS from mutablelivedata to be used on the RoomsHomeFragment*/
    val selectedCapacity: LiveData<String>
        get() = _selectedCapacity

    val selectedLocation: LiveData<String>
        get() = _selectedLocation

    val selectedEquipment: LiveData<String>
        get() = _selectedEquipment

    val selectedHours: LiveData<ChipHour>
        get() = _selectedHours

    val selectedDate: LiveData<String>
        get() = _selectedDate



    val selectedCapacityChip: LiveData<Boolean>
        get() = _selectedCapacityChip

    val selectedLocationChip: LiveData<Boolean>
        get() = _selectedLocationChip

    val selectedEquipmentChip: LiveData<Boolean>
        get() = _selectedEquipmentChip

    val selectedHoursChip: LiveData<Boolean>
        get() = _selectedHoursChip

    val selectedDateChip :LiveData<Boolean>
        get() = _selectedDateChip

    val rooms: LiveData<MutableList<Room>>
        get() = _rooms

    /** these variables are to save and get the multiple checkbox selections of the user on the dialog  */

    private var arrayEquipmentChecked: MutableList<Boolean>

    private var arrayFloorChecked: MutableList<Boolean>

    private var historyList = mutableListOf<RoomQueryHistory>()


    /** these are constant values for the dialogs */
    private var roomEquipment: MutableList<String> =
        mutableListOf("Pantalla", "Televisor", "Telefono", "Proyector")

    private var roomFloorLevels =
        mutableListOf("Nivel 1", "Nivel 2", "Nivel 3", "Nivel 4", "Nivel 5")

    /** this is an important variable, this is the one that stores all the information of the selected chip values
     *  and it changes for every operation of add,delete or modification on the chips
     * */
    private var _roomQueryParams: MutableList<RoomQuery> = mutableListOf()


    /** these are all the rooms on de database*/
    private var initRoomsList: MutableList<Room> = mutableListOf()




    init {


        arrayEquipmentChecked = BooleanArray(roomEquipment.size).toMutableList()

        arrayFloorChecked = BooleanArray(roomFloorLevels.size).toMutableList()




    }

    val roomList = MutableLiveData<MutableList<Room>>()




    /** a function to query ALL rooms on the database */
    fun getAllRooms(collectionPath: String): LiveData<MutableList<Room>> {

        db.collection(collectionPath).get().addOnSuccessListener { snap ->

            val list = snap.toObjects(Room::class.java)
            Timber.d("SNAP %s", "$list")
            _rooms.value = list
            historyList.add(RoomQueryHistory("main", list))
            initRoomsList = list
        }

        return _rooms
    }

    /** a function that displays the dialogs when a category(HOUR,DATE,LOCATION,CAPACITY) button is selected */
    fun showFilterDialog(context: Context, filter: String) {

        lateinit var filterDialog: AlertDialog

        lateinit var arrayOfFilterList: Array<String>
        var item: Int? = null
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)
        var initialHour: String? = null
        var finalHour: String? = null


        when (filter) {


            LOCATION_FILTER -> {
                builder.setTitle("Selecciona nivel")
                arrayOfFilterList = roomFloorLevels.toTypedArray()

                // this is for checking if it was a previous value on the chip
                val previous = if (!_selectedLocation.value.isNullOrEmpty()) {
                    roomFloorLevels.indexOf(_selectedLocation.value.toString())
                } else {
                    -1 //minus one is for the case that none are selected
                }

                builder.setSingleChoiceItems(
                    arrayOfFilterList, previous
                ) { _, selected ->
                    item = selected


                }

            }

            CAPACITY_FILTER -> {
                builder.setTitle("Selecciona la capacidad maxima")

                // this is for checking if it was a previous value on the chip
                val numberRequested = _selectedCapacity.value?.toIntOrNull()
                if (!_selectedCapacity.value.isNullOrEmpty() && numberRequested != null) {

                    input.setText(_selectedCapacity.value)
                }

                // this generates a line bellow the editText capacity, a blue line
                input.background.mutate().colorFilter =
                    BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        context.getColor(R.color.colorPrimary01Blue),
                        BlendModeCompat.SRC_ATOP
                    )

                // this is a validation, that the editText value size can't be longer than 2 numbers
                val filterArray = arrayOfNulls<InputFilter>(1)
                filterArray[0] = InputFilter.LengthFilter(2)
                input.filters = filterArray
                input.gravity = Gravity.CENTER_HORIZONTAL
                input.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_NUMBER

                builder.setView(input)


            }

            EQUIPMENT_FILTER -> {
                builder.setTitle("Selecciona el equipo")
                arrayOfFilterList = roomEquipment.toTypedArray()

                // this is for checking if it was a previous value on the chip
                val previous = if (!_selectedEquipment.value.isNullOrEmpty()) {
                    roomEquipment.indexOf(_selectedEquipment.value.toString())
                } else {
                    -1 //minus one is for the case that none are selected
                }

                builder.setSingleChoiceItems(
                    arrayOfFilterList, previous
                ) { _, selected ->
                    item = selected


                }

            }

            HOUR_FILTER -> {
                builder.setTitle("Selecciona el rango de horas")
                arrayOfFilterList = listScheduleStatic.toTypedArray()


                val binding: DialogSpinnersHoursBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_spinners_hours,
                    null,
                    false
                )

                val spinnerArrayAdapterInitHour: ArrayAdapter<String>?

                if (selectedHours.value != null) {

                    spinnerArrayAdapterInitHour = ArrayAdapter(
                        context, android.R.layout.simple_spinner_item,
                        arrayOfFilterList.filterIndexed { index, _ ->

                            (index != arrayOfFilterList.size - 1)

                        }
                    )


                } else {
                    spinnerArrayAdapterInitHour = ArrayAdapter(
                        context, android.R.layout.simple_spinner_item,
                        arrayOfFilterList.filterIndexed { index, _ ->

                            index != arrayOfFilterList.size - 1

                        }

                    )

                }

                spinnerArrayAdapterInitHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerStartHour.adapter = spinnerArrayAdapterInitHour
                binding.spinnerFinalHour.adapter = spinnerArrayAdapterInitHour


                binding.spinnerStartHour.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        Toast.makeText(context, "nothing", Toast.LENGTH_LONG).show()
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val spinneritem = parent?.getItemAtPosition(position)
                        initialHour = spinneritem.toString()

                        val auxAdapter: ArrayAdapter<String> = ArrayAdapter(
                            context, android.R.layout.simple_spinner_item,
                            arrayOfFilterList.filterIndexed { index, _ ->

                                index > position

                            })




                        auxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerFinalHour.adapter = auxAdapter


                    }


                }


                binding.spinnerFinalHour.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val spinneritem = parent?.getItemAtPosition(position)
                        finalHour = spinneritem.toString()

                    }


                }

                builder.setView(binding.root)


            }


        }



        builder.setPositiveButton("OK") { _, _ ->

            when (filter) {

                LOCATION_FILTER -> {
                    if (item != null) {
                        _selectedLocationChip.postValue(true)

                        _selectedLocation.postValue(roomFloorLevels[item!!])


                        queryParameters(
                            RoomQuery(
                                LOCATION_FILTER,
                                mutableListOf(roomFloorLevels[item!!])
                            )
                        )
                        roomFilters()


                    } else {
                        _selectedLocation.postValue("Ubicacion")
                    }
                }

                CAPACITY_FILTER -> {
                    if (input.text != null && input.text.toString() != "") {
                        _selectedCapacityChip.postValue(true)
                        _selectedCapacity.postValue(input.text.toString())

                        queryParameters(
                            RoomQuery(
                                CAPACITY_FILTER,
                                mutableListOf(input.text.toString())
                            )
                        )
                        roomFilters()

                    } else {
                        _selectedCapacity.postValue("Capacidad")

                    }
                }

                EQUIPMENT_FILTER -> {
                    if (item != null) {
                        _selectedEquipmentChip.postValue(true)

                        _selectedEquipment.postValue(roomEquipment[item!!])



                        queryParameters(
                            RoomQuery(
                                EQUIPMENT_FILTER,
                                mutableListOf(roomEquipment[item!!])
                            )
                        )
                        roomFilters()

                    } else {
                        _selectedEquipment.postValue("Equipo")
                    }
                }


                HOUR_FILTER -> {
                    if (initialHour != null && finalHour != null) {

                        _selectedHoursChip.postValue(true)

                        Timber.d("chip date %s" ,"the value is -> ${_selectedDateChip.value}")

                        _selectedDateChip.postValue(true)


                        _selectedHours.postValue(
                            ChipHour(
                                "$initialHour-$finalHour",
                                initialHour,
                                finalHour
                            )
                        )
                        Timber.d("chip date %s" ,"the value is -> ${_selectedDate.value}")
                        //if (_selectedDate.value==null)
                        _selectedDate.postValue(getDateJoda())

                        queryParameters(
                            RoomQuery(HOUR_FILTER, mutableListOf(initialHour!!, finalHour!!))
                        )
                        pushDetailId( getDateJoda(),HOUR_FILTER)
                        //roomFilters()


                    }


                }



            }

        }
        builder.setNegativeButton("Cancel") { _, _ ->

        }

        filterDialog = builder.create()

        filterDialog.show()
    }

    private fun dateFilter(date:String){

        _selectedDate.postValue(date)
        _selectedDateChip.postValue(true)

        val splitedHours= getClosestHour().split("-")
        _selectedHoursChip.postValue(true)
        _selectedHours.postValue(
            ChipHour(
                getClosestHour(),
                splitedHours[0],
                splitedHours[1]
            )
        )

        queryParameters(
            RoomQuery(DATE_FILTER, mutableListOf(date))
        )

        roomFilters()


    }

    fun resetChip(filter: String) {

        when (filter) {


            LOCATION_FILTER -> {
                arrayFloorChecked = BooleanArray(roomFloorLevels.size).toMutableList()
                _selectedLocation.postValue("Ubicacion")
                _selectedLocationChip.postValue(false)
                _roomQueryParams =
                    _roomQueryParams.filter { it.field != LOCATION_FILTER }.toMutableList()
                roomFilters()

            }

            CAPACITY_FILTER -> {

                _selectedCapacity.postValue("Capacidad")
                _selectedCapacityChip.postValue(false)
                _roomQueryParams =
                    _roomQueryParams.filter { it.field != CAPACITY_FILTER }.toMutableList()
                roomFilters()

            }

            EQUIPMENT_FILTER -> {
                arrayEquipmentChecked = BooleanArray(roomEquipment.size).toMutableList()
                _selectedEquipment.postValue("Equipo")
                _selectedEquipmentChip.postValue(false)
                _roomQueryParams =
                    _roomQueryParams.filter { it.field != EQUIPMENT_FILTER }.toMutableList()
                roomFilters()

            }

            HOUR_FILTER -> {
                _selectedHours.postValue(ChipHour())
                _selectedHoursChip.postValue(false)

                _selectedDate.postValue(null)
                _selectedDateChip.postValue(false)

                _roomQueryParams =
                    _roomQueryParams.filter { it.field != HOUR_FILTER && it.field != DATE_FILTER }
                        .toMutableList()
                roomFilters()



            }

            DATE_FILTER->{

                _selectedHours.postValue(ChipHour())
                _selectedHoursChip.postValue(false)

                _selectedDate.postValue(null)
                _selectedDateChip.postValue(false)

                _roomQueryParams =
                    _roomQueryParams.filter { it.field != HOUR_FILTER && it.field != DATE_FILTER }
                        .toMutableList()
                roomFilters()

            }


        }

    }

    fun resetQueryRoom(){
        _rooms.postValue(initRoomsList)
    }


    private fun queryParameters(queryList: RoomQuery) {

        Timber.d("QUERYV2-LIST %s", "$queryList")
        Timber.d("QUERYV2 %s", "$_roomQueryParams")

        _roomQueryParams.any { it.field == queryList.field }.apply {

            if (this) {
                Timber.d("QUERYV2-PREFILTER %s", "$_roomQueryParams")
                _roomQueryParams =
                    _roomQueryParams.filter { it.field != queryList.field }.toMutableList()
                Timber.d("QUERYV2-FILTERED %s", "$_roomQueryParams")
                _roomQueryParams.add(queryList)
                Timber.d("QUERYV2-POSTFILTERED %s", "$_roomQueryParams")
            } else {

                when (queryList.field) {

                    HOUR_FILTER -> {
                        _roomQueryParams.add(queryList)
                        _roomQueryParams.add(RoomQuery(DATE_FILTER, mutableListOf(getDateJoda())))

                    }
                    DATE_FILTER->{
                        _roomQueryParams.add(queryList)
                        _roomQueryParams.add((RoomQuery(HOUR_FILTER, mutableListOf( getClosestHour()))))

                    }

                    else -> {
                        _roomQueryParams.add(queryList)
                    }
                }


                Timber.d("QUERYV2-ADDEDNOTFILTER %s", "$_roomQueryParams")
            }

        }
    }



    private fun roomFilters(): Task<HashMap<*, *>> {
        // Create the arguments to the callable function.

        val gson = Gson()
        Timber.d("QPARAMS %s", " -> ${gson.toJson(_roomQueryParams)}")
        val data = hashMapOf(
            "queryParams" to gson.toJson(_roomQueryParams),
            "collectionPath" to collectionPath,
            "subCollectionPath" to subCollectionPath
        )



        return functions
            .getHttpsCallable("roomFilters")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as HashMap<*, *>


                val jsonResponse = gson.toJson(result["result"])
                val roomType = object : TypeToken<MutableList<Room>>() {}.type
                val resultRoomList: MutableList<Room> = gson.fromJson(jsonResponse, roomType)

                _rooms.postValue(resultRoomList)
                Timber.d("RESULT-MODEL2 %s", "THE RESULT IS -> $resultRoomList")
                result

            }
            .addOnFailureListener {

                Timber.d("Result %s", it)
            }
    }

    fun pushDetailId(date:String,path: String): Task<HashMap<*, *>> {

        val data = hashMapOf(
            "date" to date,
            "collectionPath" to collectionPath,
            "subCollectionPath" to subCollectionPath
        )

        return functions
            .getHttpsCallable("pushDetailId")
            .call(data)
            .continueWith { task ->

                val result = task.result?.data as HashMap<*, *>

                Timber.d("RESULT-MODEL2 %s", "THE RESULT IS -> $result")
                if (path== DATE_FILTER) {
                    dateFilter(date)
                }else{
                    roomFilters()
                }
                result

            }
            .addOnFailureListener {

                Timber.d("Result %s", it)
            }
    }



    fun queryRooms(path: String, queryList: MutableList<RoomQuery>) {

        val query = db.collection(path)

        var roomValue: String? = null



        queryList.forEach { queryParam ->

            when (queryParam.field) {

                NAME_FILTER -> {
                    roomValue = queryParam.value[0]
                }

            }

        }

        if (roomValue != null) {
            query.whereEqualTo(NAME_FILTER, roomValue).get().addOnSuccessListener {

                val resultQuery = it.toObjects(Room::class.java)

                _rooms.postValue(resultQuery)


            }.addOnFailureListener {

                Timber.d("QUERY-FAIL %s", "THE QUERY FAILED")
            }
        }






    }

}



