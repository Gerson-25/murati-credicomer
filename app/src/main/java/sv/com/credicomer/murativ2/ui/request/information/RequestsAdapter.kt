package sv.com.credicomer.murativ2.ui.request.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_request.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStartBinding
import sv.com.credicomer.murativ2.databinding.ItemRequestBinding
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfoModel
import java.util.zip.Inflater
import kotlin.coroutines.coroutineContext

class RequestsAdapter(val requestList: MutableList<GeneralInfoModel>):RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>(){

    lateinit var binding: ItemRequestBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
       binding = DataBindingUtil.inflate(inflater, R.layout.item_request, parent, false)
        return RequestsViewHolder(binding.root)
    }

    override fun getItemCount() = requestList.size

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        holder.itemView.request_item_container.setOnClickListener {
            Navigation.findNavController(it).navigate(StartFragmentDirections.actionStartFragmentToStepTwoFragment(requestList[position].id))
        }
        holder.itemView.amount.text = "Solicitud por: $"+requestList[position].cantidad.toString()
        holder.itemView.date.text = requestList[position].fecha_solicitud
    }

    inner class RequestsViewHolder(item:View): RecyclerView.ViewHolder(item){

    }

}
