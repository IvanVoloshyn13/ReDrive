package com.example.redrive.presentation.refuelFeature.refuel

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Refuel
import com.example.domain.useCase.refuel.SaveRefuelUseCase
import com.example.domain.useCase.settings.GetDateFormatPatternUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.Router
import com.example.redrive.core.wrapLocaleDataSourceRequests
import com.example.redrive.presentation.refuelFeature.BaseRefuelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefuelViewModel @Inject constructor(
    private val saveRefuelUseCase: SaveRefuelUseCase,
    private val getDateFormatPatternUseCase: GetDateFormatPatternUseCase,
    private val appStringResProvider: AppStringResProvider
) : BaseRefuelViewModel() {

    init {
        viewModelScope.launch {
            mDateFormatPattern.emit(getDateFormatPatternUseCase())
        }
    }

    override val isSaveBtnEnabled = combine(
        mOdometerInput, mFuelVolumeInput, mPricePerUnitInput
    ) { odometer, fuelVolume, pricePerUnit ->
        fieldsAreNotEmpty(odometer, fuelVolume, pricePerUnit)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private fun fieldsAreNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.isNotEmpty() }
    }


    override fun onBtnSaveClick() {
        val refuel = Refuel(
            refuelTimeStamp = mTimeStamp.value,
            odometerValue = mOdometerInput.value.toInt(),
            fuelAmount = mFuelVolumeInput.value.toDouble(),
            pricePerUnit = mPricePerUnitInput.value.toDouble(),
            notes = mNotesInput.value,
            fullTank = mFullTank.value,
            missedPrevious = mMissedPrevious.value
        )
        wrapLocaleDataSourceRequests(
            appStringResProvider = appStringResProvider,
            action = {
                saveRefuelUseCase(refuel = refuel)
                navigate(Router.RefuelDirection.ToLogs)
            },
        ) {
            emitError(it)
        }
    }

}