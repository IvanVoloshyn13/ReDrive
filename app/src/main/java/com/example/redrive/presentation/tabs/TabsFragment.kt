package com.example.redrive.presentation.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHost
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.redrive.MainActivity
import com.example.redrive.R
import com.example.redrive.databinding.FragmentTabsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {
    private val binding by viewBinding<FragmentTabsBinding>()
    private val args: TabsFragmentArgs by navArgs<TabsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isUserSignedIn = args.isUserSignedIn

        val navHost = childFragmentManager.findFragmentById(R.id.tabs_fragment_container)
                as NavHost
        val navController = navHost.navController
        val tabsGraph = navController.graph
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