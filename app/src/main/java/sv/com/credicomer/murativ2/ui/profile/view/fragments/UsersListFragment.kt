package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentUsersListListBinding
import sv.com.credicomer.murativ2.ui.profile.view.adapters.MyItemRecyclerViewAdapter
import sv.com.credicomer.murativ2.ui.profile.view.fragments.GroupListFragment.Companion.ARG_COLUMN_COUNT
import sv.com.credicomer.murativ2.ui.profile.view.fragments.dummy.DummyContent


class UsersListFragment(var listener: OnItemListClickListener) : Fragment() {

    private var columnCount = 1
    private lateinit var binding: FragmentUsersListListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_list_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            binding.list.apply{
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    MyItemRecyclerViewAdapter(
                        DummyContent.ITEMS,
                        listener,
                    34, context)
            }
        }

}