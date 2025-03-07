package com.example.redrive.presentation.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavHost
import androidx.navigation.ui.NavigationUI
import com.example.redrive.R
import com.example.redrive.databinding.FragmentTabsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {
    private val binding by viewBinding<FragmentTabsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = childFragmentManager.findFragmentById(R.id.tabs_fragment_container)
                as NavHost
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
    }
}