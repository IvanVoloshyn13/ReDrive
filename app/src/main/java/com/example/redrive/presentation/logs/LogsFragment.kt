package com.example.redrive.presentation.logs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentLogsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogsFragment : Fragment(R.layout.fragment_logs), RefuelLogsAdapter.LogItemClickListener {
    private val binding by viewBinding<FragmentLogsBinding>()
    private val viewModel by viewModels<LogsViewModel>()
    private lateinit var adapter: RefuelLogsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        initLogsAdapter()
        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.navigation.collectLatest { direction ->
                    when (direction) {
                        Router.LogsDirections.ToRefuel -> {
                            navigate(LogsFragmentDirections.actionLogsFragmentToRefuelFragment())
                        }

                        Router.LogsDirections.ToVehicles -> {}
                        is Router.LogsDirections.ToEditRefuel -> {
                            navigate(
                                LogsFragmentDirections.actionLogsFragmentToEditRefuelFragment(
                                    direction.refuelId
                                )
                            )
                        }
                    }
                }
            }

            launch {
                viewModel.state.collectLatest {
                    adapter.submitList(it.logs)
                    binding.tvCurrentVehicleName.text =
                        it.vehicle?.name ?: getString(R.string.app_name)
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

    private fun initLogsAdapter() {
        adapter = RefuelLogsAdapter(this)
        val rv = binding.rvLogs
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    override fun onItemClick(itemId: Long) {
        viewModel.onLogItemClick(itemId)
    }
}