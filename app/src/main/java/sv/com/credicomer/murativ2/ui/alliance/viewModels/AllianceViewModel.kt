package sv.com.credicomer.murativ2.ui.alliance.viewModels

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import sv.com.credicomer.murativ2.databinding.FragmentAllianceBinding
import sv.com.credicomer.murativ2.ui.alliance.adapters.PromotionsPaginatorAdapter
import sv.com.credicomer.murativ2.ui.alliance.models.*
import timber.log.Timber

class AllianceViewModel : ViewModel() {


    private var firestoredb = FirebaseDatabase.getInstance().getReference("alliance")
    
    private var db=FirebaseFirestore.getInstance()

    private var _categories = MutableLiveData<MutableList<Category>>()

    private var _categoriesFS= MutableLiveData<MutableList<CategoryFS>>()

    private var _establishmentsFS = MutableLiveData<MutableList<EstablishmentFS>>()

    private var _promotionsFS= MutableLiveData<MutableList<PromotionFS>>()

    private var _adapter= MutableLiveData<PromotionsPaginatorAdapter>()

    init {
        val settings= FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings=settings
    }

    val adapter:LiveData<PromotionsPaginatorAdapter>
        get() = _adapter

    val categoriesFS:LiveData<MutableList<CategoryFS>>
        get() = _categoriesFS

    val categories: LiveData<MutableList<Category>>
        get() = _categories

    val establishmenFS:LiveData<MutableList<EstablishmentFS>>
        get()=_establishmentsFS

    val promotionFS:LiveData<MutableList<PromotionFS>>
        get() = _promotionsFS

    var scrollYposition=0

    var parcel:Parcelable?=null

    
    fun getFireStoreCategories(collectionPath: String):LiveData<MutableList<CategoryFS>>{

      //  var allCategory= mutableListOf<CategoryFS>()
        
        db.collection(collectionPath).get()
            .addOnSuccessListener {snap->


                   val obj=snap.toObjects(CategoryFS::class.java)
                _categoriesFS.value=obj


            }
        return _categoriesFS

    }

    fun getFireStoreCategoriesRefresh(collectionPath: String){

        db.collection(collectionPath).get()
            .addOnSuccessListener {snap->

                val obj=snap.toObjects(CategoryFS::class.java)
                _categoriesFS.value=obj



            }

    }

    fun fireStoreAdapter(context: Context,  binding: FragmentAllianceBinding,owner: LifecycleOwner,path:String):LiveData<PromotionsPaginatorAdapter>{

        val promotionsQuery = db.collectionGroup(path)
        val query = promotionsQuery.whereEqualTo("estado", true).orderBy("date", Query.Direction.DESCENDING)

        // Init Paging Configuration
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(30) //page size * 3
            .setPageSize(10)
            .build()

        // Init Adapter Configuration
        val options = FirestorePagingOptions.Builder<PromotionFS>()
            .setLifecycleOwner(owner)
            .setQuery(query, config, PromotionFS::class.java)
            .build()

        _adapter.value=PromotionsPaginatorAdapter(binding,options, context)

        return _adapter
    }

    fun restoreAdapter(context: Context,  binding: FragmentAllianceBinding,owner: LifecycleOwner,path:String):PromotionsPaginatorAdapter{
        val promotionsQuery = db.collectionGroup(path)
        val query = promotionsQuery.whereEqualTo("estado", true).orderBy("date", Query.Direction.DESCENDING)

        // Init Paging Configuration
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(60) //page size * 2
            .setPageSize(30)
            .build()

        // Init Adapter Configuration
        val options = FirestorePagingOptions.Builder<PromotionFS>()
            .setLifecycleOwner(owner)
            .setQuery(query, config, PromotionFS::class.java)
            .build()

        return PromotionsPaginatorAdapter(binding,options, context)

    }



}
