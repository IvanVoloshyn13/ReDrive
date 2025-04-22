package com.example.redrive.presentation.redrive

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.Summary
import com.example.domain.model.log.RefuelLog
import com.example.domain.model.log.ValueWithUnit
import com.example.redrive.R
import com.example.redrive.R.layout
import com.example.redrive.core.Router
import com.example.redrive.core.navigate
import com.example.redrive.databinding.FragmentRedriveBinding
import com.example.redrive.presentation.logs.CustomTypefaceSpan
import com.example.redrive.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedriveFragment : Fragment(layout.fragment_redrive) {
    private val viewModel by viewModels<RedriveViewModel>()
    private val binding by viewBinding<FragmentRedriveBinding>()

    private val summaryViewList by lazy {
        arrayOf(
            binding.tvPaymentsSum,
            binding.tvTravelledDistance,
            binding.tvFuelAmountSum
        )
    }

    private val refuelLogViews by lazy {
        arrayOf(
            binding.widgetLastRef.tvAvgConsumption,
            binding.widgetLastRef.tvAvgConsumptionUnit,
            binding.widgetLastRef.tvDistance,
            binding.widgetLastRef.tvPayment,
            binding.widgetLastRef.tvFuelAmount,
            binding.widgetLastRef.tvFuelPricePerUnit,
            binding.widgetLastRef.tvItemRefuelMainInfo

        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setViewListeners()
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
                tvTravelledDistance.text = summary?.travelledDistance?:getString(R.string.place_holder_double)
                tvFuelAmountSum.text = summary?.fuelAmountSum?:getString(R.string.place_holder_double)
                tvPaymentsSum.text = summary?.paymentsSum?:getString(R.string.place_holder_double)
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
                spannableText(it.odometerReading, it.date, requireContext())
            } ?: getString(R.string.place_holder_double)

    }

    private fun spannableText(odometerReading: String, date: String, context: Context): Spannable {
        val message = context.getString(R.string.refuel_message, odometerReading, date)
        val spannable = SpannableString(message)
        val typefaceSemibold = ResourcesCompat.getFont(context, R.font.poppins_semibold)
        val odometerPartStart = message.indexOf(odometerReading)
        val odometerPartEnd = odometerPartStart + odometerReading.length
        spannable.setSpan(
            CustomTypefaceSpan("", typefaceSemibold),
            odometerPartStart,
            odometerPartEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val datePartStart = message.indexOf(date)
        val datePartEnd = datePartStart + date.length
        spannable.setSpan(
            CustomTypefaceSpan("", typefaceSemibold),
            datePartStart,
            datePartEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

}