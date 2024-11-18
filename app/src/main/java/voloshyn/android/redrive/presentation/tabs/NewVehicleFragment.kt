package voloshyn.android.redrive.presentation.tabs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentNewVehicleBinding
import voloshyn.android.redrive.utils.viewBinding


class NewVehicleFragment : Fragment(R.layout.fragment_new_vehicle) {
    private val binding by viewBinding<FragmentNewVehicleBinding>()
    private val defaultVehicleType = R.drawable.ic_car

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        vehicleNameInput()
        odometerInput()
        vehicleTypeSwitcher()
    }

    private fun initView() {
        binding.vehicleContainer.isHelperTextEnabled = false
        binding.odometerContainer.isHelperTextEnabled = false
    }

    private fun vehicleNameInput() {
        with(binding) {
            etVehicleName.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus && etVehicleName.text.isNullOrEmpty()) {
                    vehicleContainer.isHelperTextEnabled = true
                    vehicleContainer.helperText = getString(R.string.required)
                } else {
                    vehicleContainer.isHelperTextEnabled = false
                }
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
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun vehicleTypeSwitcher() {
        binding.ivVehicleType.setImageDrawable(requireContext().getDrawable(defaultVehicleType))
        binding.vehicleToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btt_car -> {
                        binding.ivVehicleType.setImageDrawable(
                            requireContext().getDrawable(R.drawable.ic_car)
                        )
                        binding.ivVehicle.setImageDrawable(requireContext().getDrawable(R.drawable.ic_car_black))
                    }

                    R.id.btt_bike -> {
                        binding.ivVehicleType.setImageDrawable(
                            requireContext().getDrawable(R.drawable.ic_bike)
                        )
                        binding.ivVehicle.setImageDrawable(requireContext().getDrawable(R.drawable.ic_bike_black))
                    }
                }
            }
        }

    }


}