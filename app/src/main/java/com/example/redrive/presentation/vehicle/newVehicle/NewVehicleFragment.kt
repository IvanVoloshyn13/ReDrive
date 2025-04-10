package com.example.redrive.presentation.vehicle.newVehicle

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.VehicleType
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentNewVehicleBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewVehicleFragment : Fragment(R.layout.fragment_new_vehicle) {
    private val binding by viewBinding<FragmentNewVehicleBinding>()
    private val viewModel by viewModels<NewVehicleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftInputAndClearViewsFocus(binding.root)

        collectStates()
        setupOnOdometerTextChangeListener()
        setupOnVehicleNameTextChangeListener()
        setupVehicleTypeSwitcher()

        binding.btnSave.setOnClickListener {
            viewModel.doOnBtnSaveClick()
        }

    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.vehicleNameState.collectLatest {
                    binding.etVehicleName.setTextKeepState(it)
                }
            }

            launch {
                viewModel.vehicleTypeState.collectLatest {
                    when (it) {
                        VehicleType.Car, VehicleType.Default -> {
                            setVehicleTypeIcon(R.drawable.ic_car)
                        }

                        VehicleType.Bike -> {
                            setVehicleTypeIcon(R.drawable.ic_bike)
                        }
                    }
                }
            }

            launch {
                viewModel.odometerState.collectLatest {
                    binding.etOdometer.setTextKeepState(
                       it
                    )
                }
            }

            launch {
                viewModel.isBttSaveEnabled.collectLatest {
                    binding.btnSave.isEnabled = it
                }
            }

            launch {
                viewModel.errorState.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(it.second) {
                            viewModel.resetErrorState()
                        }
                    }
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.NewVehicleDirections.ToVehicles -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

    }

    private fun setupOnVehicleNameTextChangeListener() {
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
                viewModel.onVehicleNameTextChange(it.toString())
            }

        }
    }

    private fun setupOnOdometerTextChangeListener() {
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
                viewModel.onVehicleOdometerTextChange(it.toString())
            }
        }
    }


    private fun setupVehicleTypeSwitcher() {
        binding.vehicleToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btt_car -> {
                        viewModel.onVehicleTypeSwitcherClick(VehicleType.Car)
                    }

                    R.id.btt_bike -> {
                        viewModel.onVehicleTypeSwitcherClick(VehicleType.Bike)
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
                    AppCompatResources.getDrawable(
                        requireContext(), vehicleType
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