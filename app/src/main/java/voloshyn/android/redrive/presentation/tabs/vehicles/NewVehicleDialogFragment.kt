package voloshyn.android.redrive.presentation.tabs.vehicles

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import voloshyn.android.app.R
import voloshyn.android.app.databinding.DialogNewVehicleBinding
import voloshyn.android.domain.models.tabs.redrive.Vehicle

interface DialogFragmentListener

class NewVehicleDialogFragment : DialogFragment(), DialogFragmentListener {
    private lateinit var binding: DialogNewVehicleBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewVehicleBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener {
            onTextFieldsValueChange()
            binding.etVehicleName.requestFocus()
            binding.etVehicleName.postDelayed({
                showKeyboard(binding.etVehicleName)
            }, 100)
        }

        binding.bttCancel.setOnClickListener {
            dismiss()
        }
        binding.bttConfirm.setOnClickListener {
            val vehicle = confirmVehicleData() ?: return@setOnClickListener
        setFragmentResult(
                REQUEST_KEY,
                bundleOf(KEY_VEHICLE_RESPONSE to vehicle)
            )
            dismiss()
        }

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        hideKeyboard(binding.etVehicleName)
    }

    private fun confirmVehicleData(): Vehicle? {
        val vehicleName = binding.etVehicleName.text.toString()
        val odometer = binding.etOdometer.text.toString()
        validateVehicleName(vehicleName)
        validateOdometer(odometer)

        return if (vehicleName.isNotEmpty() && odometer.isNotEmpty()) {
            val vehicle = Vehicle(id = 0, name = vehicleName, currentMileage = odometer.toInt())
            vehicle
        } else return null
    }

    private fun onTextFieldsValueChange() {
        binding.etVehicleName.doOnTextChanged { text, start, before, count ->
            validateVehicleName(name = text)
        }
        binding.etOdometer.doOnTextChanged { text, start, before, count ->
            validateOdometer(text)
        }
    }

    private fun validateVehicleName(name: CharSequence?) {
        if (name.toString().isNotEmpty()) {
            binding.vehicleContainer.helperText = null
            binding.vehicleContainer.error = null
        } else {
            binding.vehicleContainer.error = getString(R.string.empty_field_error)
        }
    }

    private fun validateOdometer(odometer: CharSequence?) {
        if (odometer.toString().isNotEmpty()) {
            binding.odometerContainer.helperText = null
            binding.odometerContainer.error = null
        } else {
            binding.odometerContainer.error = getString(R.string.empty_field_error)
        }
    }


    private fun hideKeyboard(view: View) {
        getInputMethodManager().hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        getInputMethodManager().showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun getInputMethodManager(): InputMethodManager {
        val context = requireContext()
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    companion object {
        private val TAG = NewVehicleDialogFragment::class.java.simpleName
        const val KEY_VEHICLE_RESPONSE = "KEY_VEHICLE_RESPONSE"
        val REQUEST_KEY = "$TAG:defaultRequestKey"
    }
}