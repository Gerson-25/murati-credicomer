package sv.com.credicomer.murati.ui.share

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.alert_logout_dialog.view.*
import sv.com.credicomer.murati.LoginActivity
import sv.com.credicomer.murati.MainViewModel
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentSettingsBinding
import sv.com.credicomer.murati.ui.alliance.viewModels.CreateCarnetViewModel

class SettingsFragment : Fragment() {

    private lateinit var createCarnetViewModel: CreateCarnetViewModel
    private lateinit var binding: FragmentSettingsBinding
    private var dbAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private lateinit var navController: NavController
    private lateinit var collectionPath: String
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        createCarnetViewModel =
            ViewModelProvider(this).get(CreateCarnetViewModel::class.java)
        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
        collectionPath = mainViewModel.allianceCollectionPath.value.toString()
        navController = findNavController()

        createCarnetViewModel.getCarnet()
        createCarnetViewModel.isCarnet.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it.isNotEmpty()) {
                    binding.carnet = it[0]

                    if (collectionPath.contains("unicomer")) {
                        binding.imageCarnetUnicomer.visibility = View.VISIBLE
                        binding.imageCarnetUnicomer.setOnClickListener {
                            navController.navigate(SettingsFragmentDirections.actionNavSettingsToCreateCarnetFragment(1))
                        }
                    } else {
                        binding.imageCarnetCredicomer.visibility = View.VISIBLE
                        binding.imageCarnetCredicomer.setOnClickListener {
                            navController.navigate(SettingsFragmentDirections.actionNavSettingsToCreateCarnetFragment(1))
                        }
                    }
                } else {
                    binding.updateName.text ="-"
                    binding.updateDep.text ="-"
                    binding.updateCod.text="---"

                    if (collectionPath.contains("unicomer")) {
                        binding.imageCarnetUnicomer.visibility = View.VISIBLE
                        binding.imageCarnetUnicomer.setOnClickListener {
                            Toast.makeText(requireContext(),"No has creado tu carnet aun",Toast.LENGTH_LONG).show()
                        }
                    } else {
                        binding.imageCarnetCredicomer.visibility = View.VISIBLE
                        binding.imageCarnetCredicomer.setOnClickListener {
                            Toast.makeText(requireContext(),"No has creado tu carnet aun",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })

        binding.btnUpdateCarnet.setOnClickListener {
            navController.navigate(SettingsFragmentDirections.actionNavSettingsToCreateCarnetFragment(2))
        }


        binding.btnLogout.setOnClickListener {
            val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_logout_dialog,null)
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mDialogView)

            val mAlertDialog = mBuilder.create()
            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            mAlertDialog.show()

            mDialogView.id_cancel_btn.setOnClickListener{
                mAlertDialog.dismiss()

            }

            mDialogView.id_confirm_btn.setOnClickListener{
                mAlertDialog.dismiss()

                // Manejar el evento en item "Cerrar sesion"

                Toast.makeText(requireContext(), "La sesion ha finalizado", Toast.LENGTH_SHORT).show()
                dbAuth?.signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()

            }
        }

        return binding.root
    }
}