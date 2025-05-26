package com.example.redrive.presentation.tabs

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    private lateinit var dialog: AlertDialog

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
            launch {
                viewModel.destination.collectLatest {
                    setTabsGraphStartDestination(
                        navController = navController,
                        startDestination = it
                    )
                }
            }
            launch {
                viewModel.isOnline.collectLatest {
                    if (!it) showOfflineDialog() else {
                        if (::dialog.isInitialized) {
                            dialog.cancel()
                            // todo maybe show some status later
                            Toast.makeText(requireContext(), "You are online", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
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

    private fun showOfflineDialog() {
        dialog = MaterialAlertDialogBuilder(requireContext(), R.style.App_SettingsDialog)
            .setTitle(R.string.offline_title)
            .setMessage(R.string.offline_message)
            .setCancelable(false)
            .setNegativeButton(R.string.settings, null)
            .setPositiveButton(R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnShowListener {
            val btnSettings = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            btnSettings.setOnClickListener {
                startActivity(
                    Intent(
                        Settings.ACTION_WIFI_SETTINGS
                    )
                )
            }
        }
        dialog.show()
    }


    companion object {
        object Destinations {
            const val REDRIVE = "redrive"
            const val PROFILE = "profile"
        }
    }

}