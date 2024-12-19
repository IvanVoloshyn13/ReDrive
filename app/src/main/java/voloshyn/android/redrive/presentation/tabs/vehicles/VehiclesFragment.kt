package voloshyn.android.redrive.presentation.tabs.vehicles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentVehiclesBinding
import voloshyn.android.domain.models.Vehicle
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

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
        binding.bttAddNewVehicle.setOnClickListener {
            findTopNavController().navigate(R.id.action_global_newVehicleFragment)
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                if (!it.isLoading) {
                    vehiclesAdapter.submitList(it.vehicles)
                    if (it.error) {
                        Toast.makeText(
                            requireContext(),
                            getText(it.errorMessage),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        viewModel.resetError()
                    }
                }

            }
        }
    }

    private fun initVehicleRecyclerView() {
        val rv = binding.rvVehicles
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = vehiclesAdapter
    }

    override fun onItemClick(vehicle: Vehicle) {
        viewModel.onIntent(VehicleIntent.OnVehicleChange(vehicle.id))
        findTopNavController().popBackStack()
    }

    override fun editVehicle(vehicle: Vehicle) {
        val action = VehiclesFragmentDirections.actionVehiclesFragmentToFragmentEditVehicle(vehicle)
        findTopNavController().navigate(action)
    }

    override fun deleteVehicle(vehicleId: Long) {
        viewModel.onIntent(VehicleIntent.OnDeleteVehicle(vehicleId = vehicleId))
    }


}