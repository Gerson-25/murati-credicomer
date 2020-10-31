package sv.com.credicomer.murativ2.ui.alliance.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS
import sv.com.credicomer.murativ2.ui.alliance.models.RatingUsers
import timber.log.Timber

class PromotionsDetailViewModel : ViewModel() {

    var _promotion = MutableLiveData<PromotionFS>()

    private var _ratings = MutableLiveData<MutableList<RatingUsers>>()
    val ratings: LiveData<MutableList<RatingUsers>>
        get() = _ratings
    private var firestoredb = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance().currentUser


    fun updateRating(promotionFS: PromotionFS, rate: Int,collectionPath:String,subCollectionPath:String) {
        val ref = firestoredb.collection(collectionPath).document(promotionFS.category_id.toString())
            .collection("establishments")
            .document(promotionFS.establishment_id.toString()).collection(subCollectionPath)
            .document(promotionFS.promotion_id.toString())

        firestoredb.runTransaction { transaction ->

            val snap = transaction.get(ref).toObject(PromotionFS::class.java)
            val userRate = RatingUsers(auth?.email, rate)
            snap?.rated_users?.add(userRate)

            when (rate) {

                1 -> {
                    snap?.rating!!["one"] = snap.rating!!["one"]!!.plus(1)
                }
                2 -> {
                    snap?.rating!!["two"] = snap.rating!!["two"]!!.plus(1)
                }
                3 -> {
                    snap?.rating!!["three"] = snap.rating!!["three"]!!.plus(1)
                }
                4 -> {
                    snap?.rating!!["four"] = snap.rating!!["four"]!!.plus(1)
                }
                5 -> {
                    snap?.rating!!["five"] = snap.rating!!["five"]!!.plus(1)
                }
            }

            transaction.update(ref, "rated_users", snap?.rated_users)
            transaction.update(ref, "rating", snap?.rating)

            return@runTransaction

        }.addOnSuccessListener {
            Timber.d("FStransact Success", "Transaction success!")

        }
            .addOnFailureListener { e -> Timber.w("FStransact Failure", "Transaction failure.", e) }
    }


}