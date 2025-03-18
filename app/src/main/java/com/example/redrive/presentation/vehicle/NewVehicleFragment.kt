package com.example.redrive.presentation.vehicle

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
import com.example.domain.model.VehicleType
import com.example.redrive.R
import com.example.redrive.databinding.FragmentNewVehicleBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewVehicleFragment : Fragment(R.layout.fragment_new_vehicle) {
    private val binding by viewBinding<FragmentNewVehicleBinding>()
    private val viewModel by viewModels<NewVehicleViewModel>()
    private val editable by lazy {
        Editable.Factory.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectStates()
        onOdometerTextChangeListener()
        onVehicleNameTextChangeListener()
        vehicleTypeSwitcher()

        binding.bttSave.setOnClickListener {
            viewModel.saveNewVehicle()
        }

    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {

            launch {
                viewModel.vehicleNameState.collectLatest {
                    binding.etVehicleName.setTextKeepState(editable.newEditable(it))
                }
            }

            launch {
                viewModel.vehicleTypeState.collectLatest {
                    when (it) {
                        VehicleType.Car -> {
                            setVehicleTypeIcon(R.drawable.ic_car)
                        }

                        VehicleType.Bike -> {
                            setVehicleTypeIcon(R.drawable.ic_bike)
                        }

                        VehicleType.Default -> {
                            return@collectLatest
                        }
                    }
                }
            }

            launch {
                viewModel.odometerState.collectLatest {
                    binding.etOdometer.setTextKeepState(
                        editable.newEditable(it)
                    )
                }
            }

            launch {
                viewModel.isBttSaveEnabled.collectLatest {
                    binding.bttSave.isEnabled = it
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        NewVehicleViewModel.NavigationPath.ToVehicles -> {
                            findNavController().popBackStack()
                        }

                        null -> return@collectLatest
                    }
                }
            }
        }

    }

    private fun onVehicleNameTextChangeListener() {
        with(binding) {
            etVehicleName.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus && etVehicleName.text!!.isEmpty()) {
                    vehicleNameContainer.isHelperTextEnabled = true
                    vehicleNameContainer.helperText = getString(R.string.required)
                } else {
                    vehicleNameContainer.isHelperTextEnabled = false
                }
            }
            etVehicleName.doAfterTextChanged {
                viewModel.setVehicleName(it.toString())
            }

        }
    }

    private fun onOdometerTextChangeListener() {
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
                viewModel.setVehicleOdometer(it.toString())
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
    private fun setVehicleTypeIcon(@DrawableRes vehicleType: Int) {
        exampleAnimationForIconReplace(vehicleType)
        binding.ivVehicle.setImageDrawable(requireContext().getDrawable(vehicleType))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun exampleAnimationForIconReplace(@DrawableRes vehicleType: Int) {
        binding.ivCurrentVehicleType.animate()
            .translationX(100f) // Move slightly to the right
            .alpha(0f) // Fade out
            .setDuration(300)
            .withEndAction {
                // Change the drawable after animation ends
                binding.ivCurrentVehicleType.setImageDrawable(
                    requireContext().getDrawable(
                        vehicleType
                    )
                )

                // Reset position for smooth transition
                binding.ivCurrentVehicleType.translationX = -100f
                binding.ivCurrentVehicleType.alpha = 0f

                // Animate the new icon in (slide left + fade in)
                binding.ivCurrentVehicleType.animate()
                    .translationX(0f) // Move to original position
                    .alpha(1f) // Fade in
                    .setDuration(300)
                    .start()
            }
            .start()
    }

}