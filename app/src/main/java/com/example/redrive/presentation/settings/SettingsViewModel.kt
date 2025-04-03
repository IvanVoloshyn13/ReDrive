package com.example.redrive.presentation.settings

import androidx.lifecycle.viewModelScope
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.Settings
import com.example.domain.useCase.settings.ObserveSettingsUseCase
import com.example.domain.useCase.settings.SettingsFacade
import com.example.domain.useCase.settings.UpdateSettingsUseCase
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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val settingsFacade: SettingsFacade,
    private val appStringResProvider: AppStringResProvider
) : BaseViewModel() {

    private val _settings: MutableStateFlow<Settings> = MutableStateFlow(Settings())
    val settings = _settings.asStateFlow()

    private val originalSettings = MutableStateFlow(Settings())

    val areSettingsTheSame = settings.map { settings ->
        originalSettings.value == settings
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    init {
        viewModelScope.launch {
            observeSettingsUseCase.invoke().collectLatest {
                _settings.value = it
                originalSettings.value = it
            }
        }

    }

    fun getCurrencies(): List<Currency> {
        return settingsFacade.getCurrencyUnits()
    }

    fun getCapacities(): List<Capacity> {
        return settingsFacade.getCapacityUnits()
    }

    fun getAvgConsumptions(): List<AvgConsumption> {
        return settingsFacade.getAvgConsumptionUnits()
    }

    fun getDistances(): List<Distance> {
        return settingsFacade.getDistanceUnits()
    }

    fun getDateFormatPatternsUnits(): List<DateFormatPattern> {
        return settingsFacade.getDateFormatPatterns()
    }

    fun onCurrencyUnitItemClick(currency: Currency) {
        if (currency.unit == settings.value.currencyAbbr) return
        _settings.update {
            it.copy(
                currencyAbbr = currency.abbreviation
            )
        }
    }

    fun onCapacityUnitItemClick(capacity: Capacity) {
        if (capacity.unit == settings.value.capacityAbbr) return
        _settings.update {
            it.copy(
                capacityAbbr = capacity.abbreviation
            )
        }
    }

    fun onDistanceUnitItemClick(distance: Distance) {
        if (distance.unit == settings.value.distanceAbbr) return
        _settings.update {
            it.copy(
                distanceAbbr = distance.abbreviation
            )
        }
    }

    fun onAvgConsumptionUnitItemClick(avgConsumption: AvgConsumption) {
        if (avgConsumption.unit == settings.value.avgConsumptionAbbr) return
        _settings.update {
            it.copy(
                avgConsumptionAbbr = avgConsumption.abbreviation
            )
        }
    }

    fun onDatePatternItemClick(datePattern: DateFormatPattern) {
        if (datePattern.pattern == settings.value.dateFormatPattern) return
        _settings.update {
            it.copy(
                dateFormatPattern = datePattern.pattern
            )
        }
    }

    fun onBtnSaveClick() {
        wrapLocaleDataSourceRequests(
            appStringResProvider = appStringResProvider,
            action = {
                updateSettingsUseCase.invoke(settings = settings.value)
                navigate(Router.SettingsDirection.ToProfile)
            }
        ) { message ->
            emitError(message)
        }
    }

    data object NavigateBack

}