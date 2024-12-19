package voloshyn.android.redrive.presentation.newVehicle

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentNewVehicleBinding
import voloshyn.android.domain.models.VehicleType
import voloshyn.android.redrive.utils.viewBinding

@AndroidEntryPoint
class NewVehicleFragment : Fragment(R.layout.fragment_new_vehicle) {
    private val binding by viewBinding<FragmentNewVehicleBinding>()
    private val viewModel by viewModels<NewVehicleViewModel>()
    private val editable by lazy {
        Editable.Factory.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bttSave.setOnClickListener {
            viewModel.saveNewVehicle()
            if (findNavController().currentBackStackEntry?.destination == null) {
                findNavController().navigate(R.id.action_newVehicleFragment_to_tabsFragment)
            } else {
                findNavController().popBackStack()
            }
        }

        initView()
        odometerInput()
        vehicleTypeSwitcher()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                bttSaveState(it.vehicleName, it.odometer)
                vehicleNameInput(it.vehicleName)

                binding.etVehicleName.setTextKeepState(editable.newEditable(it.vehicleName))
                binding.etOdometer.setTextKeepState(
                    editable.newEditable(
                        it.odometer
                    )
                )

                when (it.vehicleType) {
                    VehicleType.Car -> {
                        setVehicleTypeIcon(R.drawable.ic_car, R.drawable.ic_car_black)
                    }

                    VehicleType.Bike -> {
                        setVehicleTypeIcon(R.drawable.ic_bike, R.drawable.ic_bike_black)
                    }

                }
            }
        }

    }

    private fun initView() {
        binding.vehicleContainer.isHelperTextEnabled = false
        binding.odometerContainer.isHelperTextEnabled = false
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
                        viewModel.switchVehicleType(VehicleType.Car)
                    }

                    R.id.btt_bike -> {
                        viewModel.switchVehicleType(VehicleType.Bike)
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

    private fun bttSaveState(name: String, odometer: String) {
        binding.bttSave.isEnabled = !(name.isEmpty() || odometer.isEmpty())
    }


}