package com.example.redrive.presentation.vehicle.newVehicle

import androidx.lifecycle.viewModelScope
import com.example.domain.UserException
import com.example.domain.model.Vehicle
import com.example.domain.model.VehicleType
import com.example.domain.useCase.vehicle.AddNewVehicleUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.RedriveDirection
import com.example.redrive.core.Router
import com.example.redrive.presentation.vehicle.BaseVehicleViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewVehicleViewModel @Inject constructor(
    private val addNewVehicleUseCase: AddNewVehicleUseCase,
    private val appStringResProvider: AppStringResProvider,
) : BaseVehicleViewModel() {

    val isBttSaveEnabled = combine(mVehicleNameInput, mOdometerReadingInput) { name, odometer ->
        Pair(name, odometer)
    }.map {
        it.first.isNotEmpty() && it.second.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)


    override fun doOnBtnSaveClick() {
        val vehicle = Vehicle(
            id = 0,
            name = mVehicleNameInput.value,
            initialOdometerValue = mOdometerReadingInput.value.toInt(),
            type = mVehicleTypeSwitcher.value
        )
        viewModelScope.launch {
            try {
                addNewVehicleUseCase.invoke(vehicle)
                navigate(Router.NewVehicleDirections.ToVehicles)
            } catch (e: UserException.NoUserDetectedException) {
                emitError(appStringResProvider.provideStringRes(e))

            }
        }

    }


}