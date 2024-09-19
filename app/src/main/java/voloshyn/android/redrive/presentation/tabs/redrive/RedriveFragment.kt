package voloshyn.android.redrive.presentation.tabs.redrive

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentRedriveBinding
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.redrive.presentation.tabs.vehicles.VehiclesFragment
import voloshyn.android.redrive.presentation.tabs.vehicles.VehiclesViewModel
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class RedriveFragment : Fragment(R.layout.fragment_redrive) {

    private val binding by viewBinding<FragmentRedriveBinding>()
    private val vehicleViewModel by viewModels<VehiclesViewModel>()
    private val redriveViewModel by viewModels<RedriveViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            VehiclesFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey: String, bundle: Bundle ->
            val vehicle =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(
                        VehiclesFragment.VEHICLE_RESPONSE_KEY,
                        Vehicle::class.java
                    ) as Vehicle
                } else {
                    bundle.getSerializable(
                        VehiclesFragment.VEHICLE_RESPONSE_KEY,
                    ) as Vehicle
                }
            redriveViewModel.onCurrentVehicleChange(vehicle)
        }

        binding.bttAddRefuel.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_newRefuelFragment)
        }

        binding.ivDropDownMenu.setOnClickListener {
            findTopNavController().navigate(R.id.action_tabsFragment_to_vehiclesFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            redriveViewModel.state.collectLatest {
                if (!it.isLoading) {
                    updateUi(it.currentVehicle.name)
                    
                }
            }
        }

    }

    private fun updateUi(vehicleName: String) {
        binding.tvCurrentVehicleName.text =
            vehicleName.ifEmpty {
                binding.bttAddRefuel.isEnabled = false
                getString(R.string.add_new_vehicle)
            }
    }

}