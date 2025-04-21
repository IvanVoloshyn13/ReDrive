package com.example.redrive.presentation.vehicles

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Vehicle
import com.example.redrive.R
import com.example.redrive.core.AppDirection
import com.example.redrive.core.Router
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentVehiclesBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        initVehicleRecyclerView()
        observeViewModel()
        binding.bttAddNewVehicle.setOnClickListener {
            viewModel.navigate(Router.VehiclesDirections.ToNewVehicle)
        }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {

            launch {
                viewModel.state.collectLatest {
                    vehiclesAdapter.submitList(it) {
                        binding.rvVehicles.scrollToPosition(0)
                    }
                }
            }
            launch {
                viewModel.navigation.collectLatest { direction ->
                    navigateWithDirection(direction)
                }
            }

            launch {
                viewModel.error.collectLatest { error ->
                    if (error.first) {
                        showAlertDialog(error.second, viewModel.state.value)
                        viewModel.onErrorShown()
                    }
                }
            }
        }

    }

    private fun initVehicleRecyclerView() {
        vehiclesAdapter = VehiclesAdapter(this)
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
                viewModel.onConfirmDeleteCurrentVehicle(
                    currentVehicleId!! //If there is at least one vehicle it by default will be as current
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
        viewModel.onVehicleItemClick(vehicleId = vehicle.id)
    }

    override fun onEditItemClick(vehicle: Vehicle) {
        viewModel.navigate(Router.VehiclesDirections.ToEditVehicle(vehicle))
    }

    override fun onDeleteItemClick(vehicleId: Long) {
        viewModel.onDeleteBtnClick(vehicleId)
    }

    private fun navigateWithDirection(direction: AppDirection?) {
        when (direction) {
            Router.VehiclesDirections.ToNewVehicle -> {
                navigate(VehiclesFragmentDirections.actionVehiclesFragmentToNewVehicleFragment())
            }

            is Router.VehiclesDirections.ToEditVehicle -> {
                navigate(
                    VehiclesFragmentDirections.actionVehiclesFragmentToEditVehicleFragment(
                        direction.vehicle
                    )
                )
            }

            else -> return
        }
    }

}