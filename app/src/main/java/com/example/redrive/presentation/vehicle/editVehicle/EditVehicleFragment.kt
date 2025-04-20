package com.example.redrive.presentation.vehicle.editVehicle

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.presentation.vehicle.BaseVehicleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditVehicleFragment :
    BaseVehicleFragment<EditVehicleViewModel>(R.layout.fragment_vehicle) {

    private val args: EditVehicleFragmentArgs by navArgs()
    private val vehicle by lazy {
        args.editedVehicle
    }

    private val viewModel by viewModels<EditVehicleViewModel>()
    override val baseVm: EditVehicleViewModel
        get() = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.emitVehicle(vehicle)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observeViewModel() {
        super.observeViewModel()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                baseVm.isSaveBtnEnabled.collectLatest {
                    binding.btnSave.isEnabled = it
                }
            }

            launch {
                baseVm.navigation.collectLatest {
                    when (it) {
                        Router.EditVehicleDirections.ToVehicles -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }

        }
    }

}