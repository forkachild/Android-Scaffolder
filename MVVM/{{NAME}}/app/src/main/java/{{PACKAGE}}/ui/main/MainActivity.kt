package {{PACKAGE}}.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import {{PACKAGE}}.R
import {{PACKAGE}}.base.BaseActivity
import {{PACKAGE}}.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    override fun onCreated(savedInstanceState: Bundle?) {
        super.onCreated(savedInstanceState)

        val navHostFragment = nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.startDestination = R.id.loginFragment
        navHostFragment.navController.graph = graph

        supportFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(navHostFragment)
            .commit()
    }

}
