package com.example.redrive.presentation.refuel

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val DATE_PICKER_TAG = "DATE_PICKER"

@AndroidEntryPoint
class RefuelFragment : Fragment(R.layout.fragment_refuel) {

    private val viewModel by viewModels<RefuelViewModel>()
    private val binding by viewBinding<FragmentRefuelBinding>()
    private val editable by lazy {
        Editable.Factory.getInstance()
    }
    private lateinit var calendar: Calendar
    private var datePicker: MaterialDatePicker<Long>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    @OptIn(FlowPreview::class)
    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.odometerInput.debounce(500).collectLatest {
                    binding.etOdometer.setTextFromState(it)
                }
            }
            launch {
                viewModel.fuelVolumeInput.debounce(500).collectLatest {
                    binding.etFuelVol.setTextFromState(it)
                }
            }
            launch {
                viewModel.pricePerUnitInput.debounce(500).collectLatest {
                    binding.etPricePerUnit.setTextFromState(it)
                }
            }
            launch {
                viewModel.notesInput.debounce(500).collectLatest {
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
        binding.etOdometer.doAfterTextChanged { text ->
            viewModel.onOdometerTextChange(text.toString())
        }
        binding.etFuelVol.doAfterTextChanged { text ->
            viewModel.onFuelVolumeTextChange(text.toString())
        }
        binding.etPricePerUnit.doAfterTextChanged { text ->
            viewModel.onPricePerUnitTextChange(text.toString())
        }
        binding.etNotes.doAfterTextChanged { text ->
            viewModel.onNotesTextChange(text.toString())
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
        datePicker?.show(parentFragmentManager, DATE_PICKER_TAG)
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

}