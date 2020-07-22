package sv.com.credicomer.murati.ui.ride

import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.ride.models.Reservation
import sv.com.credicomer.murati.ui.ride.viewModels.ReservationViewModel

@BindingAdapter(value = ["showRound", "viewModel"])
fun TextView.showRound(reservation: Reservation?, viewModel: ReservationViewModel?) {


    reservation?.let {

        if (viewModel?.reservations?.value?.any { rsv ->
                rsv.users.contains(viewModel.user.email)
                    .apply {

                        text = resources.getString(
                            R.string.two_format_string,
                            "Viaje N° ",
                            rsv.round.toString()
                        )
                    }

            }!!

        ) {

            Toast.makeText(context, "the user is inside->", Toast.LENGTH_LONG).show()
        }


    }


}


@BindingAdapter(value = ["capacity", "viewModel"])
fun TextView.showCapacity(reservation: Reservation?, viewModel: ReservationViewModel?) {


    reservation?.let {


        viewModel?.reservations?.value?.any { rsv ->
            rsv.users.contains(viewModel.user.email)
                .apply {
                    if (!this) {
                        text = resources.getString(
                            R.string.two_format_string,
                            "",
                            reservation.pplsize.toString()
                        )

                    }
                }

        }!!

        setOnClickListener {

            Toast.makeText(context, "the ppl is-> ${reservation.pplsize}", Toast.LENGTH_LONG).show()
        }
    }


}












