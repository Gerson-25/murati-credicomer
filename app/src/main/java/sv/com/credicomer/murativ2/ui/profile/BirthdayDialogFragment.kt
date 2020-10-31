package sv.com.credicomer.murativ2.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentBirthdayDialogBinding
import sv.com.credicomer.murativ2.databinding.FragmentLoginDialogBinding


class BirthdayDialogFragment : DialogFragment() {

    private var listener: OnFragmentInteractionListener? = null
    lateinit var binding: FragmentBirthdayDialogBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_birthday_dialog, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textviewMessage.text = arguments!!.getString("message")
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BirthdayDialogFragment()
    }
}
