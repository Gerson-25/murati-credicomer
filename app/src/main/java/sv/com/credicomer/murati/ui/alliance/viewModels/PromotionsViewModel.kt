package sv.com.credicomer.murati.ui.alliance.viewModels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import sv.com.credicomer.murati.databinding.PromotionsFragmentBinding
import sv.com.credicomer.murati.ui.alliance.adapters.PromotionsPaginatorAdapter2
import sv.com.credicomer.murati.ui.alliance.models.PromotionFS

class PromotionsViewModel : ViewModel() {


    private var firestoredb = FirebaseDatabase.getInstance().getReference("alliance")

    private var db= FirebaseFirestore.getInstance()

    private var _promotionsFS = MutableLiveData<PromotionsPaginatorAdapter2>()

    /*private var _promotionsFS= MutableLiveData<MutableList<PromotionFS>>()

    val promotionFS: LiveData<MutableList<PromotionFS>>

        get() = _promotionsFS*/

    init {
        val settings= FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings=settings
    }



    fun getPromotions(context: Context, binding: PromotionsFragmentBinding, owner: LifecycleOwner, idCategory:String, establishment:String,collectionPath:String,subCollectionPath:String):LiveData<PromotionsPaginatorAdapter2>{

       val promotions= db.collection(collectionPath).document(idCategory)
            .collection("establishments").document(establishment).collection(subCollectionPath)
        val query = promotions.whereEqualTo("estado", true).orderBy("date",Query.Direction.DESCENDING)

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


        _promotionsFS.value= PromotionsPaginatorAdapter2(binding,options,context)

        return _promotionsFS

    }






}

