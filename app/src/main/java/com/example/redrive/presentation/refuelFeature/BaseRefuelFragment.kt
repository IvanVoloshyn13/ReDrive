package com.example.redrive.presentation.refuelFeature

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.redrive.core.Router
import com.example.redrive.core.hideSoftInputAndClearViewsFocus
import com.example.redrive.core.setTextFromState
import com.example.redrive.core.showErrorAndResetState
import com.example.redrive.databinding.FragmentRefuelBinding
import com.example.redrive.viewBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
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

abstract class BaseRefuelFragment<VM : BaseRefuelViewModel>(@LayoutRes layoutRes: Int) :
    Fragment(layoutRes) {
    protected abstract val baseVM: VM
    protected val binding by viewBinding<FragmentRefuelBinding>()
    private val editable by lazy { Editable.Factory.getInstance() }
    private lateinit var calendar: Calendar
    private var datePicker: MaterialDatePicker<Long>? = null

    private lateinit var fragmentScope: CoroutineScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentScope = viewLifecycleOwner.lifecycleScope
        initUi()
        observeViewModel()
        setListeners()
    }

    private fun initUi() {
        hideSoftInputAndClearViewsFocus(binding.root)
        calendar = Calendar.getInstance(TimeZone.getDefault())
        datePicker =
            parentFragmentManager.findFragmentByTag(DATE_PICKER_TAG) as? MaterialDatePicker<Long>
        if (datePicker != null) addDatePickerListeners()
    }


    private fun setListeners() {
        setOnEditTextChangeListeners()
        setDatePickerListeners()
        setViewsClickListeners()
        setOnCheckBoxChangeListeners()
    }

    private fun setViewsClickListeners() {
        binding.bttClear.setOnClickListener {
            baseVM.onBtnClearClick()
        }
        binding.btnSave.setOnClickListener {
            baseVM.onBtnSaveClick()
        }
    }

    private fun setOnEditTextChangeListeners() {
        binding.etOdometer.addDebouncedListener(fragmentScope) { text ->
            baseVM.onOdometerTextChange(text)
        }
        binding.etFuelVol.addDebouncedListener(fragmentScope) { text ->
            baseVM.onFuelVolumeTextChange(text)
        }
        binding.etPricePerUnit.addDebouncedListener(fragmentScope) { text ->
            baseVM.onPricePerUnitTextChange(text)
        }
        binding.etNotes.addDebouncedListener(fragmentScope) { text ->
            baseVM.onNotesTextChange(text)
        }

    }

    private fun setOnCheckBoxChangeListeners() {
        binding.fullTank.setOnCheckedChangeListener { buttonView, isChecked ->
            baseVM.onFullTankChange(isChecked)
        }
        binding.missedPrevious.setOnCheckedChangeListener { buttonView, isChecked ->
            baseVM.onMissedPreviousChange(isChecked)
        }
    }

    private fun observeViewModel() {
        observeInputFieldsAndSetText()
        observeCheckBoxesAndSetValue()
        observeAndRenderBtnsState()

        fragmentScope.launch {
            launch {
                baseVM.error.collectLatest {
                    if (it.first) {
                        showErrorAndResetState(it.second) {
                            baseVM.onErrorShown()
                        }
                    }
                }
            }
            launch {
                baseVM.date.collectLatest {
                    if (it.pattern.isEmpty()) return@collectLatest
                    setDateInView(it.date, it.pattern)
                }
            }
            launch {
                baseVM.navigation.collectLatest {
                    when (it) {
                        Router.RefuelDirection.ToLogs -> findNavController().popBackStack()
                    }
                }
            }
        }
    }


    private fun observeInputFieldsAndSetText() {
        fragmentScope.launch {
            launch {
                baseVM.odometerInput.collectLatest {
                    binding.etOdometer.setTextFromState(it)
                }
            }
            launch {
                baseVM.fuelVolumeInput.collectLatest {
                    binding.etFuelVol.setTextFromState(it)
                }
            }
            launch {
                baseVM.pricePerUnitInput.collectLatest {
                    binding.etPricePerUnit.setTextFromState(it)
                }
            }
            launch {
                baseVM.notesInput.collectLatest {
                    binding.etNotes.setTextFromState(it)
                }
            }
        }
    }

    private fun observeCheckBoxesAndSetValue() {
        fragmentScope.launch {
            launch {
                baseVM.fullTank.collectLatest {
                    binding.fullTank.isChecked = it
                }
            }
            launch {
                baseVM.missedPrevious.collectLatest {
                    binding.missedPrevious.isChecked = it
                }
            }
        }
    }

    private fun observeAndRenderBtnsState() {
        fragmentScope.launch {
            launch {
                baseVM.isSaveBtnEnabled.collectLatest {
                    binding.btnSave.isEnabled = it
                }
            }
            launch {
                baseVM.isClearBtnEnabled.collectLatest {
                    binding.bttClear.isEnabled = it
                }
            }
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
                    baseVM.onDateChange(timeStamp)

                }
            }
        }
    }

    private fun setDatePickerListeners() {
        binding.etDate.setOnKeyListener { _, _, _ ->
            return@setOnKeyListener true
        }
        binding.etDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && datePicker == null) {
                createDatePicker()
            } else return@setOnFocusChangeListener
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

    private fun setDateInView(timeStamp: Long, pattern: String) {
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