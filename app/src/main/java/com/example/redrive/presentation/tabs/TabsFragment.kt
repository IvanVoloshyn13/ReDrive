package com.example.redrive.presentation.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.redrive.R
import com.example.redrive.databinding.FragmentTabsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {
    private val binding by viewBinding<FragmentTabsBinding>()
    private val args: TabsFragmentArgs by navArgs<TabsFragmentArgs>()

    /** Rewrite this with viewModel. Navigation using SharedFlow, args store in ViewModel, because
     * when configuration changes tabs open at startDestination
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = childFragmentManager.findFragmentById(R.id.tabs_fragment_container)
                as NavHost
        val navController = navHost.navController

        initTabsGraphStartDestination(navController, args.isUserSignedIn)
        setupToolbar(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toggleToolbarHomeBttVisibility(destination)
        }

    }

    private fun toggleToolbarHomeBttVisibility(destination: NavDestination?) {
        val currentGraph = destination?.parent ?: return
        val currentGraphStartDestinationId = currentGraph.startDestinationId
        if (currentGraphStartDestinationId != destination.id) {
            binding.tabsToolbar.setNavigationIcon(
                R.drawable.ic_back
            )
        } else binding.tabsToolbar.navigationIcon = null
    }

    private fun setupToolbar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.tabsToolbar.setupWithNavController(
            navController,
            configuration = appBarConfiguration
        )
    }

    private fun initTabsGraphStartDestination(
        navController: NavController,
        isUserSignedIn: Boolean
    ) {
        val tabsGraph = navController.navInflater.inflate(R.navigation.tabs_graph)
        tabsGraph.setStartDestination(getStartDestinationId(isUserSignedIn))
        navController.graph = tabsGraph
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
    }

    private fun getStartDestinationId(isUserSignedIn: Boolean): Int {
        return if (isUserSignedIn) {
            getRedriveGraphId()
        } else getProfileGraphId()
    }

    private fun getProfileGraphId() = R.id.profile_graph
    private fun getRedriveGraphId() = R.id.redrive_graph

}