package com.example.redrive.presentation.settings

import android.os.Bundle
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
import com.example.domain.model.Settings
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentSettingsBinding
import com.example.redrive.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel by viewModels<SettingsViewModel>()
    private val binding by viewBinding<FragmentSettingsBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectStates()
        setupListeners()
    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.settings.collectLatest {
                    setUnitsAbbreviation(state = it)
                }
            }

            launch {
                viewModel.areSettingsTheSame.collectLatest {
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

    private fun setUnitsAbbreviation(state: Settings) {
        binding.itemCurrency.tvAbbreviation.text = state.currency
        binding.itemCapacity.tvAbbreviation.text = state.capacity
        binding.itemDistance.tvAbbreviation.text = state.distance
        binding.itemAvgConsumption.tvAbbreviation.text = state.avgConsumption
        binding.itemDatePattern.tvAbbreviation.text = state.dateFormatPattern
    }

    private fun showCurrencyUnits() {
        val currencies = viewModel.getCurrencies()
        val units = currencies.map {
            it.unit
        }.toTypedArray()

        openItemUnitsDialog(
            title = getString(R.string.currency),
            units = units,
            type = SettingType.Currency,
            elements = currencies
        )
    }

    private fun showCapacityUnits() {
        val capacities = viewModel.getCapacities()
        val units = capacities.map {
            it.unit
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.capacity),
            units = units,
            type = SettingType.Capacity,
            elements = capacities
        )
    }

    private fun showDistanceUnits() {
        val distances = viewModel.getDistances()
        val units = distances.map {
            it.unit
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.distance),
            units = units,
            type = SettingType.Distance,
            elements = distances
        )
    }

    private fun showAvgConsumptionUnits() {
        val avgConsumptions = viewModel.getAvgConsumptions()
        val units = avgConsumptions.map {
            it.unit
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.avg_consumption),
            units = units,
            type = SettingType.AvgConsumption,
            elements = avgConsumptions
        )
    }

    private fun showDateFormatPatternsUnits() {
        val datePatterns = viewModel.getDateFormatPatternsUnits()
        val units = datePatterns.map {
            it.unit
        }.toTypedArray()
        openItemUnitsDialog(
            title = getString(R.string.date_format),
            units = units,
            type = SettingType.FormatOfDate,
            elements = datePatterns
        )
    }


    private fun openItemUnitsDialog(
        title: String,
        units: Array<String>,
        elements: List<AppSettingsItem>,
        type: SettingType
    ) {
        val builder: MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext(), R.style.App_SettingsDialog)
        builder.setTitle(title)
            .setItems(units) { dialog, which ->
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