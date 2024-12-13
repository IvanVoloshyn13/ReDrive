package voloshyn.android.redrive.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.ActivityMainBinding
import voloshyn.android.redrive.presentation.tabs.TabsFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val destinationListener =
        NavController.OnDestinationChangedListener { navController, destination, _ ->

            val previousDestination = navController.previousBackStackEntry?.destination
            supportActionBar?.title = destination.label
            supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination) && previousDestination != null)

            configureToolbarForDestination(destination)

        }

    private val topLevelDestinations = setOf(getTabsDestination())

    private val authNavigationFlow = setOf(getSignInDestination(), getSignUpDestination())

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            viewModel.observeUser().collect()
        }

        onNavControllerActivated(getMainNavController())

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (isStartDestination(navController?.currentDestination)) {
                    finishAffinity()
                } else {
                    navController?.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun configureToolbarForDestination(destination: NavDestination) {
        if (authNavigationFlow.contains(destination.id)) {
            binding.toolbar.setTitleTextColor(Color.WHITE)
            binding.toolbar.setNavigationIconTint(Color.WHITE)
        } else {
            binding.toolbar.setTitleTextColor(Color.BLACK)
            binding.toolbar.setNavigationIconTint(Color.BLACK)
        }
    }


    private fun getMainNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        return navHost.navController
    }

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getSignUpDestination(): Int = R.id.signUpFragment

    private fun getSignInDestination(): Int = R.id.signInFragment


//    fun ifNeeded() {
//        private fun showHomeBttDestination(destination: NavDestination?): Boolean {
//            if (destination == null) return false
//            return homeBttDestinations.contains(destination.id)
//        }
//
//        private val homeBttDestinations =
//            setOf(getVehiclesDestination(), getSignUpDestination(), getNewRefuelDestination())
//    }

    //    private val onBackInvokeListener = (
//            OnBackInvokedCallback {
//                Log.d("MainActivity", "Back invoked")
//                if (isStartDestination(navController?.currentDestination)) {
//                    Log.d("MainActivity", "exit  invoked")
//                    finishAffinity()
//                } else {
//                    Log.d("MainActivity", "Back ")
//                    navController?.popBackStack()
//                }
//            }
//            )

    //        if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
//         onBackInvokedDispatcher.registerOnBackInvokedCallback(
//                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
//                onBackInvokeListener
//            )
//        }


}