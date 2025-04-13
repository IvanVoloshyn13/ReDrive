package com.example.redrive.presentation.refuel

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Refuel
import com.example.domain.useCase.refuel.SaveRefuelUseCase
import com.example.domain.useCase.settings.GetDateFormatPatternUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import com.example.redrive.core.wrapLocaleDataSourceRequests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEBOUNCE_TIME_MILLIS = 250L

@HiltViewModel
class RefuelViewModel @Inject constructor(
    private val saveRefuelUseCase: SaveRefuelUseCase,
    private val getDateFormatPatternUseCase: GetDateFormatPatternUseCase,
    private val appStringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _odometerInput: MutableStateFlow<String> = MutableStateFlow("")
    val odometerInput = _odometerInput.asStateFlow()
    private val _fuelVolumeInput: MutableStateFlow<String> = MutableStateFlow("")
    val fuelVolumeInput = _fuelVolumeInput.asStateFlow()
    private val _pricePerUnitInput: MutableStateFlow<String> = MutableStateFlow("")
    val pricePerUnitInput = _pricePerUnitInput.asStateFlow()
    private val _notesInput: MutableStateFlow<String> = MutableStateFlow("")
    val notesInput = _notesInput.asStateFlow()
    private val _fullTank: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val fullTank = _fullTank.asStateFlow()
    private val _missedPrevious: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val missedPrevious = _missedPrevious.asStateFlow()

    private val _date: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    private val _dateFormatPattern = MutableStateFlow<String>("")

    init {
        viewModelScope.launch {
            _dateFormatPattern.emit(getDateFormatPatternUseCase())
        }
    }

    val date = combine(
        _date, _dateFormatPattern
    ) { date, pattern ->
        DateUiModel(date, pattern)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DateUiModel()
    )

    val isSaveBtnEnabled = combine(
        _odometerInput, _fuelVolumeInput, _pricePerUnitInput
    ) { odometer, fuelVolume, pricePerUnit ->
        odometer.isNotEmpty() && fuelVolume.isNotEmpty() && pricePerUnit.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isClearBtnEnabled = combine(
        _odometerInput, _fuelVolumeInput, _pricePerUnitInput
    ) { odometer, fuelVolume, pricePerUnit ->
        odometer.isNotEmpty() || fuelVolume.isNotEmpty() || pricePerUnit.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onOdometerTextChange(odometer: String) {
        _odometerInput.onTextChange(odometer)
    }

    fun onFuelVolumeTextChange(fuelVolume: String) {
        _fuelVolumeInput.onTextChange(fuelVolume)
    }

    fun onPricePerUnitTextChange(pricePerUnit: String) {
        _pricePerUnitInput.onTextChange(pricePerUnit)
    }

    fun onDateChange(date: Long) {
        _date.onTextChange(date)
    }

    fun onNotesTextChange(notes: String) {
        _notesInput.onTextChange(notes)
    }

    fun onFullTankChange(fullTank: Boolean) {
        _fullTank.value = fullTank
    }

    fun onMissedPreviousChange(missedPrevious: Boolean) {
        _missedPrevious.value = missedPrevious
    }

    fun onBtnClearClick() {
        _odometerInput.value = ""
        _fuelVolumeInput.value = ""
        _pricePerUnitInput.value = ""
        _notesInput.value = ""
        _fullTank.value = true
        _missedPrevious.value = false
        _date.value = System.currentTimeMillis()
    }

    fun onBtnSaveClick() {
        val refuel = Refuel(
            refuelDate = _date.value,
            odometerValue = _odometerInput.value.toInt(),
            fuelAmount = _fuelVolumeInput.value.toDouble(),
            pricePerUnit = _pricePerUnitInput.value.toDouble(),
            notes = _notesInput.value,
            fullTank = _fullTank.value,
            missedPrevious = _missedPrevious.value
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

    private fun <T> MutableStateFlow<T>.onTextChange(value: T) {
        if (this.value != value)
            viewModelScope.launch {
                this@onTextChange.emit(value)
            }

    }

    data class DateUiModel(
        val date: Long = 0L,
        val pattern: String = ""
    )
}