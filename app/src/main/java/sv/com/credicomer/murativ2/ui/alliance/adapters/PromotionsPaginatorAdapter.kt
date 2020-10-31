package sv.com.credicomer.murativ2.ui.alliance.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import sv.com.credicomer.murativ2.databinding.FragmentAllianceBinding
import sv.com.credicomer.murativ2.databinding.ListRowPromotionBinding
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS
import timber.log.Timber

class PromotionsPaginatorAdapter(
    var allianceBinding: FragmentAllianceBinding,
   var options: FirestorePagingOptions<PromotionFS>,
    var context: Context
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
        Timber.e("PAGINATOR", e.message.toString())
    }
    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            LoadingState.LOADING_INITIAL -> {
                allianceBinding.swipeRefreshLayout.isRefreshing = true
                Timber.d("REFRESHER","REFRESHER ADAPTER INIT")
            }

            LoadingState.LOADING_MORE -> {
                 allianceBinding.swipeRefreshLayout.isRefreshing = true
                Timber.d("REFRESHER","REFRESHER ADAPTER LOADING")

            }

            LoadingState.LOADED -> {
                allianceBinding.swipeRefreshLayout.isRefreshing = false
                Timber.d("REFRESHER","REFRESHER ADAPTER LOADED")
            }

            LoadingState.ERROR -> {
               allianceBinding.swipeRefreshLayout.isRefreshing = false
                Timber.d("REFRESHER","REFRESHER ADAPTER ERROR")
            }

            LoadingState.FINISHED -> {
                allianceBinding.swipeRefreshLayout.isRefreshing = false
                Timber.d("REFRESHER","REFRESHER ADAPTER FINISHED ")
            }
        }
    }

}