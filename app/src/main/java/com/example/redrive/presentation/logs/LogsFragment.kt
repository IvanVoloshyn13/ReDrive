package com.example.redrive.presentation.logs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentLogsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogsFragment : Fragment(R.layout.fragment_logs) {
    private val binding by viewBinding<FragmentLogsBinding>()
    private val viewModel by viewModels<LogsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigation.collectLatest { direction ->
                when (direction) {
                    Router.LogsDirections.ToRefuel -> {
                        navigate(LogsFragmentDirections.actionLogsFragmentToRefuelFragment())
                    }

                    Router.LogsDirections.ToVehicles -> {}
                    is Router.LogsDirections.ToEditRefuel -> {}
                }
            }
        }


    }

    private fun setupListeners() {
        binding.ivDropDownMenu.setOnClickListener {
            findNavController().navigate(R.id.action_logsFragment_to_vehicle_flow)
        }

        binding.bttAddRefuel.setOnClickListener {
            viewModel.navigate(Router.LogsDirections.ToRefuel)
        }
    }
}