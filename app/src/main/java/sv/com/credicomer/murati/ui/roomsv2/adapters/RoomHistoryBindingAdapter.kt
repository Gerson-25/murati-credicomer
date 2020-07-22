package sv.com.credicomer.murati.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.alert_logout_dialog.view.id_cancel_btn
import kotlinx.android.synthetic.main.alert_logout_dialog.view.id_confirm_btn
import kotlinx.android.synthetic.main.alert_rating_bar_dialog.view.*
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.roomsv2.models.HistoryRoom
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomDetailViewModel

@SuppressLint("InflateParams")
@BindingAdapter(value = ["reservate", "dateRooms", "roomsId", "viewModel", "collection", "subCollection"])
fun TextView.reservate(
    roomDetail: HistoryRoom,
    dateRooms: String,
    roomsId: String,
    viewModel: RoomDetailViewModel,
    collection: String,
    subCollection: String
) {

    setOnClickListener {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.alert_rating_bar_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.create()
        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        mAlertDialog.show()

        val auth = FirebaseAuth.getInstance().currentUser

        val schedul = roomDetail.schedule.toString()

        mDialogView.id_cancel_btn.setOnClickListener {
            mAlertDialog.dismiss()

        }
        roomDetail.roomReservation.ratedUsers?.filter { it.email == auth?.email }
            .apply {
                if (this.isNullOrEmpty()) {

                    mDialogView.ratingBarRoom.setOnRatingBarChangeListener { _, rating, _ ->

                        mDialogView.id_confirm_btn.setOnClickListener {

                            viewModel.updateRating(
                                rating.toInt(),
                                roomsId,
                                schedul,
                                dateRooms,
                                collection,
                                subCollection
                            )
                            mDialogView.ratingBarRoom.isEnabled = false
                            Toast.makeText(context, "Su rating de: $rating ha sido asignado", Toast.LENGTH_SHORT)
                                .show()
                            mAlertDialog.dismiss()
                            findNavController().popBackStack()
                        }
                    }
                } else {
                    mDialogView.ratingBarRoom.setIsIndicator(true)
                    mDialogView.ratingBarRoom.rating = this[0].rating?.toFloat() ?: 0F
                }
            }
    }

}

@BindingAdapter(value = ["ranking"])
fun TextView.ranking(roomDetail: HistoryRoom) {
    text = roomDetail.roomReservation.ratingAvg.toString()
}

@BindingAdapter(value = ["reservateDate"])
fun TextView.reservateDate(dateRooms: String) {
    text = dateRooms
}



