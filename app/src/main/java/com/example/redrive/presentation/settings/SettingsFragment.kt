package com.example.redrive.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.AppSettingsItem
import com.example.domain.model.AvgConsumption
import com.example.domain.model.Capacity
import com.example.domain.model.Currency
import com.example.domain.model.DateFormatPattern
import com.example.domain.model.Distance
import com.example.domain.model.SettingType
import com.example.domain.model.Settings
import com.example.redrive.R
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
                    updateUi(state = it)
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
                            resetStateAction = { viewModel.resetErrorState() }
                        )
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.itemCurrency.itemCurrency.setOnClickListener {
            val currencies = viewModel.getCurrencies()
            val items = currencies.map {
                it.unit
            }.toTypedArray()

            openItemUnitsDialog(
                title = getString(R.string.currency),
                items = items,
                type = SettingType.Currency,
                elements = currencies
            )
        }

        binding.itemCapacity.itemCapacity.setOnClickListener {
            val capacities = viewModel.getCapacities()
            val items = capacities.map {
                it.unit
            }.toTypedArray()
            openItemUnitsDialog(
                title = getString(R.string.capacity),
                items = items,
                type = SettingType.Capacity,
                elements = capacities
            )
        }

        binding.itemDistance.itemDistance.setOnClickListener {
            val distances = viewModel.getDistances()
            val items = distances.map {
                it.unit
            }.toTypedArray()
            openItemUnitsDialog(
                title = getString(R.string.capacity),
                items = items,
                type = SettingType.Distance,
                elements = distances
            )
        }

        binding.itemAvgConsumption.itemAvgConsumption.setOnClickListener {
            val avgConsumptions = viewModel.getAvgConsumptions()
            val items = avgConsumptions.map {
                it.unit
            }.toTypedArray()
            openItemUnitsDialog(
                title = getString(R.string.capacity),
                items = items,
                type = SettingType.AvgConsumption,
                elements = avgConsumptions
            )
        }

        binding.itemDatePattern.itemDatePattern.setOnClickListener {
            val datePatterns = viewModel.getDateFormatPatternsUnits()
            val items = datePatterns.map {
                it.pattern
            }.toTypedArray()
            openItemUnitsDialog(
                title = getString(R.string.capacity),
                items = items,
                type = SettingType.FormatOfDate,
                elements = datePatterns
            )
        }

        binding.btnSave.setOnClickListener {
            viewModel.updateSettings()
        }

    }

    private fun updateUi(state: Settings) {
        binding.itemCurrency.tvAbbreviation.text = state.currencyAbbr
        binding.itemCapacity.tvAbbreviation.text = state.capacityAbbr
        binding.itemDistance.tvAbbreviation.text = state.distanceAbbr
        binding.itemAvgConsumption.tvAbbreviation.text = state.avgConsumptionAbbr
        binding.itemDatePattern.tvAbbreviation.text = state.dateFormatPattern
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
                        viewModel.updateCurrency(elements[which] as Currency)
                    }

                    SettingType.Capacity -> {
                        viewModel.updateCapacity(elements[which] as Capacity)
                    }

                    SettingType.Distance -> {
                        viewModel.updateDistance(elements[which] as Distance)
                    }

                    SettingType.AvgConsumption -> {
                        viewModel.updateAvgConsumption(elements[which] as AvgConsumption)
                    }

                    SettingType.FormatOfDate -> {
                        viewModel.updateDatePattern(elements[which] as DateFormatPattern)
                    }
                }
            }
        builder.create().show()
    }

}