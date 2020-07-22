package sv.com.credicomer.murati.ui.ride.adapters


import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.ride.models.ReservationFS
import sv.com.credicomer.murati.ui.ride.viewModels.ReservationFSViewModel
import timber.log.Timber


@BindingAdapter(value = ["showOnlyAvailable"])
fun CardView.showOnlyAvailable(reservation: ReservationFS?) {

}

@BindingAdapter(value = ["showLottie"])
fun LottieAnimationView.showLottie(reservation: ReservationFS?){

}


@BindingAdapter(value = ["pushReservationFS", "viewModelFS"])
fun Button.pushReservationFS(reservation: ReservationFS?, viewModel: ReservationFSViewModel?) {

    reservation?.let {


        reservation.users!!.contains(viewModel?.user?.email).apply {

            if (this) {
                text = resources.getString(R.string.dialog_confirmation_negative_btn)
                setBackgroundResource(R.drawable.btn_radius_alert)
                //setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

            } else {
                text = resources.getString(R.string.reservar_btn)
                setBackgroundResource(R.drawable.btn_radius)

                //setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary01Blue))
            }

        }

        setOnClickListener {
            viewModel?.reservations?.value?.filter { it.users!!.contains(viewModel.user.email) && it.schedule_time == reservation.schedule_time }
                .apply {

                    if (this!!.isEmpty()||this.any {rsv-> rsv.schedule_time=="evening" && rsv.id!=reservation.id}&&this.size<2) {

                        viewModel?.confirmReservation(reservation, context)

                    } else {
                        Timber.d("PARAMS %s","${this}")
                        if (this[0].round_status=="finished"){

                            when(this[0].schedule_time){

                                "morning"-> Snackbar.make(rootView,"Tu reservacion de la mañana ha finalizado ", Snackbar.LENGTH_LONG)
                                    .setAction("") {  }.show()
                                "evening"->{
                                    if (this[1].round_status!="finished"){

                                        if ( this[1].id==reservation.id ) {

                                            viewModel?.deleteReservation(reservation, context)
                                        } else {
                                            viewModel?.deleteReservation(reservation, context, this[1])
                                        }

                                    }else {
                                        Snackbar.make(rootView, "Tu reservacion del mediodia ha finalizado ", Snackbar.LENGTH_LONG)
                                            .setAction("") { }.show()
                                    }
                                }
                                "afternoon"->Snackbar.make(rootView, "Tu reservacion de la tarde ha finalizado  ", Snackbar.LENGTH_LONG)
                                    .setAction("") {  }.show()

                            }

                        }
                        else{

                            if ( this.any { it.id==reservation.id }) {

                                viewModel?.deleteReservation(reservation, context)
                            } else {
                                viewModel?.deleteReservation(reservation, context, this[0])
                            }
                        }



                    }

                }

        }


    }
}











