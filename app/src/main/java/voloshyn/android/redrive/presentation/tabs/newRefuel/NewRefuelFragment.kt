package voloshyn.android.redrive.presentation.tabs.newRefuel

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentNewRefuelBinding
import voloshyn.android.redrive.utils.viewBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.Delegates

private const val DATE_PICKER_TAG = "DATE_PICKER"

@AndroidEntryPoint
class NewRefuelFragment : Fragment(R.layout.fragment_new_refuel) {

    private val binding by viewBinding<FragmentNewRefuelBinding>()
    private lateinit var calendar: Calendar
    private var datePicker: MaterialDatePicker<Long>? = null
    private val viewModel by viewModels<NewRefuelViewModel>()
    private val editable by lazy { Editable.Factory.getInstance() }
    private lateinit var state: NewRefuelState

    private var _odometer: String by Delegates.observable("") { property, oldValue, newValue ->
        if (oldValue != newValue) {
            saveButtonStateRender(
                fuelVolume = _fuelVolume,
                odometer = _odometer,
                unitPrice = _unitPrice
            )
            binding.odometerContainer.helperText =
                if (_odometer.isNotEmpty()) "" else getString(R.string.required)
        }
    }
    private var _fuelVolume: String by Delegates.observable("") { property, oldValue, newValue ->
        if (oldValue != newValue) {
            saveButtonStateRender(
                fuelVolume = _fuelVolume,
                odometer = _odometer,
                unitPrice = _unitPrice
            )
            binding.fuelVolumeContainer.helperText =
                if (_fuelVolume.isNotEmpty()) "" else getString(R.string.required)
        }
    }
    private var _unitPrice: String by Delegates.observable("") { property, oldValue, newValue ->
        if (oldValue != newValue) {
            saveButtonStateRender(
                fuelVolume = _fuelVolume,
                odometer = _odometer,
                unitPrice = _unitPrice
            )
            binding.unitPriceContainer.helperText =
                if (_unitPrice.isNotEmpty()) "" else getString(R.string.required)
        }
    }

    private var _notes: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dateContainer.isHelperTextEnabled = false
        calendar = Calendar.getInstance(TimeZone.getDefault())
        datePicker =
            parentFragmentManager.findFragmentByTag(DATE_PICKER_TAG) as? MaterialDatePicker<Long>
        if (datePicker != null) addDatePickerListeners()

        saveButtonStateRender()
        addListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                state = it
                setDate(it.date)
                with(binding) {
                    etOdometer.text = editable.newEditable(it.odometer)
                    etFuelVol.text = editable.newEditable(it.fuelVolume)
                    etUnitPrice.text = editable.newEditable(it.unitPrice)
                    etNotes.text = editable.newEditable(it.notes)
                    fullTank.isChecked = it.fullTank
                    missedPrevious.isChecked = it.missedPrevious

                }
            }
        }
        binding.bttSave.setOnClickListener {
            binding.root.children.forEach {
                it.clearFocus()
            }
            viewModel.addNewRefuel()
        }
    }

    private fun saveButtonStateRender(
        fuelVolume: String = "",
        odometer: String = "",
        unitPrice: String = ""
    ) {
        binding.bttSave.isEnabled =
            fuelVolume.isNotEmpty() && odometer.isNotEmpty() && unitPrice.isNotEmpty()
    }


    private fun addListeners() {
        binding.etDate.setOnKeyListener { _, _, _ ->
            return@setOnKeyListener true
        }
        binding.etDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && datePicker == null) {
                createDatePicker()
            } else return@setOnFocusChangeListener
        }

        onOdometerChange()
        onFuelVolumeChange()
        onUnitPriceChange()
        onNotesChange()

        binding.fullTank.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.updateState(state.copy(fullTank = isChecked))
        }
        binding.missedPrevious.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.updateState(state.copy(missedPrevious = isChecked))
        }
    }

    private fun onOdometerChange() {
        binding.etOdometer.doOnTextChanged { text, start, before, count ->
            _odometer = text.toString()
        }
        binding.etOdometer.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && state.odometer != _odometer) {
                viewModel.updateState(state.copy(odometer = _odometer))
            }
        }
    }

    private fun onFuelVolumeChange() {
        binding.etFuelVol.doOnTextChanged { text, start, before, count ->
            _fuelVolume = text.toString()
        }
        binding.etFuelVol.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && state.fuelVolume != _fuelVolume) {
                viewModel.updateState(state.copy(fuelVolume = _fuelVolume))
            }
        }
    }

    private fun onUnitPriceChange() {
        binding.etUnitPrice.doOnTextChanged { text, start, before, count ->
            _unitPrice = text.toString()
        }
        binding.etUnitPrice.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && state.unitPrice != _unitPrice) {
                viewModel.updateState(state.copy(unitPrice = _unitPrice))
            }
        }
    }

    private fun onNotesChange() {
        binding.etNotes.doOnTextChanged { text, start, before, count ->
            _notes = text.toString()
        }
        binding.etNotes.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && state.notes != _notes) {
                viewModel.updateState(state.copy(notes = _notes))
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
                    viewModel.updateState(NewRefuelState(date = timeStamp))

                }
            }
        }
    }

    private fun setDate(timeStamp: Long) {
        val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = dateFormater.format(timeStamp)
        binding.etDate.text = editable.newEditable(date)
    }

    private fun dateContainerClearFocus() {
        binding.etDate.clearFocus()
        datePicker = null
    }

}