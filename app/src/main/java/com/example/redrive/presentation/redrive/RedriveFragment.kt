package com.example.redrive.presentation.redrive

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Summary
import com.example.domain.model.log.RefuelLog
import com.example.domain.model.log.ValueWithUnit
import com.example.redrive.R
import com.example.redrive.R.layout
import com.example.redrive.core.logTextFormatter.LogSpannableTextCreator
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentRedriveBinding
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RedriveFragment : Fragment(layout.fragment_redrive) {
    private val viewModel by viewModels<RedriveViewModel>()
    private val binding by viewBinding<FragmentRedriveBinding>()
    @Inject
    lateinit var spannableTextCreator: LogSpannableTextCreator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setViewListeners()
        Log.d("LOG+FR_Redr", spannableTextCreator.toString())
    }

    private fun setViewListeners() {
        binding.bttAddRefuel.setOnClickListener {
            viewModel.onBttAddRefuelClick()
        }
        binding.ivDropDownMenu.setOnClickListener {
            viewModel.onVehiclesDropDownClick()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collectLatest {
                    renderDrivingCostWidget(it?.drivingCost)
                    renderSummaryWidget(it?.summary)
                    renderLastRefuelLogWidget(it?.lastRefuelLog)
                    renderAvgConsumptionWidget(it?.avgConsumption)

                    binding.tvCurrentVehicleName.text =
                        it?.vehicle?.name ?: getString(R.string.app_name)

                }
            }
            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.ReDriveDirection.ToRefuel -> navigate(RedriveFragmentDirections.actionRedriveFragmentToRefuelFragment())
                        Router.ReDriveDirection.ToVehicles -> navigate(RedriveFragmentDirections.actionRedriveFragmentToVehicleFlow())
                    }
                }
            }
        }
    }

    private fun renderSummaryWidget(summary: Summary?) {
        with(binding) {
            tvTravelledDistance.text =
                summary?.travelledDistance ?: getString(R.string.place_holder_double)
            tvFuelAmountSum.text = summary?.fuelAmountSum ?: getString(R.string.place_holder_double)
            tvPaymentsSum.text = summary?.paymentsSum ?: getString(R.string.place_holder_double)
        }
    }

    private fun renderAvgConsumptionWidget(avgCons: ValueWithUnit?) {
        binding.tvAvgConsumptionValue.text =
            avgCons?.value ?: getString(R.string.place_holder_double)

        binding.tvAvgConsumptionUnit.text =
            avgCons?.unit ?: getString(R.string.place_holder_double)
    }

    private fun renderDrivingCostWidget(cost: ValueWithUnit?) {
        binding.tvDrivingCostValue.text =
            cost?.value ?: getString(R.string.place_holder_double)

        binding.tvDrivingCostUnit.text =
            cost?.unit ?: getString(R.string.place_holder_double)
    }

    private fun renderLastRefuelLogWidget(log: RefuelLog?) {
        binding.widgetLastRef.tvAvgConsumption.text =
            log?.avgConsumption?.value ?: getString(R.string.place_holder_double)
        binding.widgetLastRef.tvAvgConsumptionUnit.text =
            log?.avgConsumption?.unit ?: getString(R.string.place_holder_double)
        binding.widgetLastRef.tvDistance.text =
            log?.travelledDistance ?: getString(R.string.place_holder_double)
        binding.widgetLastRef.tvPayment.text =
            log?.payment ?: getString(R.string.place_holder_double)
        binding.widgetLastRef.tvFuelAmount.text =
            log?.fuelAmount ?: getString(R.string.place_holder_double)
        binding.widgetLastRef.tvFuelPricePerUnit.text =
            log?.pricePerUnit ?: getString(R.string.place_holder_double)

        binding.widgetLastRef.tvItemRefuelMainInfo.text =
            log?.let {
               spannableTextCreator.spannableText(it.odometerReading, it.date)
            } ?: getString(R.string.place_holder_double)

    }


}