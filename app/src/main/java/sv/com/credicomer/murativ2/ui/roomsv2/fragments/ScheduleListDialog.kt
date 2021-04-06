package sv.com.credicomer.murativ2.ui.roomsv2.fragments

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.roomsv2.models.ScheduleList
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

const val ARG_ITEM_COUNT = "item_count"
const val ARG_SWITCH = "input switch"
class ScheduleListDialog : BottomSheetDialogFragment() {


    lateinit var viewModel: RoomDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activity.run {
            ViewModelProvider(requireActivity()).get(RoomDetailViewModel::class.java)
        }
        return inflater.inflate(R.layout.item_schedule_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
            LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }*/

        val items = (arguments?.getParcelableArray(ARG_ITEM_COUNT)!! as Array<out ScheduleList>).toMutableList<ScheduleList>()
        val switcher = arguments?.getInt(ARG_SWITCH)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(items, switcher!!)
    }

    private inner class ViewHolder internal constructor(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.layout_schedule_list_dialog,
            parent,
            false
        )
    ) {
        internal val text: TextView = itemView.findViewById(R.id.text)
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: MutableList<ScheduleList>, private val switch: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = mItemCount[position].schedule.split("-")[0]

            if (mItemCount[position].available){
                holder.text.setOnClickListener {
                   if (switch == 1){
                       viewModel.setStartTime(mItemCount[position].schedule)
                   }
                    else{
                       viewModel.setEndTime(mItemCount[position].schedule)
                   }
                    this@ScheduleListDialog.dismiss()
                }
            }
            else{
                holder.text.background = resources.getDrawable(R.drawable.bg_room_items_active)
            }
        }

        override fun getItemCount(): Int {
            return mItemCount.size
        }
    }

    companion object {
        fun newInstance(itemCount: Array<ScheduleList>, switch:Int): ScheduleListDialog =
            ScheduleListDialog().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_ITEM_COUNT, itemCount)
                    putInt(ARG_SWITCH, switch)
                }
            }

    }
}