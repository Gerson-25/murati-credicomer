package sv.com.credicomer.murati.ui.alliance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import sv.com.credicomer.murati.databinding.ListRowPromotionBinding
import sv.com.credicomer.murati.databinding.PromotionsFragmentBinding
import sv.com.credicomer.murati.ui.alliance.models.PromotionFS
import timber.log.Timber

class PromotionsPaginatorAdapter2(

    val promotionBinding: PromotionsFragmentBinding,
    options: FirestorePagingOptions<PromotionFS>,
    val context: Context
) : FirestorePagingAdapter<PromotionFS, PromotionFSViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionFSViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRowPromotionBinding.inflate(inflater, parent, false)
        return PromotionFSViewHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: PromotionFSViewHolder,
        position: Int,
        promotion: PromotionFS
    ) {

        viewHolder.bind(promotion)
    }

    override fun onError(e: Exception) {
        super.onError(e)
        Timber.e("MainActivity", e.message)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            LoadingState.LOADING_INITIAL -> {
                promotionBinding.swipeRefreshPromotions.isRefreshing = true
            }

            LoadingState.LOADING_MORE -> { // allianceBinding.swipeRefreshLayout.isRefreshing = true

            }
            LoadingState.LOADED -> {
                promotionBinding.swipeRefreshPromotions.isRefreshing = false
            }
            LoadingState.ERROR -> {
                promotionBinding.swipeRefreshPromotions.isRefreshing = false
            }

            LoadingState.FINISHED -> {
                promotionBinding.swipeRefreshPromotions.isRefreshing = false
            }
        }
    }


}