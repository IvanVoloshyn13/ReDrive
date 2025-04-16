package com.example.redrive.presentation.unitPreferences

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.model.AppSettingsItem
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.SettingType
import com.example.domain.model.UnitsPreferencesAbbreviation
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentUnitsPreferencesBinding
import com.example.redrive.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UnitsPrefFragment : Fragment(R.layout.fragment_units_preferences) {

    private val viewModel by viewModels<UnitsPrefViewModel>()
    private val binding by viewBinding<FragmentUnitsPreferencesBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectStates()
        setupListeners()
    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.unitPreferences.collectLatest {
                    setUnitsAbbreviation(state = it)
                }
            }

            launch {
                viewModel.arePreferencesTheSame.collectLatest {
                    binding.btnSave.visibility = if (it) View.GONE else View.VISIBLE
                }
            }
            launch {
                viewModel.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(
                            errorMessage = it.second,
                            resetStateAction = { viewModel.onErrorShown() }
                        )
                    }
                }
            }

            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.SettingsDirection.ToProfile -> findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.itemCurrency.itemCurrency.setOnClickListener {
            showCurrencyUnits()
        }

        binding.itemCapacity.itemCapacity.setOnClickListener {
            showCapacityUnits()
        }

        binding.itemDistance.itemDistance.setOnClickListener {
            showDistanceUnits()
        }

        binding.itemAvgConsumption.itemAvgConsumption.setOnClickListener {
            showAvgConsumptionUnits()
        }

        binding.itemDatePattern.itemDatePattern.setOnClickListener {
            showDateFormatPatternsUnits()
        }

        binding.btnSave.setOnClickListener {
            viewModel.onBtnSaveClick()
        }
    }

    private fun setUnitsAbbreviation(state: UnitsPreferencesAbbreviation) {
        binding.itemCurrency.tvAbbreviation.text = state.currency
        binding.itemCapacity.tvAbbreviation.text = state.capacity
        binding.itemDistance.tvAbbreviation.text = state.distance
        binding.itemAvgConsumption.tvAbbreviation.text = state.avgConsumption
        binding.itemDatePattern.tvAbbreviation.text = state.dateFormatPattern
    }

    private fun showCurrencyUnits() {
        val currencies = viewModel.getCurrencies()
        val names = currencies.map {
            it.displayName
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.currency),
            items = names,
            type = SettingType.Currency,
            elements = currencies
        )
    }

    private fun showCapacityUnits() {
        val capacities = viewModel.getCapacities()
        val names = capacities.map {
            it.displayName
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.capacity),
            items = names,
            type = SettingType.Capacity,
            elements = capacities
        )
    }

    private fun showDistanceUnits() {
        val distances = viewModel.getDistances()
        val names = distances.map {
            it.displayName
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.distance),
            items = names,
            type = SettingType.Distance,
            elements = distances
        )
    }

    private fun showAvgConsumptionUnits() {
        val avgConsumptions = viewModel.getAvgConsumptions()
        val abbreviations = avgConsumptions.map {
            it.abbreviation
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.avg_consumption),
            items = abbreviations,
            type = SettingType.AvgConsumption,
            elements = avgConsumptions
        )
    }

    private fun showDateFormatPatternsUnits() {
        val datePatterns = viewModel.getDateFormatPatternsUnits()
        val names = datePatterns.map {
            it.displayName
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.date_format),
            items = names,
            type = SettingType.FormatOfDate,
            elements = datePatterns
        )
    }


    private fun openItemUnitsDialog(
        title: String,
        items: Array<String>,
        elements: List<AppSettingsItem>,
        type: SettingType
    ) {
        val builder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.App_SettingsDialog)
        builder.setTitle(title)
            .setItems(items) { dialog, which ->
                when (type) {
                    SettingType.Currency -> {
                        viewModel.onCurrencyUnitItemClick(elements[which] as Currency)
                    }

                    SettingType.Capacity -> {
                        viewModel.onCapacityUnitItemClick(elements[which] as Capacity)
                    }

                    SettingType.Distance -> {
                        viewModel.onDistanceUnitItemClick(elements[which] as Distance)
                    }

                    SettingType.AvgConsumption -> {
                        viewModel.onAvgConsumptionUnitItemClick(elements[which] as AvgConsumption)
                    }

                    SettingType.FormatOfDate -> {
                        viewModel.onDatePatternItemClick(elements[which] as DateFormatPattern)
                    }
                }
            }
        builder.create().show()
    }

}