package com.example.redrive.presentation.editRefuel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Refuel
import com.example.domain.useCase.refuel.GetRefuelByIdUseCase
import com.example.domain.useCase.refuel.UpdateRefuelUseCase
import com.example.domain.useCase.settings.GetDateFormatPatternUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import com.example.redrive.core.wrapLocaleDataSourceRequests
import com.example.redrive.presentation.refuel.RefuelViewModel.DateUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRefuelViewModel @Inject constructor(
    private val getRefuelByIdUseCase: GetRefuelByIdUseCase,
    private val getDateFormatPatternUseCase: GetDateFormatPatternUseCase,
    private val appStringResProvider: AppStringResProvider,
    private val updateRefuelUseCase: UpdateRefuelUseCase
) : BaseViewModel() {

    private val oldRefuel: MutableStateFlow<Refuel?> = MutableStateFlow(null)

    private val _timeStamp: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    private val _dateFormatPattern = MutableStateFlow<String>("")

    val date = combine(
        _timeStamp, _dateFormatPattern
    ) { timeStamp, pattern ->
        DateUiModel(timeStamp, pattern)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DateUiModel()
    )

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

    private val _refuelId = MutableStateFlow<Long?>(null)


    private val textInputsFlow: StateFlow<TextInputs> =
        combine(
            _odometerInput, _fuelVolumeInput,
            _pricePerUnitInput, _notesInput
        ) { odo, vol, price, notes ->
            TextInputs(odo, vol, price, notes)
        }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5_000),
                TextInputs()
            )

    private val boolInputsFlow: StateFlow<BoolInputs> =
        combine(_fullTank, _missedPrevious) { full, missed ->
            BoolInputs(full, missed)
        }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5_000),
                BoolInputs()
            )

    private val newRefuel: StateFlow<Refuel> =
        combine(
            oldRefuel.filterNotNull(),
            _timeStamp,
            _dateFormatPattern,
            textInputsFlow,
            boolInputsFlow
        ) { old, ts, pattern, textInputs, bools ->
            old.copy(
                id = old.id,
                refuelTimeStamp = ts,
                odometerValue = textInputs.odometer.toIntOrNull() ?: 0,
                fuelAmount = textInputs.fuelVolume.toDoubleOrNull() ?: 0.0,
                pricePerUnit = textInputs.pricePerUnit.toDoubleOrNull()
                    ?: 0.0,
                notes = textInputs.notes,
                fullTank = bools.fullTank,
                missedPrevious = bools.missedPrevious
            )
        }
            .stateIn(viewModelScope, SharingStarted.Lazily, Refuel())


    val isSaveBtnEnabled = combine(
        oldRefuel, newRefuel
    ) { old, new ->
        old != new && fieldsNotEmpty(new)
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    val isClearBtnEnabled = combine(
        _odometerInput, _fuelVolumeInput, _pricePerUnitInput
    ) { odometer, fuelVolume, pricePerUnit ->
        odometer.isNotEmpty() || fuelVolume.isNotEmpty() || pricePerUnit.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        viewModelScope.launch {
            launch {
                _dateFormatPattern.emit(getDateFormatPatternUseCase())
            }
            launch {
                _refuelId.filterNotNull().collectLatest {
                    initiateState(it)
                }
            }
        }
    }

    private fun fieldsNotEmpty(new: Refuel): Boolean {
        val odometerIsNotEmpty = new.odometerValue != 0 && new.odometerValue.toString().isNotEmpty()
        val fuelAmountIsNotEmpty = new.fuelAmount != 0.0 && new.fuelAmount.toString().isNotEmpty()
        val pricePerUnitIsNotEmpty =
            new.pricePerUnit != 0.0 && new.pricePerUnit.toString().isNotEmpty()
        return odometerIsNotEmpty && fuelAmountIsNotEmpty && pricePerUnitIsNotEmpty
    }

    fun doOnRefuelId(refuelId: Long) {
        viewModelScope.launch {
            _refuelId.emit(refuelId)
        }
    }

    private fun initiateState(refuelId: Long) {
        viewModelScope.launch {
            val refuel = getRefuelByIdUseCase(refuelId)
            setInputsFrom(refuel)
        }
    }

    private fun setInputsFrom(refuel: Refuel) {
        oldRefuel.value = refuel
        _timeStamp.value = refuel.refuelTimeStamp
        _odometerInput.value = refuel.odometerValue.toString()
        _fuelVolumeInput.value = refuel.fuelAmount.toString()
        _pricePerUnitInput.value = refuel.pricePerUnit.toString()
        _notesInput.value = refuel.notes
        _fullTank.value = refuel.fullTank
        _missedPrevious.value = refuel.missedPrevious
    }


    fun onOdometerTextChange(odometer: String) {
        _odometerInput.onTextChange(odometer)

    }

    fun onFuelVolumeTextChange(fuelAmount: String) {
        _fuelVolumeInput.onTextChange(fuelAmount)
    }

    fun onPricePerUnitTextChange(pricePerUnit: String) {
        _pricePerUnitInput.onTextChange(pricePerUnit)
    }

    fun onDateChange(timeStamp: Long) {
        _timeStamp.onTextChange(timeStamp)
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
        _timeStamp.value = System.currentTimeMillis()
    }

    fun onBtnSaveClick() {
        wrapLocaleDataSourceRequests(
            appStringResProvider = appStringResProvider,
            action = {
                updateRefuelUseCase(refuel = newRefuel.value)
                navigate(Router.RefuelDirection.ToLogs)
            },
        ) {
            emitError(it)
        }
    }

    private data class TextInputs(
        val odometer: String = "",
        val fuelVolume: String = "",
        val pricePerUnit: String = "",
        val notes: String = ""
    )

    private data class BoolInputs(
        val fullTank: Boolean = true,
        val missedPrevious: Boolean = false
    )

}