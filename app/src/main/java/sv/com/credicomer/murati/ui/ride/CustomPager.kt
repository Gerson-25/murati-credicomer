package sv.com.credicomer.murati.ui.ride

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.maps.MapView
import androidx.viewpager.widget.ViewPager


class CustomViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v is MapView) {
            true
        } else super.canScroll(v, checkV, dx, x, y)
    }

}