package com.example.redrive.presentation.vehicle

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.VehicleType
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.addDebouncedListener
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentVehicleBinding
import com.example.redrive.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseVehicleFragment<VM : BaseVehicleViewModel>(@LayoutRes layout: Int) :
    Fragment(layout) {
    protected abstract val baseVm: VM
    protected val binding by viewBinding<FragmentVehicleBinding>()
    private lateinit var fragmentScope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSoftInputAndClearViewsFocus(binding.root)
        fragmentScope = viewLifecycleOwner.lifecycleScope
        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        setOnTextChangeListeners()
        setOnFocusChangeListeners()
        addVehicleTypeSwitcherListener()
        binding.btnSave.setOnClickListener {
            baseVm.doOnBtnSaveClick()
        }
    }

    private fun setOnTextChangeListeners() {
        binding.etVehicleName.addDebouncedListener(fragmentScope) {
            baseVm.onVehicleNameTextChange(it)
        }
        binding.etOdometer.addDebouncedListener(fragmentScope) {
            baseVm.onVehicleOdometerTextChange(it.toString())
        }
    }

    private fun setOnFocusChangeListeners() {
        with(binding) {
            etVehicleName.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus && etVehicleName.text!!.isEmpty()) {
                    vehicleNameContainer.isHelperTextEnabled = true
                    vehicleNameContainer.helperText = getString(R.string.required)
                } else {
                    vehicleNameContainer.isHelperTextEnabled = false
                }
            }

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

    protected open fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                baseVm.vehicleNameInput.collectLatest {
                    binding.etVehicleName.setTextKeepState(it)
                }
            }

            launch {
                baseVm.vehicleTypeInput.collectLatest {
                    when (it) {
                        VehicleType.Car -> {
                            binding.vehicleToggle.check(R.id.btt_car)
                            setVehicleTypeIcon(R.drawable.ic_car)
                        }

                        VehicleType.Bike -> {
                            binding.vehicleToggle.check(R.id.btt_bike)
                            setVehicleTypeIcon(R.drawable.ic_bike)
                        }
                    }
                }
            }

            launch {
                baseVm.odometerReadingInput.collectLatest {
                    binding.etOdometer.setTextKeepState(
                        it
                    )
                }
            }

            launch {
                baseVm.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(it.second) {
                            baseVm.onErrorShown()
                        }
                    }
                }
            }

            launch {
                baseVm.navigation.collectLatest {
                    when (it) {
                        Router.NewVehicleDirections.ToVehicles -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun addVehicleTypeSwitcherListener() {
        binding.vehicleToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btt_car -> {
                        baseVm.onVehicleTypeSwitcherClick(VehicleType.Car)
                    }

                    R.id.btt_bike -> {
                        baseVm.onVehicleTypeSwitcherClick(VehicleType.Bike)
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