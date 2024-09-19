package voloshyn.android.redrive.presentation.tabs.newRefuel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import voloshyn.android.app.R
import voloshyn.android.app.databinding.FragmentNewRefuelBinding
import voloshyn.android.redrive.utils.viewBinding

class NewRefuelFragment : Fragment(R.layout.fragment_new_refuel) {

    private val binding by viewBinding<FragmentNewRefuelBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDate.setOnFocusChangeListener { v, hasFocus ->
            datePicker()
        }
    }

    private fun datePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.App_ThemeOverlay_DatePicker)
            .build()
        datePicker.show(childFragmentManager, null)
    }
}