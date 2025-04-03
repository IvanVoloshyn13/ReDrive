package com.example.redrive.presentation.vehicle.editVehicle

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
import androidx.navigation.fragment.navArgs
import com.example.domain.model.VehicleType
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.databinding.FragmentEditVehicleBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditVehicleFragment : Fragment(R.layout.fragment_edit_vehicle) {

    private val args: EditVehicleFragmentArgs by navArgs()

    private val viewModel by viewModels<EditVehicleViewModel>()
    private val binding by viewBinding<FragmentEditVehicleBinding>()
    private val vehicle by lazy {
        args.editedVehicle
    }
    private val editable by lazy {
        Editable.Factory.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftInputAndClearViewsFocus(binding.root)
        viewModel.emitVehicle(vehicle)
        setupVehicleTypeSwitcher()
        setupOnVehicleNameTextChangeListener()
        setupOnOdometerTextChangeListener()

        updateUi()

        binding.btnSave.setOnClickListener {
            viewModel.onSaveBtnClick()
        }

    }

    private fun updateUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.areVehiclesTheSame.collectLatest {
                    binding.btnSave.isEnabled = !it
                }
            }

            launch {
                viewModel.editedVehicle.collectLatest {
                    binding.etVehicleName.setTextKeepState(editable.newEditable(it.name))
                    binding.etOdometer.setTextKeepState(
                        editable.newEditable(it.initialOdometerValue.toString())
                    )
                    when (it.type) {
                        VehicleType.Car -> {
                            binding.vehicleToggle.check(R.id.btt_car)
                            setVehicleTypeIcon(R.drawable.ic_car)
                        }

                        VehicleType.Bike -> {
                            binding.vehicleToggle.check(R.id.btt_bike)
                            setVehicleTypeIcon(R.drawable.ic_bike)
                        }

                        VehicleType.Default -> {
                            binding.vehicleToggle.check(R.id.btt_car)
                            return@collectLatest
                        }
                    }
                }
            }
            launch {
                viewModel.navigation.collectLatest {
                    if (it == Router.EditVehicleDirections.ToVehicles) findNavController().popBackStack()
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
                viewModel.onOdometerTextChange(it.toString())
            }
        }
    }


    private fun setupVehicleTypeSwitcher() {
        binding.vehicleToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btt_car -> {
                        viewModel.onVehicleTypeChange(VehicleType.Car)
                    }

                    R.id.btt_bike -> {
                        viewModel.onVehicleTypeChange(VehicleType.Bike)
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