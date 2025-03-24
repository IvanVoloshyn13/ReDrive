package com.example.redrive.presentation.vehicle.vehicles

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Vehicle
import com.example.redrive.R
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
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
        hideSoftInputAndClearViewsFocus(binding.root)
        vehiclesAdapter = VehiclesAdapter(this)
        initVehicleRecyclerView()
        collectState()
        binding.bttAddNewVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_vehiclesFragment_to_newVehicleFragment)
        }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                updateUi(it)
            }
        }
    }

    private fun updateUi(state: VehiclesFragmentState) {
        vehiclesAdapter.submitList(state.vehicles) {
            binding.rvVehicles.scrollToPosition(0)
            if (state.error) {
                showAlertDialog(state.errorMessage, state.vehicles)
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

    private fun showAlertDialog(message: String, vehicles: List<Vehicle>) {
        val dialog = AlertDialog.Builder(requireContext()).setMessage(message)
            .setPositiveButton(
                getString(R.string.delete_anyway),
            ) { _, _ ->
                val currentVehicleId = vehicles.find { it.isCurrentVehicle }?.id
                viewModel.confirmDeleteCurrentVehicle(
                    currentVehicleId ?: throw RuntimeException("TODO")
                )
            }
            .setNegativeButton(getString(R.string.cancel)) { di, _ ->
                di.dismiss()
            }.create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(requireContext(), R.color.md_theme_light_error)
        )
    }

    override fun onVehicleItemClick(vehicle: Vehicle) {
        viewModel.setVehicleAsCurrent(vehicleId = vehicle.id)
    }

    override fun editVehicle(vehicle: Vehicle) {
        val action = VehiclesFragmentDirections.actionVehiclesFragmentToEditVehicleFragment(vehicle)
        findNavController().navigate(action)
    }

    override fun deleteVehicle(vehicleId: Long) {
        viewModel.deleteVehicle(vehicleId)
    }

}