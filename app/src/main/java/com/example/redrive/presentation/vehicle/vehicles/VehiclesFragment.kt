package com.example.redrive.presentation.vehicle.vehicles

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Vehicle
import com.example.redrive.R
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentVehiclesBinding
import com.example.redrive.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
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
                AlertDialog.Builder(requireContext()).setMessage(state.errorMessage)
                    .setPositiveButton(
                        getString(R.string.delete_anyway),
                    ) { _, _ ->
                        Toast.makeText(
                            requireContext(),
                            "Delete",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNeutralButton("Cancel") { _, _ ->
                        Toast.makeText(
                            requireContext(),
                            "Cancel",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.show()

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
        TODO("navigate to editVehicle fragment and send vehicle as args")
    }

    override fun deleteVehicle(vehicleId: Long) {
        viewModel.deleteVehicle(vehicleId)
    }

}