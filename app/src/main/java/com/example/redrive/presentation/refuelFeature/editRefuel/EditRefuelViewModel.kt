package com.example.redrive.presentation.refuelFeature.editRefuel

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Refuel
import com.example.domain.useCase.refuel.GetRefuelByIdUseCase
import com.example.domain.useCase.refuel.UpdateRefuelUseCase
import com.example.domain.useCase.settings.GetDateFormatPatternUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.Router
import com.example.redrive.core.wrapLocaleDataSourceRequests
import com.example.redrive.presentation.refuelFeature.BaseRefuelViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
) : BaseRefuelViewModel() {

    private val _refuelId = MutableStateFlow<Long?>(null)

    private val textInputsFlow: StateFlow<TextInputs> =
        combine(
            mOdometerInput, mFuelVolumeInput,
            mPricePerUnitInput, mNotesInput
        ) { odo, vol, price, notes ->
            TextInputs(odo, vol, price, notes)
        }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5_000),
                TextInputs()
            )

    private val boolInputsFlow: StateFlow<BoolInputs> =
        combine(mFullTank, mMissedPrevious) { full, missed ->
            BoolInputs(full, missed)
        }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5_000),
                BoolInputs()
            )

    private val oldRefuel: MutableStateFlow<Refuel?> = MutableStateFlow(null)
    private val newRefuel: StateFlow<Refuel> =
        combine(
            oldRefuel.filterNotNull(),
            mTimeStamp,
            mDateFormatPattern,
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


    override val isSaveBtnEnabled = combine(
        oldRefuel.filterNotNull(), newRefuel
    ) { old, new ->
        old != new && fieldsNotEmpty(new)
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)


    init {
        viewModelScope.launch {
            launch {
                mDateFormatPattern.emit(getDateFormatPatternUseCase())
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
        mTimeStamp.value = refuel.refuelTimeStamp
        mOdometerInput.value = refuel.odometerValue.toString()
        mFuelVolumeInput.value = refuel.fuelAmount.toString()
        mPricePerUnitInput.value = refuel.pricePerUnit.toString()
        mNotesInput.value = refuel.notes
        mFullTank.value = refuel.fullTank
        mMissedPrevious.value = refuel.missedPrevious
    }


    override fun onBtnSaveClick() {
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