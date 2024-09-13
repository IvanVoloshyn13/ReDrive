package voloshyn.android.redrive.presentation.tabs.vehicles

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentVehiclesBinding
import voloshyn.android.domain.models.tabs.redrive.Vehicle
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class VehiclesFragment : Fragment(R.layout.fragment_vehicles),
    VehiclesAdapter.OnVehicleItemClickListener {
    private val binding by viewBinding<FragmentVehiclesBinding>()
    private val viewModel by viewModels<VehiclesViewModel>()
    private lateinit var vehiclesAdapter: VehiclesAdapter
    private lateinit var dialogBuilder: AlertDialog.Builder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogBuilder = AlertDialog.Builder(requireContext())
        vehiclesAdapter = VehiclesAdapter(this)
        initVehicleRecyclerView()
        binding.bttAddNewVehicle.setOnClickListener {
            NewVehicleDialogFragment().show(parentFragmentManager, null)
        }
        setFragmentResultListener(NewVehicleDialogFragment.REQUEST_KEY) { requestKey, bundle ->
            val vehicle =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(
                        NewVehicleDialogFragment.KEY_VEHICLE_RESPONSE,
                        Vehicle::class.java
                    ) as Vehicle
                } else {
                    bundle.getSerializable(
                        NewVehicleDialogFragment.KEY_VEHICLE_RESPONSE,
                    ) as Vehicle
                }
            addVehicle(vehicle)
        }



        lifecycleScope.launch {
            viewModel.state.collect {
                if (!it.isLoading) {
                    vehiclesAdapter.submitList(it.vehicles)
                }
            }
        }
    }

    private fun addVehicle(vehicle: Vehicle) {
        viewModel.onIntent(VehicleIntent.AddVehicle(vehicle, null))
    }

    private fun initVehicleRecyclerView() {
        val rv = binding.rvVehicles
        rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv.adapter = vehiclesAdapter
    }

    override fun onClick(vehicle: Vehicle) {

        requireActivity().supportFragmentManager.setFragmentResult(
            REQUEST_KEY,
            bundleOf(VEHICLE_RESPONSE_KEY to vehicle)
        )
        findTopNavController().popBackStack()
    }

    companion object {
        private val TAG = VehiclesFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        const val VEHICLE_RESPONSE_KEY = "VEHICLE"
    }


}