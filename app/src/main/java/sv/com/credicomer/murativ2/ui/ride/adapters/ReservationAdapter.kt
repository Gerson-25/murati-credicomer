
package sv.com.credicomer.murativ2.ui.ride.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.firebase.messaging.FirebaseMessaging
import sv.com.credicomer.murativ2.databinding.ListRowCardRsvBinding
import sv.com.credicomer.murativ2.ui.ride.models.Reservation
import sv.com.credicomer.murativ2.ui.ride.viewModels.ReservationViewModel
import timber.log.Timber


class ReservationAdapter(var viewModel: ReservationViewModel) :ListAdapter<Reservation, ReservationViewHolder>(ReservationDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val  binding = ListRowCardRsvBinding.inflate(layoutInflater, parent, false)
        return ReservationViewHolder(binding)
    }




    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,viewModel)






    }


    class ReservationDiffCallback : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.id === newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }

        private fun unsubscribe(round: String) {

            Timber.d("NOTIFICATION", "unsubscribe to round->$round")
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().unsubscribeFromTopic("round$round")
                .addOnCompleteListener { task ->
                    var msg = "UNSUSCRIPTION SUCCESS"
                    if (!task.isSuccessful) {
                        msg = "UNSUSCRIPTION FAILED"
                    }
                    Timber.d("NOTIFICATION", msg)
                    //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            // [END subscribe_topics]


        }
    }
}



