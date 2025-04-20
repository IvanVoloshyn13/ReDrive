package com.example.redrive.presentation.vehicle.newVehicle

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.redrive.R
import com.example.redrive.presentation.vehicle.BaseVehicleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewVehicleFragment : BaseVehicleFragment<NewVehicleViewModel>(R.layout.fragment_vehicle) {

    private val viewModel by viewModels<NewVehicleViewModel>()
    override val baseVm: NewVehicleViewModel
        get() = viewModel


    override fun observeViewModel() {
        super.observeViewModel()
        viewLifecycleOwner.lifecycleScope.launch {
            baseVm.isSaveBtnEnabled.collectLatest {
                binding.btnSave.isEnabled = it
            }
        }
    }
}