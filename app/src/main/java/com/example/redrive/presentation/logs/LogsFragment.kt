package com.example.redrive.presentation.logs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.log.VehicleWithLogs
import com.example.redrive.R
import com.example.redrive.core.AppDirection
import com.example.redrive.core.logTextFormatter.LogSpannableTextCreator
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentLogsBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LogsFragment : Fragment(R.layout.fragment_logs), RefuelLogsAdapter.LogItemClickListener {
    private val binding by viewBinding<FragmentLogsBinding>()
    private val viewModel by viewModels<LogsViewModel>()
    private lateinit var adapter: RefuelLogsAdapter

    @Inject
    lateinit var spannableTextCreator: LogSpannableTextCreator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLogsRecyclerView()
        setViewsOnClickListeners()
        observeViewModel()
    }

    private fun initLogsRecyclerView() {
        adapter = RefuelLogsAdapter(this, spannableTextCreator)
        val rv = binding.rvLogs
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    private fun setViewsOnClickListeners() {
        binding.ivDropDownMenu.setOnClickListener {
            findNavController().navigate(R.id.action_logsFragment_to_vehicle_flow)
        }

        binding.bttAddRefuel.setOnClickListener {
            viewModel.navigate(Router.LogsDirections.ToRefuel)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.navigation.collectLatest { direction ->
                    if (direction != null) {
                        navigateByDirection(direction)
                    }
                }
            }

            launch {
                viewModel.vehicleWithLogs.collectLatest {
                    renderUi(it)
                }
            }
        }
    }

    private fun renderUi(state: VehicleWithLogs) {
        adapter.submitList(state.logs) {
            binding.rvLogs.scrollToPosition(0)
        }

        binding.tvCurrentVehicleName.text =
            state.vehicle?.name ?: getString(R.string.app_name)
    }

    private fun navigateByDirection(direction: AppDirection) {
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

    override fun onLogItemClick(itemId: Long) {
        viewModel.onLogItemClick(itemId)
    }

}