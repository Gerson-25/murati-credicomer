package sv.com.credicomer.murativ2.ui.travel.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Record(
    var recordId : String?="",
    var recordName: String?="",
    var recordDate: String?="",
    var recordMount: String?="",
    var recordCategory:String?="",
    var recordPhoto:String?="",
    var recordDescription: String?="",
    var recordDateRegister: String?="",
    var recordUpdateRegister: String?="") : Parcelable{


    constructor( recordName: String?,recordDate: String?,recordMount: String?,recordCategory: String?,recordDescription: String?,recordUpdateRegister: String?) : this(){
        this.recordName=recordName
        this.recordDate=recordDate
        this.recordMount=recordMount
        this.recordCategory=recordCategory
        this.recordDescription=recordDescription
        this.recordUpdateRegister=recordUpdateRegister
    }

    constructor( recordName: String?,recordDate: String?,recordMount: String?,recordCategory: String?,recordPhoto: String?,recordDescription: String?,recordUpdateRegister: String?) : this(){
        this.recordName=recordName
        this.recordDate=recordDate
        this.recordMount=recordMount
        this.recordCategory=recordCategory
        this.recordPhoto =recordPhoto
        this.recordDescription=recordDescription
        this.recordUpdateRegister=recordUpdateRegister
    }
}
/*
orden para las categorias:
food = 0
car = 1
hotel = 2
other = 3
*/