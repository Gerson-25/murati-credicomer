package sv.com.credicomer.murativ2.ui.request.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeneralInfoModel(
    var fecha_solicitud:String = "",
    var consolidar_deuda:Int = 0,
    var correo:String = "",
    var codigo:String = "",
    var nombre:String = "",
    var telefono:String = "",
    var municipio:String = "",
    var departamento: String = "",
    var domicilio:String = "",
    var ubicacion:String = "",
    var tipo: String = "",
    var fecha_ingreso:String = "",
    var ingresos_variables:Int = 0,
    var garantia:String = "",
    var telefono_rf_uno: String = "",
    var nombre_rf_uno: String = "",
    var parentesco_rf_uno:String = "",
    var telefono_rf_dos: String = "",
    var nombre_rf_dos: String = "",
    var parentesco_rf_dos:String = "",
    var telefono_rp_uno:String = "",
    var nombre_rp_uno:String = "",
    var telefono_rp_dos:String = "",
    var nombre_rp_dos:String = "",
    var water_receip:String = "",
    var energy_receip:String ="",
    var recommendatio_letter:String ="",
    var cantidad: Int = 0,
    var plazo: String = "",
    var comentarios:String = "",
    var formato_deuda: String = "",
    var couta_mensual: Int = 0,
    var id:String = ""

): Parcelable

        @Parcelize
        data class FamilyReferences(
            val telefono_rf_uno: String = "",
            val nombre_rf_uno: String = "",
            val parentesco_rf_uno:String = ""
        ):Parcelable

@Parcelize
data class PersonalReferences(
    val telefono_rp_uno:String = "",
    val nombre_rp_uno:String = ""
):Parcelable



