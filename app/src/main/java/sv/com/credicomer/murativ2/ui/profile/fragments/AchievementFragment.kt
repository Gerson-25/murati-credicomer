package sv.com.credicomer.murativ2.ui.profile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAchievementBinding


class AchievementFragment : Fragment() {

    private lateinit var binding: FragmentAchievementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievement, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments?.let { AchievementFragmentArgs.fromBundle(it) }

        binding.achievementText.text = args!!.acknowledge.message
        binding.titleText.text = args.acknowledge.type

    }
}