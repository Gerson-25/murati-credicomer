package sv.com.credicomer.murati.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentBirthdayBinding


class BirthdayFragment : Fragment() {

    lateinit var binding: FragmentBirthdayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_birthday, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewBirthdayList.layoutManager = LinearLayoutManager(context)

        binding.recyclerviewBirthdayList.adapter = BirthdayAdapter()
    }

}