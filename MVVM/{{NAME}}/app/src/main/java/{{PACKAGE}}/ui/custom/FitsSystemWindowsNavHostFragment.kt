package {{PACKAGE}}.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.NavHostFragment

class FitsSystemWindowsNavHostFragment : NavHostFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = CoordinatorLayout(inflater.context).also {
        it.id = id
        it.fitsSystemWindows = true
    }

}