package com.example.redrive.presentation.unitPreferences

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.domain.useCase.settings.ObserveUnitPreferencesUseCase
import com.example.domain.useCase.settings.UnitsPreferencesFacade
import com.example.domain.useCase.settings.UpdateUnitPreferencesUseCase
import com.example.redrive.core.AppStringResProvider
import com.example.redrive.core.BaseViewModel
import com.example.redrive.core.Router
import com.example.redrive.core.wrapLocaleDataSourceRequests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class UnitsPrefViewModel @Inject constructor(
    private val observeUnitPreferencesUseCase: ObserveUnitPreferencesUseCase,
    private val updateUnitPreferencesUseCase: UpdateUnitPreferencesUseCase,
    private val unitsPreferencesFacade: UnitsPreferencesFacade,
    private val appStringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _unitPreferences: MutableStateFlow<UnitsPreferencesAbbreviation> =
        MutableStateFlow(UnitsPreferencesAbbreviation())
    val unitPreferences = _unitPreferences.asStateFlow()

    private val originalUnitPreferences = MutableStateFlow(UnitsPreferencesAbbreviation())

    val arePreferencesTheSame = unitPreferences.map { pref ->
        originalUnitPreferences.value == pref
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    init {
        viewModelScope.launch {
            observeUnitPreferencesUseCase.invoke().collectLatest {
                _unitPreferences.value = it
                originalUnitPreferences.value = it
            }
        }

    }

    fun getCurrencies(): List<Currency> {
        return unitsPreferencesFacade.getCurrencyUnits()
    }

    fun getCapacities(): List<Capacity> {
        return unitsPreferencesFacade.getCapacityUnits()
    }

    fun getAvgConsumptions(): List<AvgConsumption> {
        return unitsPreferencesFacade.getAvgConsumptionUnits()
    }

    fun getDistances(): List<Distance> {
        return unitsPreferencesFacade.getDistanceUnits()
    }

    fun getDateFormatPatternsUnits(): List<DateFormatPattern> {
        return unitsPreferencesFacade.getDateFormatPatterns()
    }

    fun onCurrencyUnitItemClick(currency: Currency) {
        if (currency.displayName == unitPreferences.value.currency) return
        _unitPreferences.update {
            it.copy(
                currency = currency.abbreviation
            )
        }
    }

    fun onCapacityUnitItemClick(capacity: Capacity) {
        if (capacity.displayName == unitPreferences.value.capacity) return
        _unitPreferences.update {
            it.copy(
                capacity = capacity.abbreviation
            )
        }
    }

    fun onDistanceUnitItemClick(distance: Distance) {
        if (distance.displayName == unitPreferences.value.distance) return
        _unitPreferences.update {
            it.copy(
                distance = distance.abbreviation
            )
        }
    }

    fun onAvgConsumptionUnitItemClick(avgConsumption: AvgConsumption) {
        if (avgConsumption.abbreviation == unitPreferences.value.avgConsumption) return
        _unitPreferences.update {
            it.copy(
                avgConsumption = avgConsumption.abbreviation
            )
        }
    }

    fun onDatePatternItemClick(datePattern: DateFormatPattern) {
        if (datePattern.pattern == unitPreferences.value.dateFormatPattern) return
        _unitPreferences.update {
            it.copy(
                dateFormatPattern = datePattern.displayName
            )
        }
    }

    fun onBtnSaveClick() {
        wrapLocaleDataSourceRequests(
            appStringResProvider = appStringResProvider,
            action = {
                updateUnitPreferencesUseCase.invoke(unitPreferences = unitPreferences.value)
                navigate(Router.SettingsDirection.ToProfile)
            }
        ) { message ->
            emitError(message)
        }
    }

}