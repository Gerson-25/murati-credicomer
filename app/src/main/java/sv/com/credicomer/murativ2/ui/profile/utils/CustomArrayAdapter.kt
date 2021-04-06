package sv.com.credicomer.murativ2.ui.profile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.CustomListItemBinding
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet


class CustomArrayAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val list: List<UserCarnet>,val viewModel: MainViewModel):ArrayAdapter<UserCarnet>(context, layoutResource, list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView  ?: LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false)
        val textOne = view.findViewById<TextView>(R.id.text_view_one)
        textOne.text = getItem(position)!!.name
        val textTwo = view.findViewById<TextView>(R.id.text_view_two)
        textOne.text = getItem(position)!!.name
        textTwo.text = getItem(position)!!.email
        view.setOnClickListener {
            viewModel.changeProfile(getItem(position)!!.email!!)
        }
        return view
    }
    /*lateinit var view: CustomListItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }*/

    @SuppressLint("SetTextI18n")
    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${list[position].name}"
        return view
    }
}

