package sv.com.credicomer.murativ2


import android.app.Application
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.databinding.ActivityMainBinding
import sv.com.credicomer.murativ2.ui.alliance.viewModels.AllianceViewModel
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomViewModel
import sv.com.credicomer.murativ2.ui.travel.models.Travel
import timber.log.Timber


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private lateinit var roomViewModel:RoomViewModel
    private lateinit var allianceViewModel:AllianceViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private var shPref : SharedPreferencesContainer? = null

    // Declaring FirebaseAuth components

    private var dbAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var dbFirestore: FirebaseFirestore= FirebaseFirestore.getInstance()
    var dbCollectionReference: CollectionReference? = null


    // Variables para HomeTravelFragment

    var idTravel: String = ""
    var persist: String = ""
    var isActive: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        shPref = SharedPreferencesContainer(applicationContext)

        viewModel.getCredentials()
        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)

        allianceViewModel=ViewModelProvider(this).get(AllianceViewModel::class.java)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_travel, R.id.nav_map, R.id.nav_alliance,
                R.id.nav_settings, R.id.nav_ride, R.id.nav_home,R.id.nav_ridefs, R.id.profileFragment2,
                R.id.nav_rooms2, R.id.slideInfoFragment, R.id.startFragment
            ), binding.drawerLayout
        )
        /*
        Disable main drawable menu options
        binding.navView.menu.removeItem(R.id.nav_rooms2)
         */

        viewModel.roomState.observe(this, Observer {
            if (it){
                binding.navView.menu.removeItem(R.id.nav_rooms2)
            }
        })

        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)
        binding.navView.setNavigationItemSelectedListener(this)

        viewModel.allianceCollectionPath.observe(this, Observer {

        })

        viewModel.allianceSubCollectionPath.observe(this, Observer {


        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.nav_home -> {

                // Obtener currentUser de Firebase
                binding.drawerLayout.closeDrawers()

                //instancia de firebase
                dbCollectionReference = dbFirestore.collection("e-Tracker")
                val splashScreen: View = findViewById(R.id.MainSplash)
                splashScreen.visibility = View.VISIBLE

                // Cargar homeFragment para Inicio de Navegacion en UI
                dbFirestore.collection("e-Tracker") //Genera la busqueda en base al email y al estado del viaje actual

                    .whereEqualTo("emailUser", dbAuth?.currentUser?.email)
                    .whereEqualTo("active", true)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        //Dato curioso, encuentre o no lo que busca firebase igual devuelve una respuesta aunque sea vacia pero siempre es success

                        val travels =querySnapshot.toObjects(Travel::class.java)
                        Timber.d("QSNAP2 %s", "the list is-> $travels")
                        if (travels.isEmpty()) { //cuando no encuentra lo que busca igual devuelve un documento vacio para llenarlo []
                            //por tanto si devuelve vacio cargará homeFragment
                            //CallFragment().addFragment(this.supportFragmentManager, HomeFragment(), true, false, 0)
                            navController.navigate(R.id.nav_home)
                            splashScreen.visibility =
                                View.GONE //la visibilidad del splash depende de cuanto tiempo esta peticion tarde


                        } else {
                            idTravel = travels[0].travelId.toString()
                            persist = travels[0].dateRegister.toString()//envio fecha
                            isActive = travels[0].active

                            viewModel.travelId.value = idTravel
                            viewModel.date.value = persist
                            viewModel.isActive.value = isActive

                            val travel = travels[0]


                            //y si el viaje ya fue registrado cargara homeTravel
                            val bundle = bundleOf("travel" to travel, "id" to idTravel, "date" to persist, "isTravelActive" to isActive)
                            navController.navigate(R.id.nav_home_travel, bundle)

                            splashScreen.visibility = View.GONE
                        }
                    }.addOnFailureListener {


                        //y si el viaje ya fue registrado cargara homeTravel
                        navController.navigate(R.id.nav_home_travel)
                        splashScreen.visibility = View.GONE
                    }
                true

            }

            else -> {
                item.onNavDestinationSelected(navController)
                binding.drawerLayout.closeDrawers()
                true
            }
        }
    }
}
