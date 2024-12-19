package voloshyn.android.redrive.presentation.tabs.editVehicle

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentEditVehicleBinding
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.redrive.utils.findTopNavController
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class FragmentEditVehicle : Fragment(R.layout.fragment_edit_vehicle) {

    private val args: FragmentEditVehicleArgs by navArgs()
    private val viewModel by viewModels<EditVehicleViewModel>()
    private val binding by viewBinding<FragmentEditVehicleBinding>()
    private val vehicle by lazy {
        args.vehicle
    }
    private val editable by lazy {
        Editable.Factory.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initState(vehicle)

        binding.vehicleContainer.isHelperTextEnabled = false
        binding.odometerContainer.isHelperTextEnabled = false

        vehicleNameInput(vehicle.name)
        odometerInput()
        vehicleTypeSwitcher()


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {

                binding.bttSave.isEnabled = !it.areVehiclesTheSame
                binding.etOdometer.setTextKeepState(editable.newEditable(it.vehicle.initialOdometerValue.toString()))
                binding.etVehicleName.setTextKeepState(editable.newEditable(it.vehicle.name))
                when (it.vehicle.type) {
                    VehicleType.Car -> {
                        setVehicleTypeIcon(R.drawable.ic_car, R.drawable.ic_car_black)
                    }

                    VehicleType.Bike -> {
                        setVehicleTypeIcon(R.drawable.ic_bike, R.drawable.ic_bike_black)
                    }
                }
            }
        }

        binding.bttSave.setOnClickListener {
            viewModel.updateVehicle()
            findTopNavController().popBackStack()
        }
    }


    private fun vehicleNameInput(name: String) {
        with(binding) {
            etVehicleName.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus && name.isEmpty()) {
                    vehicleContainer.isHelperTextEnabled = true
                    vehicleContainer.helperText = getString(R.string.required)
                } else {
                    vehicleContainer.isHelperTextEnabled = false
                }
            }
            etVehicleName.doAfterTextChanged {
                viewModel.updateVehicleName(it.toString())
            }

        }
    }

    private fun odometerInput() {
        with(binding) {
            etOdometer.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus && etOdometer.text.isNullOrEmpty()) {
                    odometerContainer.isHelperTextEnabled = true
                    odometerContainer.helperText = getString(R.string.required)
                } else {
                    odometerContainer.isHelperTextEnabled = false
                }
            }
            etOdometer.doAfterTextChanged {
                viewModel.updateOdometer(it.toString())
            }
        }
    }

    private fun vehicleTypeSwitcher() {
        binding.vehicleToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btt_car -> {
                        viewModel.updateVehicleType(VehicleType.Car)
                    }

                    R.id.btt_bike -> {
                        viewModel.updateVehicleType(VehicleType.Bike)
                    }
                }
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setVehicleTypeIcon(@DrawableRes vehicleType: Int, @DrawableRes vehicle: Int) {
        binding.ivVehicleType.setImageDrawable(
            requireContext().getDrawable(vehicleType)
        )
        binding.ivVehicle.setImageDrawable(requireContext().getDrawable(vehicle))
    }


}