package com.example.redrive

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.redrive.presentation.tabs.TabsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var isStartDestination = false
    private var navController: NavController? = null

    @SuppressLint("RestrictedApi")
    private val destinationListener =
        NavController.OnDestinationChangedListener { navController, destination, _ ->
            isStartDestination(destination, navController)
        }


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
        onNavControllerActivated(getMainNavController())
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isStartDestination) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun isStartDestination(
        destination: NavDestination?,
        navController: NavController
    ) {
        val graph = destination?.parent ?: return
        val graphStartDestinationId = graph.startDestinationId
        isStartDestination = graphStartDestinationId == navController.currentDestination?.id
    }

    private fun getMainNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        return navHost.navController
    }


}