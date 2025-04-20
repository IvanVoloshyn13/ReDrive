package com.example.redrive.presentation.vehicles

import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.vehicle.ConfirmDeleteVehicleUseCase
import com.example.domain.useCase.vehicle.DeleteVehicleUseCase
import com.example.domain.useCase.vehicle.ObserveVehiclesUseCase
import com.example.domain.useCase.vehicle.SetVehicleAsCurrentUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.wrapLocaleDataSourceRequests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    observeVehiclesUseCase: ObserveVehiclesUseCase,
    private val setVehicleAsCurrentUseCase: SetVehicleAsCurrentUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val confirmDeleteVehicleUseCase: ConfirmDeleteVehicleUseCase,
    private val stringResProvider: AppStringResProvider
) : BaseViewModel() {

    val state = observeVehiclesUseCase.invoke().stateIn(
        viewModelScope, SharingStarted.Lazily, ArrayList()
    )


    fun onVehicleItemClick(vehicleId: Long) {
        viewModelScope.launch {
            setVehicleAsCurrentUseCase(vehicleId)
        }
    }

    fun onDeleteBtnClick(vehicleId: Long) {
        wrapLocaleDataSourceRequests(
            appStringResProvider = stringResProvider,
            action = {
                deleteVehicleUseCase.invoke(vehicleId)
            }
        ) { message ->
            emitError(message)
        }
    }

    fun onConfirmDeleteCurrentVehicle(vehicleId: Long) {
        viewModelScope.launch {
            confirmDeleteVehicleUseCase.invoke(vehicleId)
        }
    }

}