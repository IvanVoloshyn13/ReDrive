package com.example.redrive.presentation.refuelFeature

import androidx.lifecycle.viewModelScope
import com.example.redrive.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


abstract class BaseRefuelViewModel : BaseViewModel(), BaseRefuelViewModelContract {

    protected val mOdometerInput: MutableStateFlow<String> = MutableStateFlow("")
    protected val mFuelVolumeInput: MutableStateFlow<String> = MutableStateFlow("")
    protected val mPricePerUnitInput: MutableStateFlow<String> = MutableStateFlow("")
    protected val mNotesInput: MutableStateFlow<String> = MutableStateFlow("")
    protected val mFullTank: MutableStateFlow<Boolean> = MutableStateFlow(true)
    protected val mMissedPrevious: MutableStateFlow<Boolean> = MutableStateFlow(false)

    protected val mTimeStamp: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    protected val mDateFormatPattern = MutableStateFlow<String>("")

    override val odometerInput: StateFlow<String>
        get() = mOdometerInput.asStateFlow()
    override val fuelVolumeInput: StateFlow<String>
        get() = mFuelVolumeInput.asStateFlow()
    override val pricePerUnitInput: StateFlow<String>
        get() = mPricePerUnitInput.asStateFlow()
    override val notesInput: StateFlow<String>
        get() = mNotesInput.asStateFlow()
    override val fullTank: StateFlow<Boolean>
        get() = mFullTank.asStateFlow()
    override val missedPrevious: StateFlow<Boolean>
        get() = mMissedPrevious.asStateFlow()
    override val date: StateFlow<BaseRefuelViewModelContract.DateUiModel> = combine(
        mTimeStamp, mDateFormatPattern
    ) { timeStamp, pattern ->
        BaseRefuelViewModelContract.DateUiModel(timeStamp, pattern)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BaseRefuelViewModelContract.DateUiModel()
    )

    override val isClearBtnEnabled: StateFlow<Boolean> = combine(
        mOdometerInput, mFuelVolumeInput, mPricePerUnitInput
    ) { odometer, fuelVolume, pricePerUnit ->
        odometer.isNotEmpty() || fuelVolume.isNotEmpty() || pricePerUnit.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    abstract override val isSaveBtnEnabled: StateFlow<Boolean>

    override fun onDateChange(timeStamp: Long) {
        mTimeStamp.onTextChange(timeStamp)
    }

    override fun onOdometerTextChange(odometer: String) {
        mOdometerInput.onTextChange(odometer)
    }

    override fun onFuelVolumeTextChange(fuelVolume: String) {
        mFuelVolumeInput.onTextChange(fuelVolume)
    }

    override fun onPricePerUnitTextChange(pricePerUnit: String) {
        mPricePerUnitInput.onTextChange(pricePerUnit)
    }

    override fun onNotesTextChange(notes: String) {
        mNotesInput.onTextChange(notes)
    }

    override fun onFullTankChange(fullTank: Boolean) {
        mFullTank.value = fullTank
    }

    override fun onMissedPreviousChange(missedPrevious: Boolean) {
        mMissedPrevious.value = missedPrevious
    }

    override fun onBtnClearClick() {
        mOdometerInput.value = ""
        mFuelVolumeInput.value = ""
        mPricePerUnitInput.value = ""
        mNotesInput.value = ""
        mFullTank.value = true
        mMissedPrevious.value = false
        mTimeStamp.value = System.currentTimeMillis()
    }

    abstract override fun onBtnSaveClick()
}