package sv.com.credicomer.murativ2.ui.profile.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentRecognitionDetailsBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RecognitionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRecognitionDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recognition_details, container, false)
    }


}