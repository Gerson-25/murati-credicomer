package sv.com.credicomer.murativ2.ui.profile.view.fragments

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import sv.com.credicomer.murativ2.R

import sv.com.credicomer.murativ2.ui.profile.view.fragments.dummy.DummyContent.DummyItem

class MyItemRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>(), SelectionDialogFragment.OnItemListClickListener {

    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_users_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    override fun setOnItemListClickListener(navController: NavController) {
        view.setOnClickListener {
            navController.navigate(SelectionDialogFragmentDirections.actionSelectionDialogFragmentToBlankFragment())
        }
    }
}
