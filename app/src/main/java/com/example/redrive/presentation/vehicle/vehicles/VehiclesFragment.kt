package com.example.redrive.presentation.vehicle.vehicles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Vehicle
import com.example.redrive.R
import com.example.redrive.databinding.FragmentVehiclesBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VehiclesFragment : Fragment(R.layout.fragment_vehicles),
    VehiclesAdapter.VehicleActionsListener {
    private val binding by viewBinding<FragmentVehiclesBinding>()
    private val viewModel by viewModels<VehiclesViewModel>()
    private lateinit var vehiclesAdapter: VehiclesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vehiclesAdapter = VehiclesAdapter(this)
        initVehicleRecyclerView()
        collectState()
        binding.bttAddNewVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_vehiclesFragment_to_newVehicleFragment)
        }

    }

    private fun collectState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                updateUi(it)
            }
        }
    }

    private fun updateUi(state: VehiclesFragmentState) {
        if (!state.isLoading) {
            vehiclesAdapter.submitList(state.vehicles)
            if (state.error) {
                Toast.makeText(
                    requireContext(),
                   state.errorMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
                viewModel.resetError()
            }
        }
    }

    private fun initVehicleRecyclerView() {
        val rv = binding.rvVehicles
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = vehiclesAdapter
    }

    override fun onVehicleItemClick(vehicle: Vehicle) {
        viewModel.setVehicleAsCurrent(vehicleId = vehicle.id)
    }

    override fun editVehicle(vehicle: Vehicle) {
        TODO()
    }

    override fun deleteVehicle(vehicleId: Long) {
       viewModel.deleteVehicle(vehicleId)
    }

}