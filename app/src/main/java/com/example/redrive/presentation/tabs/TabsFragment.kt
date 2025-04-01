package com.example.redrive.presentation.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {
    private val binding by viewBinding<FragmentTabsBinding>()
    private val viewModel: TabsViewModel by viewModels()
    private lateinit var navController: NavController
    private val args: TabsFragmentArgs by navArgs<TabsFragmentArgs>()
    private val topNavGraphsSet = setOf(R.id.profile_graph, R.id.logs_graph, R.id.redrive_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_fragment_container)
                as NavHost
        navController = navHost.navController
        configureAppBars(navController)

        collectState()
        viewModel.onArgs(args.startDestination)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toggleToolbarNavIconVisibility(destination)
        }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.destination.collectLatest {
                setTabsGraphStartDestination(navController = navController, startDestination = it)
            }
        }
    }

    private fun configureAppBars(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(
            navController,
            configuration = appBarConfiguration
        )
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
    }

    private fun toggleToolbarNavIconVisibility(destination: NavDestination) {
        val currentGraph = destination.parent ?: return
        if (topNavGraphsSet.contains(currentGraph.id)) {
            if (currentGraph.startDestinationId == destination.id) {
                binding.toolbar.navigationIcon = null
            } else return
        } else Unit
    }

    private fun setTabsGraphStartDestination(
        navController: NavController,
        startDestination: String
    ) {
        val tabsGraph = navController.navInflater.inflate(R.navigation.tabs_graph)
        tabsGraph.setStartDestination(getStartDestinationId(startDestination))
        navController.graph = tabsGraph
    }

    private fun getStartDestinationId(destination: String): Int {
        return when (destination) {
            Destinations.REDRIVE -> getRedriveGraphId()
            Destinations.PROFILE -> getProfileGraphId()
            else -> throw IllegalArgumentException()
        }
    }

    private fun getProfileGraphId() = R.id.profile_graph
    private fun getRedriveGraphId() = R.id.redrive_graph

    companion object {
        object Destinations {
            const val REDRIVE = "redrive"
            const val PROFILE = "profile"
        }
    }

}