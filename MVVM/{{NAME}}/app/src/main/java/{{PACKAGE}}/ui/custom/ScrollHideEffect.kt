package {{PACKAGE}}.ui.custom

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ScrollHideEffect(
    private val dimmingView: View,
    private val displacement: Float
) : RecyclerView.OnScrollListener() {

    private var scrollY = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        scrollY += dy
        dimmingView.alpha = 1.0f - (scrollY / displacement).coerceAtMost(1.0f)
    }

}