package com.example.redrive.presentation.editRefuel

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.redrive.R
import com.example.redrive.core.Router
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.setTextFromState
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentRefuelBinding
import com.example.redrive.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val DATE_PICKER_TAG = "DATE_PICKER"

@AndroidEntryPoint
class EditRefuelFragment : Fragment(R.layout.fragment_refuel) {
    private val viewModel by viewModels<EditRefuelViewModel>()
    private val binding by viewBinding<FragmentRefuelBinding>()
    private val editable by lazy {
        Editable.Factory.getInstance()
    }
    private lateinit var calendar: Calendar
    private var datePicker: MaterialDatePicker<Long>? = null

    private val args: EditRefuelFragmentArgs by navArgs()
    private val refuelId by lazy {
        args.refuelId
    }

    private lateinit var scope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.doOnRefuelId(refuelId)
        scope = viewLifecycleOwner.lifecycleScope

        initUi()
        collectStates()
        setupRefuelFieldsListeners()
        setupDatePickerListeners()
        setupBtnListeners()
    }

    private fun initUi() {
        hideSoftInputAndClearViewsFocus(binding.root)
        calendar = Calendar.getInstance(TimeZone.getDefault())
        datePicker =
            parentFragmentManager.findFragmentByTag(DATE_PICKER_TAG) as? MaterialDatePicker<Long>
        if (datePicker != null) addDatePickerListeners()
    }

    private fun setupBtnListeners() {
        binding.bttClear.setOnClickListener {
            viewModel.onBtnClearClick()
        }
        binding.btnSave.setOnClickListener {
            viewModel.onBtnSaveClick()
        }
    }


    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.odometerInput.collectLatest {
                    binding.etOdometer.setTextFromState(it)
                }
            }
            launch {
                viewModel.fuelVolumeInput.collectLatest {
                    binding.etFuelVol.setTextFromState(it)
                }
            }
            launch {
                viewModel.pricePerUnitInput.collectLatest {
                    binding.etPricePerUnit.setTextFromState(it)
                }
            }
            launch {
                viewModel.notesInput.collectLatest {
                    binding.etNotes.setTextFromState(it)
                }
            }
            launch {
                viewModel.fullTank.collectLatest {
                    binding.fullTank.isChecked = it
                }
            }
            launch {
                viewModel.missedPrevious.collectLatest {
                    binding.missedPrevious.isChecked = it
                }
            }
            launch {
                viewModel.isSaveBtnEnabled.collectLatest {
                    binding.btnSave.isEnabled = it
                }
            }
            launch {
                viewModel.isClearBtnEnabled.collectLatest {
                    binding.bttClear.isEnabled = it
                }
            }
            launch {
                viewModel.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(it.second) {
                            viewModel.onErrorShown()
                        }
                    }
                }
            }
            launch {
                viewModel.date.collectLatest {
                    if (it.pattern.isEmpty()) return@collectLatest
                    setDate(it.date, it.pattern)
                }
            }
            launch {
                viewModel.navigation.collectLatest {
                    when (it) {
                        Router.RefuelDirection.ToLogs -> findNavController().popBackStack()
                    }
                }
            }
        }
    }


    private fun setupRefuelFieldsListeners() {
        binding.etOdometer.addDebouncedListener(scope) { text ->
            viewModel.onOdometerTextChange(text)
        }
        binding.etFuelVol.addDebouncedListener(scope) { text ->
            viewModel.onFuelVolumeTextChange(text)
        }
        binding.etPricePerUnit.addDebouncedListener(scope) { text ->
            viewModel.onPricePerUnitTextChange(text)
        }
        binding.etNotes.addDebouncedListener(scope) { text ->
            viewModel.onNotesTextChange(text)
        }

        binding.fullTank.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.onFullTankChange(isChecked)
        }
        binding.missedPrevious.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.onMissedPreviousChange(isChecked)
        }
    }

    private fun setupDatePickerListeners() {
        binding.etDate.setOnKeyListener { _, _, _ ->
            return@setOnKeyListener true
        }
        binding.etDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && datePicker == null) {
                createDatePicker()
            } else return@setOnFocusChangeListener
        }
    }

    private fun addDatePickerListeners() {
        datePicker?.let {
            with(it) {
                addOnCancelListener {
                    dateContainerClearFocus()
                }
                addOnNegativeButtonClickListener {
                    dateContainerClearFocus()
                }
                addOnPositiveButtonClickListener { timeStamp ->
                    dateContainerClearFocus()
                    viewModel.onDateChange(timeStamp)

                }
            }
        }
    }

    private fun createDatePicker() {
        val today = calendar.timeInMillis
        val datePickerConstraint = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setCalendarConstraints(datePickerConstraint)
            .setSelection(today)
            .build()
        datePicker?.show(
            parentFragmentManager,
            DATE_PICKER_TAG
        )
        addDatePickerListeners()
    }

    private fun setDate(timeStamp: Long, pattern: String) {
        val dateFormater = SimpleDateFormat(pattern, Locale.getDefault())
        val date = dateFormater.format(timeStamp)
        binding.etDate.text = editable.newEditable(date)
    }

    private fun dateContainerClearFocus() {
        binding.etDate.clearFocus()
        datePicker = null
    }

    private fun EditText.addDebouncedListener(
        coroutineScope: CoroutineScope,
        delayMillis: Long = 500,
        onTextChanged: (String) -> Unit
    ) {
        var job: Job? = null
        this.doAfterTextChanged { text ->
            job?.cancel()
            job = coroutineScope.launch {
                delay(delayMillis)
                onTextChanged(text.toString())
            }
        }

    }

}