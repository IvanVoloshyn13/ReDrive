package com.example.redrive.presentation.refuelFeature

import kotlinx.coroutines.flow.StateFlow

interface BaseRefuelViewModelContract {

    val odometerInput: StateFlow<String>
    val fuelVolumeInput: StateFlow<String>
    val pricePerUnitInput: StateFlow<String>
    val notesInput: StateFlow<String>
    val fullTank: StateFlow<Boolean>
    val missedPrevious: StateFlow<Boolean>
    val date: StateFlow<DateUiModel>
    val isSaveBtnEnabled: StateFlow<Boolean>
    val isClearBtnEnabled: StateFlow<Boolean>

    fun onDateChange(timeStamp: Long)
    fun onOdometerTextChange(odometer: String)
    fun onFuelVolumeTextChange(fuelVolume: String)
    fun onPricePerUnitTextChange(pricePerUnit: String)
    fun onNotesTextChange(notes: String)
    fun onFullTankChange(fullTank: Boolean)
    fun onMissedPreviousChange(missedPrevious: Boolean)

    fun onBtnClearClick()
    fun onBtnSaveClick()

    data class DateUiModel(
        val date: Long = 0L,
        val pattern: String = ""
    )
}