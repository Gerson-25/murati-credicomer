package sv.com.credicomer.murati.dialogs

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.constants.CREDICOMER_DOMAIN
import sv.com.credicomer.murati.constants.UNICOMER_DOMAIN
import sv.com.credicomer.murati.databinding.FragmentLoginDialogBinding


class LoginDialogFragment : DialogFragment() {

    private var listener: OnFragmentInteractionListener? = null
    lateinit var binding: FragmentLoginDialogBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_dialog, container, false)

        binding.buttonResetPassword.setOnClickListener {
            sendResetPassword()

            dialog!!.cancel()
        }
        return binding.root
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

//    override fun onAttach(context: Context) {
//
//        super.onAttach(context)
//        if (context is LoginFragmentListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement LoginFragmentListener")
//        }
//    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun sendResetPassword(){
        var email = binding.etEmailReset.text.toString()

        if(UNICOMER_DOMAIN in email || CREDICOMER_DOMAIN in email){
            auth.sendPasswordResetEmail(email)
            Toast.makeText(this.context, "Correo Enviado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this.context, "Servicio Exclusivo para colaboradores de grupo unicomer o credicomer", Toast.LENGTH_SHORT).show()
        }

    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoginDialogFragment()
    }
}
